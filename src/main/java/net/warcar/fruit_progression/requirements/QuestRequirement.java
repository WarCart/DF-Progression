package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.data.entity.quests.QuestDataCapability;

public class QuestRequirement extends Requirement {
    public QuestRequirement() {
        super(QuestId.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        if (entity instanceof PlayerEntity) {
            return QuestDataCapability.get((PlayerEntity) entity).hasFinishedQuest((QuestId) GameRegistry.findRegistry(QuestId.class).getValue(new ResourceLocation(instance.getValues()[0])));
        }
        return false;
    }
}
