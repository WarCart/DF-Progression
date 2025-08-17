package net.warcar.fruit_progression.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataProvider;
import net.warcar.fruit_progression.data.entity.abilities_addition.IAbilityAdditionData;
import net.warcar.fruit_progression.data.entity.abilities_addition.SSyncAdditionalDataPacket;
import net.warcar.fruit_progression.new_data_reader.AbilityDataReader;
import net.warcar.fruit_progression.new_data_reader.RequirementModifiersPile;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.events.SetPlayerDetailsEvent;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.events.ability.EquipAbilityEvent;
import xyz.pixelatedw.mineminenomi.api.events.ability.UnlockAbilityEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.DorikiEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.HakiExpEvent;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.events.abilities.AbilityProgressionEvents;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncDevilFruitPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class UnlockEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onUnlock(UnlockAbilityEvent event) {
        ResourceLocation location = event.getAbilityCore().getRegistryName();
        if (location != null && DevilFruitProgressionMod.ABILITIES_READER.map.containsKey(location)) {
            RequirementSetInstance instance = DevilFruitProgressionMod.ABILITIES_READER.map.get(location).getRequirementSetInstance();
            if (instance != null) {
                if (instance.isFulfilled(event.getEntityLiving(), event.getAbilityCore())) {
                    event.setResult(Event.Result.ALLOW);
                } else {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHakiCheck(HakiExpEvent.Post event) {
        checkForAwakening(event.getPlayer());
        AbilityProgressionEvents.checkForDevilFruitUnlocks(event.getPlayer());
    }

    @SubscribeEvent
    public static void onUseAbilityCheck(AbilityUseEvent.Post event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            AbilityAdditionDataCapability.get(player).addUsages(event.getAbility().getCore());
            checkForAwakening(player);
            AbilityProgressionEvents.checkForDevilFruitUnlocks(player);
        }
    }

    @SubscribeEvent
    public static void onDorikiCheck(DorikiEvent.Post event) {
        checkForAwakening(event.getPlayer());
        AbilityProgressionEvents.checkForDevilFruitUnlocks(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerCreation(SetPlayerDetailsEvent event) {
        checkForAwakening(event.getPlayer());
        AbilityProgressionEvents.checkForDevilFruitUnlocks(event.getPlayer());
    }

    private static void checkForAwakening(LivingEntity player) {
        IDevilFruit props = DevilFruitCapability.get(player);
        props.getDevilFruit().ifPresent(fruit -> {
            RequirementSetInstance instance = DevilFruitProgressionMod.AWAKENINGS_READER.map.get(fruit);
            if (instance != null) {
                if (props.hasAwakenedFruit() != instance.isFulfilled(player, null)) {
                    props.setAwakenedFruit(instance.isFulfilled(player, null));
                    if (player instanceof PlayerEntity) {
                        WyNetwork.sendTo(new SSyncDevilFruitPacket(player.getId(), props), (PlayerEntity) player);
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(DevilFruitProgressionMod.MOD_ID, "ability_addition"), new AbilityAdditionDataProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlayerEntity player = event.getPlayer();
        IAbilityAdditionData props = AbilityAdditionDataCapability.get(player);
        DevilFruitProgressionMod.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SSyncAdditionalDataPacket(player, props));
    }

    @SubscribeEvent
    public static void onAbilityEquipped(EquipAbilityEvent event) {
        IAbility ability = event.getAbility();
        ResourceLocation name = ability.getCore().getRegistryName();
        if (name != null && DevilFruitProgressionMod.ABILITIES_READER.map.containsKey(name)) {
            RequirementModifiersPile pile = DevilFruitProgressionMod.ABILITIES_READER.map.get(name);
            pile.apply(ability);
        }
    }
}
