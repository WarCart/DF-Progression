package net.warcar.fruit_progression.mixins;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.warcar.fruit_progression.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.data.conditions.RandomizedFruitsCondition;

@Mixin(RandomizedFruitsCondition.class)
public abstract class RandomizedFruitMixin implements LootItemCondition {
    @Inject(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z", at = @At("RETURN"), remap = false, cancellable = true)
    public void test(LootContext context, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.isEncyclopediaOverhauled()) {
            cir.setReturnValue(true);
        }
    }
}
