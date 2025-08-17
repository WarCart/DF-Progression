package net.warcar.fruit_progression.mixins;

import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.entity.abilities_addition.IContinuousComponentMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AbilityComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AbilityComponentKey;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusManager;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

@Mixin(ContinuousComponent.class)
public class ContinuousComponentMixin extends AbilityComponent<IAbility> implements IContinuousComponentMixin {
    private static final UUID BONUS_MANAGER_UUID = UUID.fromString("4e6f0341-5c10-499b-bc7b-55c1e6eb4240");
    private final BonusManager bonusManager = new BonusManager(BONUS_MANAGER_UUID) {
        @Override
        public float applyBonus(float value) {
            for(Map.Entry<UUID, BonusValue> bonus : this.getBonuses()) {
                switch (bonus.getValue().getType()) {
                    case ADD:
                        value += bonus.getValue().getValue();
                        break;
                    case MUL:
                        value *= bonus.getValue().getValue();
                }
            }
            return value;
        }
    };
    private ContinuousComponentMixin(AbilityComponentKey<? extends AbilityComponent> key, IAbility ability) {
        super(key, ability);
    }

    @Inject(method = "<init>(Lxyz/pixelatedw/mineminenomi/api/abilities/IAbility;Z)V", at = @At("TAIL"), remap = false)
    private void onInit(IAbility ability, boolean isParallel, CallbackInfo ci) {
        this.addBonusManager(bonusManager);
    }

    @Inject(method = "<init>(Lxyz/pixelatedw/mineminenomi/api/abilities/IAbility;Ljava/util/function/Predicate;)V", at = @At("TAIL"), remap = false)
    private void onInit(IAbility ability, Predicate<ContinuousComponent> isParallelTest, CallbackInfo ci) {
        this.addBonusManager(bonusManager);
    }

    @ModifyVariable(method = "startContinuity(Lnet/minecraft/entity/LivingEntity;F)V",
            at = @At(value = "INVOKE", target = "Lxyz/pixelatedw/mineminenomi/api/abilities/components/ContinuousComponent;ensureIsRegistered()V"),
            remap = false, argsOnly = true)
    private float modifyThreshold(float val) {
        return bonusManager.applyBonus(val);
    }

    @Override
    public BonusManager getBonusManager() {
        return this.bonusManager;
    }
}
