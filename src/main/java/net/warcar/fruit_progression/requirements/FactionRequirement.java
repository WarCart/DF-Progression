package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;

public class FactionRequirement extends Requirement {
    public FactionRequirement() {
        super(String.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info(EntityStatsCapability.get(entity).getFaction() + "/" + instance.getValues()[0]);
        }
        return EntityStatsCapability.get(entity).getFaction().equalsIgnoreCase(instance.getValues()[0]);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("faction").getAsString());
        return instance;
    }
}
