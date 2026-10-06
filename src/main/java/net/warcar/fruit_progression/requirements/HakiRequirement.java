package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModTexts;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.enums.HakiType;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.IHakiData;

import java.util.Optional;

public class HakiRequirement extends Requirement {
    public HakiRequirement() {
        super(Float.TYPE, HakiType.class);
        this.optionalVals = new Class[]{Boolean.TYPE};
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        float target = Float.parseFloat(instance.getValues()[0]);
        HakiType type = HakiType.valueOf(instance.getValues()[1]);
        Optional<IHakiData> hakiOptional = HakiCapability.get(entity);
        if (hakiOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no haki data, can't check", entity);
            }
            return false;
        }
        IHakiData data = hakiOptional.get();
        boolean percentage;
        if (instance.getValues().length > 2) {
            percentage = Boolean.parseBoolean(instance.getValues()[2]);
        } else {
            percentage = false;
        }
        float haki;
        if (type == HakiType.HAOSHOKU) haki = data.getTotalHakiExp() / 2;
        else if (type == HakiType.BUSOSHOKU) haki = data.getBusoshokuHakiExp();
        else haki = data.getKenbunshokuHakiExp();
        if (percentage) {
            if (instance.isDebug())
                DevilFruitProgressionMod.LOGGER.info("{}:{}/{}", type, haki * 2 / data.getMaxHakiExp(), target);
            return haki * 2 / data.getMaxHakiExp() >= target;
        }
        if (instance.isDebug()) DevilFruitProgressionMod.LOGGER.info("{}:{}/{}", type, haki, target);
        return haki >= target;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        String[] args = {json.get("hakiXP").getAsString(), json.get("hakiType").getAsString()};
        if (json.has("percentage")) {
            args = new String[]{json.get("hakiXP").getAsString(), json.get("hakiType").getAsString(), Boolean.toString(json.get("percentage").getAsBoolean())};
        }
        instance.setValues(args);
        return instance;
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        HakiType type = HakiType.valueOf(instance.getValues()[1]);
        String key = switch (type) {
            case BUSOSHOKU -> ModTexts.NEEDS_BUSO_HAKIXP;
            case KENBUNSHOKU -> ModTexts.NEEDS_KENB_HAKIXP;
            case HAOSHOKU -> ModTexts.NEEDS_TOTAL_HAKIXP;
        };
        return Component.translatable(key, instance.getValues()[0]);
    }
}
