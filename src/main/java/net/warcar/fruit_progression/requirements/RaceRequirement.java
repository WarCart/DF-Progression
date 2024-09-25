package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import net.warcar.fruit_progression.integrations.non_fruit_rework.ApplyOtherClasses;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;

public class RaceRequirement extends Requirement {
    public RaceRequirement() {
        super(getRequirements());
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        return EntityStatsCapability.get(entity).getRace().equalsIgnoreCase(instance.getValues()[0]);
    }

    private static Class<?>[] getRequirements() {
        Class<?>[] out = new Class[]{String.class};
        if (ModList.get().isLoaded("non_fruit_rework")) {
            ApplyOtherClasses.race(out);
        }
        return out;
    }
}
