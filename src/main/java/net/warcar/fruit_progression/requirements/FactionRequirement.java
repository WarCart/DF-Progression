package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.entities.charactercreator.Faction;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.IEntityStats;

import java.util.Optional;

public class FactionRequirement extends Requirement {
    public FactionRequirement() {
        super(Faction.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IEntityStats> statsOptional = EntityStatsCapability.get(entity);
        if (statsOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no stats, can't check fraction", entity);
            }
            return false;
        }
        IEntityStats entityStats = statsOptional.get();
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info("{}/{}", entityStats.getFaction(), instance.getValues()[0]);
        }
        return entityStats.getFaction().toString().equalsIgnoreCase(instance.getValues()[0]);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("faction").getAsString());
        return instance;
    }
}
