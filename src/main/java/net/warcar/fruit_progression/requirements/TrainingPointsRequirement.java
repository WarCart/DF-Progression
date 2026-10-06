package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.enums.TrainingPointType;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.IEntityStats;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nNodes;

import java.util.Optional;

public class TrainingPointsRequirement extends Requirement {
    public TrainingPointsRequirement() {
        super(Float.TYPE, TrainingPointType.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        float target = Float.parseFloat(instance.getValues()[0]);
        TrainingPointType type = TrainingPointType.valueOf(instance.getValues()[1]);
        Optional<IEntityStats> statsOptional = EntityStatsCapability.get(entity);
        if (statsOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no stats, can't check training points", entity);
            }
            return false;
        }
        int points = statsOptional.get().getTrainingPoints(type);
        if (instance.isDebug()) DevilFruitProgressionMod.LOGGER.info("{}:{}/{}", type, points, target);
        return points >= target;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("points").getAsString(), json.get("type").getAsString());
        return instance;
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        String id = switch (instance.getValues()[1]) {
            case "ACCURACY" -> ModI18nNodes.ACCURACY_POINTS_CHECK;
            case "MARTIAL_ARTS" -> ModI18nNodes.MARTIAL_ARTS_POINTS_CHECK;
            case "TECHNOLOGY" -> ModI18nNodes.TECHNOLOGY_POINTS_CHECK;
            case "WEAPON_MASTERY" -> ModI18nNodes.WEAPON_MASTERY_POINTS_CHECK;
            case "DEVIL_FRUIT" -> "";
            default -> null;
        };

        if (id == null) {
            return Component.empty();
        }

        return Component.translatable(id, instance.getValues()[0]);
    }
}
