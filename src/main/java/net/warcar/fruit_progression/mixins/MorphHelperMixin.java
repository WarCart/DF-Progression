package net.warcar.fruit_progression.mixins;

import net.minecraft.entity.LivingEntity;
import net.warcar.fruit_progression.models.IMultiModelMorph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.helpers.MorphHelper;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

@Mixin(MorphHelper.class)
public abstract class MorphHelperMixin {
	@Inject(method = "getZoanInfo", at = {@At(value = "RETURN")}, cancellable = true, remap = false)
	private static void getZoanInfo(LivingEntity entity, CallbackInfoReturnable<MorphInfo> info) {
		IAbilityData abilityProps = AbilityDataCapability.get(entity);
		for (Ability ability : abilityProps.getUnlockedAbilities()) {
			if (ability instanceof IMultiModelMorph) {
				IMultiModelMorph morphAbility = (IMultiModelMorph) ability;
				if (morphAbility.isTransformationActive(entity)) {
					info.setReturnValue(morphAbility.getTransformation(entity));
					return;
				}
			}
		}
	}
}
