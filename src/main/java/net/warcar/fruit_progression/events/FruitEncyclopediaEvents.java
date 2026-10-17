package net.warcar.fruit_progression.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.config.ModConfig;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.init.ModRequirements;
import net.warcar.fruit_progression.init.ModTexts;
import net.warcar.fruit_progression.network.SSyncAdditionalDataPacket;
import xyz.pixelatedw.mineminenomi.api.DFEncyclopediaEntry;
import xyz.pixelatedw.mineminenomi.config.ServerConfig;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.ModItems;
import xyz.pixelatedw.mineminenomi.init.ModNetwork;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18n;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.items.DFEncyclopediaItem;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncEntityStatsPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncQuestDataPacket;
import xyz.pixelatedw.mineminenomi.packets.server.ui.SOpenEncyclopediaScreenPacket;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class FruitEncyclopediaEvents {
    public static final Component STOLEN_CONFIG_ERROR;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!ModConfig.isEncyclopediaOverhauled()) {
            return;
        }
        Player player = event.getEntity();
        ItemStack heldStack = event.getItemStack();
        IDevilFruit props = DevilFruitCapability.get(player).orElse(null);

        if (handleClueUsage(player, heldStack)) {
            event.cancel();
        }
        if (handleBook(player, heldStack)) {
            event.cancel();
        }
    }

    public static boolean handleClueUsage(Player player, ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != Items.PAPER || !stack.hasTag() || stack.getTag().isEmpty() || player.level().isClientSide) {
            return false;
        }

        if(stack.getTagElement(DFEncyclopediaEntry.FRUIT_CLUES_TAG) == null) {
            return false;
        }

        CompoundTag nbt = stack.getTagElement(DFEncyclopediaEntry.FRUIT_CLUES_TAG);
        ResourceLocation key = ResourceLocation.parse(nbt.getString(DFEncyclopediaEntry.FRUIT_KEY_TAG));
        AtomicBoolean flag = new AtomicBoolean(false);
        if (ForgeRegistries.ITEMS.getValue(key) instanceof AkumaNoMiItem df) {
            AbilityAdditionDataCapability.getLazy(player).ifPresent(data -> {
                flag.set(data.readAbout(df));
            });
            ModNetwork.sendToAllTrackingAndSelf(new SSyncEntityStatsPacket(player, EntityStatsCapability.get(player)), player);
        } else {
            DevilFruitProgressionMod.LOGGER.warn("{} is not a devil fruit?", key);
        }

        if (!ServerConfig.getRandomizedFruits()) {
            Item item = ForgeRegistries.ITEMS.getValue(key);
            if (flag.get()) {
                stack.shrink(1);
                player.displayClientMessage(Component.translatable(ModI18n.ITEM_MESSAGE_GAINED_FRUIT_CLUE, item.getDescription().getString()), false);
                return false;
            } else {
                player.displayClientMessage(Component.translatable(ModTexts.ALREADY_READ, item.getDescription().getString()), false);
                return true;
            }
        }
        return false;
    }

    public static boolean handleBook(Player player, ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != ModItems.DEVIL_FRUIT_ENCYCLOPEDIA.get() || player.level().isClientSide) {
            return false;
        }
        AbilityAdditionDataCapability.getLazy(player).ifPresent(data -> {
            CompoundTag unlocked = stack.getTagElement(DFEncyclopediaEntry.ENCYCLOPEDIA_UNLOCKED_TAG);
            if (unlocked != null) {
                for (String id : unlocked.getAllKeys()) {
                    ResourceLocation loc = ResourceLocation.tryParse(id);
                    if (loc != null && ForgeRegistries.ITEMS.getValue(loc) instanceof AkumaNoMiItem df) {
                        if (data.readAbout(df))
                            player.displayClientMessage(Component.translatable(ModI18n.ITEM_MESSAGE_GAINED_FRUIT_CLUE, df.getDescription().getString()), false);
                    }
                }
            }
            for (AkumaNoMiItem df : data.allReadFruits()) {
                if (DevilFruitCapability.hasDevilFruit(player, df) && !data.hasReadAboutOwnFruit()) {
                    EntityStatsCapability.getLazy(player).ifPresent(props -> {
                        props.alterTrainingPoints(ModRequirements.DEVIL_FRUIT_POINTS, ModConfig.getPointsForOwnFruit() - ModConfig.getPointsForOtherFruit());
                    });
                    data.readAboutOwnFruit();
                }
            }
            ModNetwork.sendToAllTrackingAndSelf(new SSyncEntityStatsPacket(player), player);
            DevilFruitProgressionMod.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SSyncAdditionalDataPacket(player, data));
        });
        ModNetwork.sendTo(new SOpenEncyclopediaScreenPacket(stack), player);
        ModNetwork.sendTo(new SSyncQuestDataPacket(player), player);
        return !ServerConfig.getRandomizedFruits();
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (ModConfig.isEncyclopediaOverhauled() && !ServerConfig.getRandomizedFruits() && event.getItemStack().getItem() instanceof DFEncyclopediaItem) {
            event.getToolTip().remove(STOLEN_CONFIG_ERROR);
        }
    }

    static {
        Component c = null;
        try {
            Field error = DFEncyclopediaItem.class.getDeclaredField("CONFIG_ERROR");
            error.setAccessible(true);
            c = (Component) error.get(null);
            error.setAccessible(false);
        } catch (ReflectiveOperationException ignored) {
        }
        STOLEN_CONFIG_ERROR = c;
    }
}
