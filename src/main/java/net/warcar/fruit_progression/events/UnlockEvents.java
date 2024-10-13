package net.warcar.fruit_progression.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataProvider;
import net.warcar.fruit_progression.new_data_reader.AbilityDataReader;
import net.warcar.fruit_progression.new_data_reader.AwakeningDataReader;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import xyz.pixelatedw.mineminenomi.api.events.SetPlayerDetailsEvent;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.events.ability.UnlockAbilityEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.DorikiEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.HakiExpEvent;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.events.abilities.AbilityProgressionEvents;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class UnlockEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onUnlock(UnlockAbilityEvent event) {
        ResourceLocation location = event.getAbilityCore().getRegistryName();
        if (location != null && AbilityDataReader.map.containsKey(location)) {
            RequirementSetInstance instance = AbilityDataReader.map.get(location);
            if (instance != null) {
                if (AbilityDataReader.map.get(location).isFulfilled(event.getEntityLiving(), event.getAbilityCore())) {
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
            RequirementSetInstance instance = AwakeningDataReader.map.get(fruit);
            if (instance != null) {
                props.setAwakenedFruit(instance.isFulfilled(player, null));
            }
        });
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(DevilFruitProgressionMod.MOD_ID, "ability_addition"), new AbilityAdditionDataProvider());
        }
    }
}
