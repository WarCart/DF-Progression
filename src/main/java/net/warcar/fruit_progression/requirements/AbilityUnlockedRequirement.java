package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;

public class AbilityUnlockedRequirement extends Requirement {
    public AbilityUnlockedRequirement() {
        super(AbilityCore.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        return AbilityDataCapability.get(entity).hasUnlockedAbility((AbilityCore) GameRegistry.findRegistry(AbilityCore.class).getValue(new ResourceLocation(instance.getValues()[0])));
    }
}
