package net.warcar.fruit_progression.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.data.entity.abilities_addition.SSyncAdditionalDataPacket;
import net.warcar.fruit_progression.new_data_reader.RequirementModifiersPile;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.events.ability.EquipAbilityEvent;
import xyz.pixelatedw.mineminenomi.api.events.ability.UnlockAbilityEvent;
import xyz.pixelatedw.mineminenomi.api.events.entity.SetPlayerDetailsEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.DorikiEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.HakiExpEvent;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.handlers.ability.ProgressionHandler;
import xyz.pixelatedw.mineminenomi.init.ModNetwork;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncDevilFruitPacket;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class UnlockEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onUnlock(UnlockAbilityEvent event) {
        ResourceLocation location = event.getAbilityCore().getRegistryKey();
        if (location != null && DevilFruitProgressionMod.ABILITIES_READER.map.containsKey(location)) {
            RequirementSetInstance instance = DevilFruitProgressionMod.ABILITIES_READER.map.get(location).requirementSetInstance();
            if (instance != null) {
                if (instance.isFulfilled(event.getEntity(), event.getAbilityCore())) {
                    event.setResult(Event.Result.ALLOW);
                } else {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHakiCheck(HakiExpEvent.Post event) {
        checkForAwakening(event.getEntity());
        ProgressionHandler.checkAllForNewUnlocks(event.getEntity(), true);
    }

    @SubscribeEvent
    public static void onUseAbilityCheck(AbilityUseEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            AbilityAdditionDataCapability.get(player).ifPresent(cap -> cap.addUsages(event.getAbility().getCore()));
            checkForAwakening(player);
            ProgressionHandler.checkAllForNewUnlocks(player, true);
        }
    }

    @SubscribeEvent
    public static void onDorikiCheck(DorikiEvent.Post event) {
        checkForAwakening(event.getEntity());
        ProgressionHandler.checkForDevilFruitUnlocks(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerCreation(SetPlayerDetailsEvent event) {
        checkForAwakening(event.getEntity());
        ProgressionHandler.checkForDevilFruitUnlocks(event.getEntity());
    }

    private static void checkForAwakening(LivingEntity player) {
        DevilFruitCapability.get(player).ifPresent(props -> props.getDevilFruit().ifPresent(fruit -> {
            RequirementSetInstance instance = DevilFruitProgressionMod.AWAKENINGS_READER.map.get(fruit);
            if (instance != null) {
                if (props.hasAwakenedFruit() != instance.isFulfilled(player, null)) {
                    props.setAwakenedFruit(instance.isFulfilled(player, null));
                    if (player instanceof Player) {
                        ModNetwork.sendTo(new SSyncDevilFruitPacket(player, props), (Player) player);
                    }
                }
            }
        }));
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(ResourceLocation.fromNamespaceAndPath(DevilFruitProgressionMod.MOD_ID, "ability_addition"), new AbilityAdditionDataCapability());
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        AbilityAdditionDataCapability.get(player).ifPresent(props -> 
                DevilFruitProgressionMod.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SSyncAdditionalDataPacket(player, props)));
    }

    @SubscribeEvent
    public static void onAbilityEquipped(EquipAbilityEvent event) {
        IAbility ability = event.getAbility();
        ResourceLocation name = ability.getCore().getRegistryKey();
        if (name != null && DevilFruitProgressionMod.ABILITIES_READER.map.containsKey(name)) {
            RequirementModifiersPile pile = DevilFruitProgressionMod.ABILITIES_READER.map.get(name);
            pile.apply(ability);
        }
    }
}
