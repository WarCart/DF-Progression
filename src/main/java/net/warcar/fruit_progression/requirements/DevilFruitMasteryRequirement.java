package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.data.entity.abilities_addition.IAbilityAdditionData;
import net.warcar.fruit_progression.init.ModTexts;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

import java.util.Optional;

public class DevilFruitMasteryRequirement extends Requirement {
    public DevilFruitMasteryRequirement() {
        super(Double.TYPE);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IAbilityAdditionData> statsOptional = AbilityAdditionDataCapability.get(entity);
        if (statsOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no additional ability data, can't check devil fruit mastery", entity);
            }
            return false;
        }
        return statsOptional.get().getDevilFruitMastery() >= Double.parseDouble(instance.getValues()[0]);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(String.valueOf(json.get("mastery").getAsDouble()));
        return instance;
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        return Component.translatable(ModTexts.NEEDS_DF_MASTERY, instance.getValues()[0]);
    }
}
