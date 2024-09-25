package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;

public class UsedAbilityRequirement extends Requirement {
    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        AbilityCore<? extends IAbility> core1 = (AbilityCore) GameRegistry.findRegistry(AbilityCore.class).getValue(new ResourceLocation(instance.getValues()[0]));
        int usedTimes = AbilityAdditionDataCapability.get(entity).getUsages(core1);
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info(instance.getValues()[0] + ":" + usedTimes + "/" + Integer.decode(instance.getValues()[1]));
        }
        return usedTimes > Integer.decode(instance.getValues()[1]);
    }
}
