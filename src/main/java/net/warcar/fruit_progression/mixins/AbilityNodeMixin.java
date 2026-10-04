package net.warcar.fruit_progression.mixins;

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
}
