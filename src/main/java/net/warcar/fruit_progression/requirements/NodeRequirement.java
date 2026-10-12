package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.mixin_interfaces.IAbilityDataExtended;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

import java.util.Optional;

public class NodeRequirement extends Requirement {
    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IAbilityData> dataOptional = AbilityCapability.get(entity);
        if (dataOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no abilities, can't check if node is unlocked", entity);
            }
            return false;
        }
        IAbilityDataExtended data = (IAbilityDataExtended) dataOptional.get();
        AbilityNode node = data.ability_progression$getSerialNode(ResourceLocation.parse(instance.getValues()[0]));
        if (node == null) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Node doesn't exist for {}", entity);
            }
            return false;
        }
        return node.isUnlocked(entity);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("node").getAsString());
        return instance;
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        return Component.empty();
    }
}
