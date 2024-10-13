package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

public abstract class Requirement extends ForgeRegistryEntry<Requirement> {
    private final Class<?>[] requiredVals;
    protected Class<?>[] optionalVals = new Class[0];

    protected Requirement(Class<?>... requiredVals) {
        this.requiredVals = requiredVals;
    }

    public abstract boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance);

    public abstract RequirementInstance deserializeInstance(JsonObject json);

    public Class<?>[] getRequiredVals() {
        return requiredVals;
    }

    public Class<?>[] getOptionalVals() {
        return optionalVals;
    }
}
