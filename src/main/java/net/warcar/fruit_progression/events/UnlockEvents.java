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
import net.warcar.fruit_progression.init.ModConfig;
import net.warcar.fruit_progression.requirements.Requirement;
import net.warcar.fruit_progression.requirements.RequirementInstance;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.events.SetPlayerDetailsEvent;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.events.ability.UnlockAbilityEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.DorikiEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.HakiExpEvent;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.events.abilities.AbilityProgressionEvents;

import java.util.List;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class UnlockEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onUnlock(UnlockAbilityEvent event) {
        ResourceLocation location = event.getAbilityCore().getRegistryName();
        if (location != null && ModConfig.INSTANCE.getAbilities().containsKey(location.toString())) {
            RequirementSetInstance instance = ModConfig.INSTANCE.getAbilities().get(location.toString());
            if (instance != null) {
                if (isFulfilled(ModConfig.INSTANCE.getAbilities().get(location.toString()), event.getEntityLiving(), event.getAbilityCore())) {
                    event.setResult(Event.Result.ALLOW);
                } else {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    private static boolean isFulfilled(RequirementSetInstance instance, LivingEntity player, AbilityCore<?> core) {
        List<List<RequirementInstance>> reqsSquared = instance.reqs;
        boolean stoppedOuter = false;
        if (reqsSquared != null) {
            for (List<RequirementInstance> requirements : reqsSquared) {
                boolean stoppedInner = true;
                for (RequirementInstance requirement : requirements) {
                    boolean reqOutput = requirement.getCore().requirementMet(player, core, requirement);
                    if (requirement.isInverted()) {
                        reqOutput = !reqOutput;
                    }
                    stoppedInner = stoppedInner && reqOutput;
                }
                stoppedOuter = stoppedOuter || stoppedInner;
            }
        }
        return stoppedOuter;
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
            RequirementSetInstance instance = ModConfig.INSTANCE.getAwakenings().get(fruit.toString());
            if (instance != null) {
                props.setAwakenedFruit(isFulfilled(instance, player, null));
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
