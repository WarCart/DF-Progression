package net.warcar.fruit_progression.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import xyz.pixelatedw.mineminenomi.api.DFEncyclopediaEntry;
import xyz.pixelatedw.mineminenomi.config.ServerConfig;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.init.ModItems;
import xyz.pixelatedw.mineminenomi.init.ModNetwork;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18n;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.packets.server.ui.SOpenEncyclopediaScreenPacket;

import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class FruitEncyclopediaEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack heldStack = event.getItemStack();
        IDevilFruit props = DevilFruitCapability.get(player).orElse(null);

        handleClueUsage(player, heldStack);
        if (handleBook(player, heldStack)) {
            event.cancel();
        }
    }

    public static void handleClueUsage(Player player, ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != Items.PAPER || !stack.hasTag() || stack.getTag().isEmpty() || player.level().isClientSide) {
            return;
        }

        if(stack.getTagElement(DFEncyclopediaEntry.FRUIT_CLUES_TAG) == null) {
            return;
        }

        CompoundTag nbt = stack.getTagElement(DFEncyclopediaEntry.FRUIT_CLUES_TAG);
        ResourceLocation key = ResourceLocation.parse(nbt.getString(DFEncyclopediaEntry.FRUIT_KEY_TAG));
        AtomicBoolean flag = new AtomicBoolean(false);
        if (ForgeRegistries.ITEMS.getValue(key) instanceof AkumaNoMiItem df) {
            AbilityAdditionDataCapability.getLazy(player).ifPresent(data -> {
                flag.set(data.readAbout(df));
            });
        } else {
            DevilFruitProgressionMod.LOGGER.debug("{} is not a devil fruit?", key);
        }

        if(flag.get() && !ServerConfig.getRandomizedFruits()) {
            stack.shrink(1);
            Item item = ForgeRegistries.ITEMS.getValue(key);
            player.displayClientMessage(Component.translatable(ModI18n.ITEM_MESSAGE_GAINED_FRUIT_CLUE, item.getDescription().getString()), false);
        }
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
        });
        ModNetwork.sendTo(new SOpenEncyclopediaScreenPacket(stack), player);
        return !ServerConfig.getRandomizedFruits();
    }
}
