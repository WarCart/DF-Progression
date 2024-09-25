package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

public class DefaultReqirement extends Requirement {
    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        if (core == null) {
            return false;
        }
        return core.canUnlock(entity);
    }
}
