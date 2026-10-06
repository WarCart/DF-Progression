package net.warcar.fruit_progression.nodes;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;

public abstract class NodeUnlock {
    private final Class<?>[] requiredVals;
    protected Class<?>[] optionalVals = new Class[0];

    protected NodeUnlock(Class<?>... requiredVals) {
        this.requiredVals = requiredVals;
    }

    public abstract void onUnlock(LivingEntity entity, NodeUnlockInstance instance);

    public abstract void onLock(LivingEntity entity, NodeUnlockInstance instance);

    public abstract NodeUnlockInstance deserializeInstance(JsonObject json);

    public Class<?>[] getRequiredVals() {
        return requiredVals;
    }

    public Class<?>[] getOptionalVals() {
        return optionalVals;
    }
}
