package net.warcar.fruit_progression.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;

@Mixin(CommonConfig.class)
public class UltrastrongMixin {
    @Inject(method = "getDorikiLimit", at = @At("RETURN"), cancellable = true, remap = false)
    private void ultraDoriki(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() * 100);
    }

    @Inject(method = "getHakiExpLimit", at = @At("RETURN"), cancellable = true, remap = false)
    private void ultraHaki(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() * 100);
    }
}
