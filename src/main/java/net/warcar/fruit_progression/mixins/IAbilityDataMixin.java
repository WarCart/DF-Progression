package net.warcar.fruit_progression.mixins;

import net.warcar.fruit_progression.data.mixin_interfaces.IAbilityDataExtended;
import org.spongepowered.asm.mixin.Mixin;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

@Mixin(IAbilityData.class)
public interface IAbilityDataMixin extends IAbilityDataExtended {
}
