package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.entities.charactercreator.FightingStyle;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.IEntityStats;

import java.util.Optional;

public class StyleRequirement extends Requirement {
    public StyleRequirement() {
        super(FightingStyle.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IEntityStats> statsOptional = EntityStatsCapability.get(entity);
        if (statsOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no stats, can't check style", entity);
            }
            return false;
        }
        Optional<FightingStyle> styleOptional = statsOptional.get().getFightingStyle();
        if (styleOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no style", entity);
            }
            return false;
        }
        String style = styleOptional.get().toString();
        String required = instance.getValues()[0];
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info("style: {}", style);
            DevilFruitProgressionMod.LOGGER.info("req: {}", required);
            DevilFruitProgressionMod.LOGGER.info("self: {}", this);
        }
        return style.equalsIgnoreCase(required);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("style").getAsString());
        return instance;
    }
}
