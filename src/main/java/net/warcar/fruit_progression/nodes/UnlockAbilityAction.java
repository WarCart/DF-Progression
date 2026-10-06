package net.warcar.fruit_progression.nodes;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;

public class UnlockAbilityAction extends NodeUnlock {
    @Override
    public void onUnlock(LivingEntity entity, NodeUnlockInstance instance) {
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info("unlocking {}", instance.getValues()[0]);
        }
        AbilityHelper.checkAndUnlockAbility(entity, AbilityCore.get(ResourceLocation.parse(instance.getValues()[0])));
    }

    @Override
    public void onLock(LivingEntity entity, NodeUnlockInstance instance) {
        AbilityCapability.get(entity).ifPresent(props ->
                props.removeUnlockedAbility(AbilityCore.get(ResourceLocation.parse(instance.getValues()[0]))));
    }

    @Override
    public NodeUnlockInstance deserializeInstance(JsonObject json) {
        NodeUnlockInstance instance = new NodeUnlockInstance(this);
        instance.setValues(json.get("ability").getAsString());
        return instance;
    }
}
