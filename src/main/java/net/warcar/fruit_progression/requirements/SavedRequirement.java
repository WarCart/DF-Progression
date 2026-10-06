package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModDataReaders;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

public class SavedRequirement extends Requirement {
    public SavedRequirement() {
        super(Requirement.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        RequirementSetInstance saved = ModDataReaders.SAVED_REQUIREMENTS_READER.get(ResourceLocation.parse(instance.getValues()[0]));
        if (saved == null) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Saved instance {} isn't loaded!", instance.getValues()[0]);
            }
            return false;
        }
        return saved.isFulfilled(entity, core);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("requirement").getAsString());
        return instance;
    }

    public RequirementInstance simpleInstance(String id) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(id);
        return instance;
    }
}
