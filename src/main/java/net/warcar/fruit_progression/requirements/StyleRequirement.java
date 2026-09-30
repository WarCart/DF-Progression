package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.entities.charactercreator.FightingStyle;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;

public class StyleRequirement extends Requirement {
    public StyleRequirement() {
        super(FightingStyle.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        String style = EntityStatsCapability.get(entity).get().getFightingStyle().get().toString();
        String required = instance.getValues()[0];
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info("style: " + style);
            DevilFruitProgressionMod.LOGGER.info("req: " + required);
            DevilFruitProgressionMod.LOGGER.info("self: " + this);
        }
        return style.equalsIgnoreCase(required);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("style").getAsString());
        return instance;
    }
}
