package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;

public class LoyaltyRequirement extends Requirement {
    public LoyaltyRequirement() {
        super(Double.TYPE);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        return EntityStatsCapability.get(entity).getLoyalty() < Double.parseDouble(instance.getValues()[0]);
    }
}
