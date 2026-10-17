package net.warcar.fruit_progression.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.MorphAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.quests.objectives.IUseAbilityObjective;
import xyz.pixelatedw.mineminenomi.data.entity.quest.QuestCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class TrainingEvents {
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
                data.addDevilFruitMastery(0.5f);
            });
        }
    }

    private static boolean isUseCase(IAbility ability) {
        if (ability instanceof MorphAbility) {
            return false;
        }
        Optional<ContinuousComponent> optContinuous = ability.getComponent(ModAbilityComponents.CONTINUOUS.get());
        if (optContinuous.isPresent()) {
            return optContinuous.get().isContinuous();
        }
        return true;
    }
}
