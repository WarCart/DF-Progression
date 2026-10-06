package net.warcar.fruit_progression.data.mixin_interfaces;

import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.requirements.RequirementInstance;

public interface INodeMixin {
    boolean ability_progression$isVisible(LivingEntity entity);

    void ability_progression$setRequirement(RequirementInstance instance);
}
