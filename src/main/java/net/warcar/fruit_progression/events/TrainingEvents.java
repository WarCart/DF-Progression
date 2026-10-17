package net.warcar.fruit_progression.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.config.ModConfig;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.MorphAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.quests.objectives.IUseAbilityObjective;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.quest.QuestCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;

import java.util.Optional;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class TrainingEvents {
    public static final Predicate<Player> IS_MORPHED = (player) -> {
        MorphInfo info = DevilFruitCapability.get(player).flatMap(IDevilFruit::getCurrentMorph).orElse(null);
        return info != null;
    };

    @SubscribeEvent
    public static void onAbilityUsed(AbilityUseEvent.Post event) {
        IAbility ability = event.getAbility();
        if (event.getEntity() instanceof Player player && isUseCase(ability)) {
            QuestCapability.get(player).ifPresent(data -> {
                data.getInProgressObjectives().forEach(objective -> {
                    if (objective instanceof IUseAbilityObjective obj && obj.checkAbility(player, ability)) {
                        objective.alterProgress(player, 1);
                    }
                });
            });
            AbilityAdditionDataCapability.get(player).ifPresent(data -> {
                data.addDevilFruitMastery(ModConfig.getMasteryPerUse());
            });
        }
    }

    private static boolean isUseCase(IAbility ability) {
        if (ability instanceof MorphAbility) {
            return false;
        }
        Optional<ContinuousComponent> optContinuous = ability.getComponent(ModAbilityComponents.CONTINUOUS.get());
        if (optContinuous.isPresent()) {
            Optional<ChargeComponent> charge = ability.getComponent(ModAbilityComponents.CHARGE.get());
            if (charge.isPresent() && charge.get().isCharging()) {
                return true;
            }
            return optContinuous.get().isContinuous();
        }
        return true;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (IS_MORPHED.test(event.player)) {
            AbilityAdditionDataCapability.get(event.player).ifPresent(data -> {
                data.addDevilFruitMastery(ModConfig.getMasteryPerTick());
            });
        }
    }
}
