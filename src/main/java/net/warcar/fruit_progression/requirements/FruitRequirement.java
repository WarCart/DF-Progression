package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.Objects;
import java.util.Optional;

public class FruitRequirement extends Requirement {
    public FruitRequirement() {
        super(AkumaNoMiItem.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IDevilFruit> dataOptional = DevilFruitCapability.get(entity);
        if (dataOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no devil fruit data, can't check", entity);
            }
            return false;
        }
        IDevilFruit fruit = dataOptional.get();
        String df = fruit.getDevilFruit().map(Objects::toString).orElse("null");
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info("{}/{}", df, instance.getValues()[0]);
        }
        boolean sameFruit = df.equals(instance.getValues()[0]);
        if (instance.getValues()[0].equals("mineminenomi:yami_yami_no_mi")) {
            return fruit.hasYamiPower() || sameFruit;
        }
        return sameFruit;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("fruitID").getAsString());
        return instance;
    }
}
