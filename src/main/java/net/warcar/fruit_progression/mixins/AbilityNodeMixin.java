package net.warcar.fruit_progression.mixins;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.data.mixin_interfaces.INodeMixin;
import net.warcar.fruit_progression.requirements.RequirementInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;

@Mixin(AbilityNode.class)
public class AbilityNodeMixin implements INodeMixin {
    @Unique
    private RequirementInstance ability_progression$canSee = null;
    @Unique
    private ResourceLocation ability_progression$id = null;

    @Override
    public boolean ability_progression$isVisible(LivingEntity entity) {
        if (ability_progression$canSee == null) {
            return true;
        }
        return ability_progression$canSee.isFulfilled(entity, null);
    }

    @Override
    public void ability_progression$setRequirement(RequirementInstance instance) {
        this.ability_progression$canSee = instance;
    }

    @Override
    public void ability_progression$setResourceLocation(ResourceLocation location) {
        this.ability_progression$id = location;
    }

    @Override
    public ResourceLocation ability_progression$getResourceLocation() {
        return this.ability_progression$id;
    }
}
