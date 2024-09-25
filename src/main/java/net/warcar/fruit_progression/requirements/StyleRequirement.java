package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.integrations.non_fruit_rework.ApplyOtherClasses;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;

public class StyleRequirement extends Requirement {
    public StyleRequirement() {
        super(getRequirements());
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        String style = EntityStatsCapability.get(entity).getFightingStyle();
        String required = instance.getValues()[0];
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info("style: " + style);
            DevilFruitProgressionMod.LOGGER.info("req: " + required);
            DevilFruitProgressionMod.LOGGER.info("self: " + this);
        }
        return style.equalsIgnoreCase(required);
    }

    private static Class<?>[] getRequirements() {
        Class<?>[] out = new Class[]{String.class};
        if (ModList.get().isLoaded("non_fruit_rework")) {
            ApplyOtherClasses.style(out);
        }
        return out;
    }
}
