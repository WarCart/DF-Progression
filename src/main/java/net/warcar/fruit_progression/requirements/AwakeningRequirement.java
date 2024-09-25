package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;

public class AwakeningRequirement extends Requirement {
    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info(DevilFruitCapability.get(entity).hasAwakenedFruit());
        }
        return DevilFruitCapability.get(entity).hasAwakenedFruit();
    }
}
