package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.warcar.fruit_progression.init.ModRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

import java.util.Arrays;

public abstract class Requirement extends ForgeRegistryEntry<Requirement> {
    private final Class<?>[] requiredVals;
    protected Class<?>[] optionalVals = new Class[0];

    protected Requirement(Class<?>... requiredVals) {
        this.requiredVals = requiredVals;
    }

    public abstract boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance);

    public Class<?>[] getRequiredVals() {
        return requiredVals;
    }

    public Class<?>[] getOptionalVals() {
        return optionalVals;
    }
}
