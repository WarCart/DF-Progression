package net.warcar.fruit_progression.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.pixelatedw.mineminenomi.api.damagesources.TargetPredicate;
import xyz.pixelatedw.mineminenomi.api.helpers.TargetHelper;
import xyz.pixelatedw.mineminenomi.packets.client.quest.CUpdateQuestStatePacket;

import java.util.List;

@Mixin(CUpdateQuestStatePacket.class)
public class CUpdateQuestPacketMixin {
    @Redirect(method = "lambda$handle$2", at = @At(value = "INVOKE", target = "Lxyz/pixelatedw/mineminenomi/api/helpers/TargetHelper;getEntitiesInArea(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/LivingEntity;DLxyz/pixelatedw/mineminenomi/api/damagesources/TargetPredicate;Ljava/lang/Class;)Ljava/util/List;"), remap = false)
    private static <T extends Entity> List<T> removePlayer(LevelAccessor level, LivingEntity entity, double size, @Nullable TargetPredicate targetPredicate, Class<T> clz) {
        if (targetPredicate != null) {
            targetPredicate.testVanilla(TargetingConditions.forNonCombat());
        }
        return TargetHelper.getEntitiesInArea(level, null, entity.position(), size, targetPredicate, clz);
    }
}
