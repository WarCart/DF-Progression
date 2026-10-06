package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

public class AlwaysTrueRequirement extends Requirement {
    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        if (instance.isDebug()) {
            if (instance.isInverted()) {
                DevilFruitProgressionMod.LOGGER.info("NEVER! >:(");
            } else {
                DevilFruitProgressionMod.LOGGER.info("ALWAYS! :)");
            }
        }
        return true;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        return new RequirementInstance(this);
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        return Component.empty();
    }
}
