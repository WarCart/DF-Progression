package net.warcar.fruit_progression.requirements;

import net.minecraft.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

public class FruitRequirement extends Requirement {
    public FruitRequirement() {
        super(AkumaNoMiItem.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        IDevilFruit fruit = DevilFruitCapability.get(entity);
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info(fruit.getDevilFruit().get() + "/" + instance.getValues()[0]);
        }
        boolean sameFruit = fruit.getDevilFruit().isPresent() && fruit.getDevilFruit().get().toString().equals(instance.getValues()[0]);
        if (instance.getValues()[0].equals("mineminenomi:yami_yami_no_mi")) {
            return fruit.hasYamiPower() || sameFruit;
        }
        return sameFruit;
    }
}
