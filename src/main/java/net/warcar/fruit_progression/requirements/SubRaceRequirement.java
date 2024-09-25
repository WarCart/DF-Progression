package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;

public class SubRaceRequirement extends Requirement {
    public SubRaceRequirement() {
        super(String.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        return EntityStatsCapability.get(entity).getSubRace().equalsIgnoreCase(instance.getValues()[0]);
    }
}
