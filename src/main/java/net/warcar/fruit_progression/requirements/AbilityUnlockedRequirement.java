package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;

public class AbilityUnlockedRequirement extends Requirement {
    public AbilityUnlockedRequirement() {
        super(AbilityCore.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        return AbilityCapability.get(entity).get().hasEquippedAbility(WyRegistry.ABILITIES.get().getValue(ResourceLocation.parse(instance.getValues()[0])));
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("ability").getAsString());
        return instance;
    }
}
