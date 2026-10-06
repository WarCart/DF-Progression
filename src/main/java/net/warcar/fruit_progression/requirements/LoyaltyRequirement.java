package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModTexts;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.IEntityStats;

import java.util.Optional;

public class LoyaltyRequirement extends Requirement {
    public LoyaltyRequirement() {
        super(Double.TYPE);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IEntityStats> statsOptional = EntityStatsCapability.get(entity);
        if (statsOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no stats, can't check loyalty", entity);
            }
            return false;
        }
        return statsOptional.get().getLoyalty() >= Double.parseDouble(instance.getValues()[0]);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(String.valueOf(json.get("loyalty").getAsDouble()));
        return instance;
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        return Component.translatable(ModTexts.NEEDS_LOYALTY, instance.getValues()[0]);
    }
}
