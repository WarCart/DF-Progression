package net.warcar.fruit_progression.models;

import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.abilities.IMorphAbility;

import net.minecraft.entity.LivingEntity;

public interface IMultiModelMorph extends IMorphAbility {
	default MorphInfo getTransformation() {
		return null;
	}
	MorphInfo getTransformation(LivingEntity target);
}
