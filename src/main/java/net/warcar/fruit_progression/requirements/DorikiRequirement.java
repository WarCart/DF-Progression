package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;

public class DorikiRequirement extends Requirement {
    public DorikiRequirement() {
        super(Double.TYPE);
        this.optionalVals = new Class[]{Boolean.TYPE};
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        double targetDoriki = Double.parseDouble(instance.getValues()[0]);
        boolean percentage;
        if (instance.getValues().length > 1) {
            percentage = Boolean.parseBoolean(instance.getValues()[1]);
        } else {
            percentage = false;
        }
        if (percentage) {
            if (instance.isDebug()) DevilFruitProgressionMod.LOGGER.info(EntityStatsCapability.get(entity).getDoriki() / CommonConfig.INSTANCE.getDorikiLimit() + "/" + targetDoriki);
            return EntityStatsCapability.get(entity).getDoriki() / CommonConfig.INSTANCE.getDorikiLimit() >= targetDoriki;
        }
        if (instance.isDebug()) DevilFruitProgressionMod.LOGGER.info(EntityStatsCapability.get(entity).getDoriki() + "/" + targetDoriki);
        return EntityStatsCapability.get(entity).getDoriki() >= targetDoriki;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        String[] args = {json.get("doriki").getAsString()};
        if (json.has("percentage")) {
            args = new String[]{json.get("doriki").getAsString(), Boolean.toString(json.get("percentage").getAsBoolean())};
        }
        instance.setValues(args);
        return instance;
    }
}
