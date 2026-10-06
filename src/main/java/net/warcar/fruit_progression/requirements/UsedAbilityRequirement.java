package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.data.entity.abilities_addition.IAbilityAdditionData;
import net.warcar.fruit_progression.init.ModTexts;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;

import java.util.Optional;

public class UsedAbilityRequirement extends Requirement {
    public UsedAbilityRequirement() {
        super(AbilityCore.class, Integer.TYPE);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        AbilityCore<? extends IAbility> core1 = WyRegistry.ABILITIES.get().getValue(ResourceLocation.parse(instance.getValues()[0]));
        Optional<IAbilityAdditionData> dataOptional = AbilityAdditionDataCapability.get(entity);
        if (dataOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no ability data, can't check uses", entity);
            }
            return false;
        }
        int usedTimes = dataOptional.get().getUsages(core1);
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info("{}:{}/{}", instance.getValues()[0], usedTimes, Integer.decode(instance.getValues()[1]));
        }
        return usedTimes > Integer.decode(instance.getValues()[1]);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("abilityID").getAsString(), String.valueOf(json.get("timesUsed").getAsInt()));
        return instance;
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        AbilityCore<?> core = AbilityCore.get(ResourceLocation.parse(instance.getValues()[0]));
        String abl;
        if (core == null) {
            abl = instance.getValues()[0];
        } else {
            abl = core.getLocalizedName().getString();
        }
        return Component.translatable(ModTexts.NEEDS_USED_ABILITY, abl, instance.getValues()[1]);
    }
}
