package net.warcar.fruit_progression.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.DevilFruitHelper;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.entities.zoan.YomiMorphInfo;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

@Mixin(AbilityHelper.class)
public abstract class AbilityHelperMixin {
    @Inject(remap = false, method = "validateDevilFruitMoves", at = @At(value = "HEAD"), cancellable = true)
    private static void fruitMoves(PlayerEntity player, CallbackInfo info) {
        info.cancel();
        if (CommonConfig.INSTANCE.isAbilityFraudChecksEnabled()) {
            IDevilFruit devilFruitProps = DevilFruitCapability.get(player);
            IAbilityData abilityProps = AbilityDataCapability.get(player);
            if (!devilFruitProps.hasDevilFruit(ModAbilities.YOMI_YOMI_NO_MI) || YomiMorphInfo.INSTANCE.isActive(player)) {
                ItemStack df = DevilFruitHelper.getDevilFruitItemStack(devilFruitProps.getDevilFruit());
                if (df != null && !df.isEmpty()) {
                    int var6;
                    if (devilFruitProps.hasYamiPower()) {
                        ItemStack yami = DevilFruitHelper.getDevilFruitItemStack("yami_yami");
                        AbilityCore[] var5 = ((AkumaNoMiItem)yami.getItem()).getAbilities();
                        var6 = var5.length;

                        for(int var7 = 0; var7 < var6; ++var7) {
                            AbilityCore a = var5[var7];
                            if (AbilityHelper.verifyIfAbilityIsBanned(a)) {
                                abilityProps.removeUnlockedAbility(a);
                            } else if (!AbilityHelper.verifyIfAbilityIsBanned(a) && !abilityProps.hasUnlockedAbility(a)) {
                                abilityProps.addUnlockedAbility(a);
                            }
                        }
                    }

                    AbilityCore[] var9 = ((AkumaNoMiItem)df.getItem()).getAbilities();
                    int var10 = var9.length;

                    for(var6 = 0; var6 < var10; ++var6) {
                        AbilityCore a = var9[var6];
                        if (AbilityHelper.verifyIfAbilityIsBanned(a) || !a.canUnlock(player)) {
                            abilityProps.removeUnlockedAbility(a);
                        } else if (!AbilityHelper.verifyIfAbilityIsBanned(a) && !abilityProps.hasUnlockedAbility(a) && a.canUnlock(player)) {
                            abilityProps.addUnlockedAbility(a);
                        }
                    }
                } else {
                    abilityProps.clearUnlockedAbilities(AbilityHelper.getAbilityFromCategoryPredicate(AbilityCategory.DEVIL_FRUITS));
                }

            }
        }
    }
}
