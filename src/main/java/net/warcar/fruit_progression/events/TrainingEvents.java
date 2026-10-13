package net.warcar.fruit_progression.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.quests.objectives.IUseAbilityObjective;
import xyz.pixelatedw.mineminenomi.data.entity.quest.QuestCapability;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class TrainingEvents {
    @SubscribeEvent
    public static void onAbilityUsed(AbilityUseEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            QuestCapability.get(player).ifPresent(data -> {
                data.getInProgressObjectives().forEach(objective -> {
                    if (objective instanceof IUseAbilityObjective obj && obj.checkAbility(player, event.getAbility())) {
                        objective.alterProgress(player, 1);
                    }
                });
            });
        }
    }
}
