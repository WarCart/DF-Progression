package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.charactercreator.RaceId;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;

public class RaceRequirement extends Requirement {
    public RaceRequirement() {
        super(RaceId.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        return EntityStatsCapability.get(entity).getRace().toString().equalsIgnoreCase(instance.getValues()[0]);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("race").getAsString());
        return instance;
    }
}
