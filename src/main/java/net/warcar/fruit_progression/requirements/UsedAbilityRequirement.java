package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;

public class UsedAbilityRequirement extends Requirement {
    public UsedAbilityRequirement() {
        super(AbilityCore.class, Integer.TYPE);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        AbilityCore<? extends IAbility> core1 = WyRegistry.ABILITIES.get().getValue(ResourceLocation.parse(instance.getValues()[0]));
        int usedTimes = AbilityAdditionDataCapability.get(entity).getUsages(core1);
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info(instance.getValues()[0] + ":" + usedTimes + "/" + Integer.decode(instance.getValues()[1]));
        }
        return usedTimes > Integer.decode(instance.getValues()[1]);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("abilityID").getAsString(), String.valueOf(json.get("timesUsed").getAsInt()));
        return instance;
    }
}
