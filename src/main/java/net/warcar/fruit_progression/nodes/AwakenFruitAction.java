package net.warcar.fruit_progression.nodes;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;

public class AwakenFruitAction extends NodeUnlock {
    @Override
    public void onUnlock(LivingEntity entity, NodeUnlockInstance instance) {
        DevilFruitCapability.get(entity).ifPresent((props) ->
                props.setAwakenedFruit(true));
    }

    @Override
    public void onLock(LivingEntity entity, NodeUnlockInstance instance) {
        DevilFruitCapability.get(entity).ifPresent((props) ->
                props.setAwakenedFruit(false));
    }

    @Override
    public NodeUnlockInstance deserializeInstance(JsonObject json) {
        return new NodeUnlockInstance(this);
    }
}
