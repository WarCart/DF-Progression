package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

import java.util.Optional;

public class AbilityUnlockedRequirement extends Requirement {
    public AbilityUnlockedRequirement() {
        super(AbilityCore.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IAbilityData> data = AbilityCapability.get(entity);
        if (data.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no abilities, can't check if unlocked", entity);
            }
            return false;
        }
        return data.get().hasUnlockedAbility(WyRegistry.ABILITIES.get().getValue(ResourceLocation.parse(instance.getValues()[0])));
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("ability").getAsString());
        return instance;
    }
}
