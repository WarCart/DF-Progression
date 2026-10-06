package net.warcar.fruit_progression.mixins;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.init.ModDataReaders;
import net.warcar.fruit_progression.nodes.AbilityNodeLink;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;

@Mixin(AbilityCore.class)
public abstract class AbilityCoreMixin {
    @Shadow
    @Nullable
    public abstract ResourceLocation getRegistryKey();

    @Shadow
    public abstract int getNodeCount();

    @Inject(method = "getNode(Lnet/minecraft/world/entity/LivingEntity;I)Lxyz/pixelatedw/mineminenomi/api/abilities/nodes/AbilityNode;", at = @At("HEAD"), remap = false, cancellable = true)
    private void newNode(LivingEntity entity, int index, CallbackInfoReturnable<AbilityNode> cir) {
        if (ModDataReaders.ABILITY_TREE_READER.contains(this.getRegistryKey())) {
            cir.setReturnValue(AbilityNodeLink.resolve(this.getRegistryKey()));
            return;
        }
        if (this.getNodeCount() > 1) {
            ResourceLocation withPostfix = ResourceLocation.fromNamespaceAndPath(this.getRegistryKey().getNamespace(), this.getRegistryKey().getPath() + "/" + index);
            if (ModDataReaders.ABILITY_TREE_READER.contains(withPostfix)) {
                cir.setReturnValue(AbilityNodeLink.resolve(withPostfix));
            }
        }
    }
}
