package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.warcar.fruit_progression.init.ModTexts;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.config.ServerConfig;

public class HaoshokuBornRequirement extends Requirement {
    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        if (entity instanceof Player) {
            return HakiHelper.isHaoshokuBorn((Player) entity) || ServerConfig.getHaoshokuUnlockLogic() == ServerConfig.HaoshokuUnlockLogic.EXPERIENCE;
        }
        return false;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        return new RequirementInstance(this);
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        return Component.translatable(ModTexts.NEEDS_HAO_BORN);
    }
}
