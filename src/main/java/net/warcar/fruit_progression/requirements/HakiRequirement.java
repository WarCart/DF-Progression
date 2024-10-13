package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.enums.HakiType;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.IHakiData;

public class HakiRequirement extends Requirement {
    public HakiRequirement() {
        super(Float.TYPE, HakiType.class);
        this.optionalVals = new Class[]{Boolean.TYPE};
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        float target = Float.parseFloat(instance.getValues()[0]);
        HakiType type = HakiType.valueOf(instance.getValues()[1]);
        IHakiData data = HakiDataCapability.get(entity);
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
            if (instance.isDebug()) DevilFruitProgressionMod.LOGGER.info(type + ":" + haki * 2 / data.getMaxHakiExp() + "/" + target);
            return haki * 2 / data.getMaxHakiExp() >= target;
        }
        if (instance.isDebug()) DevilFruitProgressionMod.LOGGER.info(type + ":" + haki + "/" + target);
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
}
