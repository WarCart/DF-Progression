package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;

public class HaoshokuBornRequirement extends Requirement {
    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        if (entity instanceof PlayerEntity) {
            return HakiHelper.isHaoshokuBorn((PlayerEntity) entity) || CommonConfig.INSTANCE.getHaoshokuUnlockLogic() == CommonConfig.HaoshokuUnlockLogic.EXPERIENCE;
        }
        return false;
    }
}
