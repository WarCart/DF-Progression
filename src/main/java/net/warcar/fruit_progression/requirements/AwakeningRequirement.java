package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModTexts;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;

import java.util.Optional;

public class AwakeningRequirement extends Requirement {
    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IDevilFruit> fruitOptional = DevilFruitCapability.get(entity);
        if (fruitOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no devil fruit data, can't check awakening", entity);
            }
            return false;
        }
        boolean awakened = fruitOptional.get().hasAwakenedFruit();
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info(awakened);
        }
        return awakened;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        return new RequirementInstance(this);
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        return Component.translatable(ModTexts.NEEDS_AWAKENING);
    }
}
