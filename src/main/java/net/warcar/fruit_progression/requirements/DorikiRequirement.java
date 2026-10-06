package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.config.GeneralConfig;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.IEntityStats;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nNodes;

import java.util.Optional;

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
        Optional<IEntityStats> statsOptional = EntityStatsCapability.get(entity);
        if (statsOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no stats, can't check doriki", entity);
            }
            return false;
        }
        IEntityStats stats = statsOptional.get();
        if (percentage) {
            if (instance.isDebug())
                DevilFruitProgressionMod.LOGGER.info("{}/{}", stats.getDoriki() / GeneralConfig.DORIKI_LIMIT.get(), targetDoriki);
            return stats.getDoriki() / GeneralConfig.DORIKI_LIMIT.get() >= targetDoriki;
        }
        if (instance.isDebug()) DevilFruitProgressionMod.LOGGER.info("{}/{}", stats.getDoriki(), targetDoriki);
        return stats.getDoriki() >= targetDoriki;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        String[] args = {json.get("doriki").getAsString()};
        if (json.has("percentage")) {
            args = new String[]{args[0], Boolean.toString(json.get("percentage").getAsBoolean())};
        }
        instance.setValues(args);
        return instance;
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        return Component.translatable(ModI18nNodes.DORIKI_CHECK, instance.getValues()[0]);
    }
}
