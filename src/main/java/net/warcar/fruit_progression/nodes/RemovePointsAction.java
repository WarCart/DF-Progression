package net.warcar.fruit_progression.nodes;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.enums.TrainingPointType;
import xyz.pixelatedw.mineminenomi.config.GeneralConfig;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;

public class RemovePointsAction extends NodeUnlock {
    @Override
    public void onUnlock(LivingEntity entity, NodeUnlockInstance instance) {
        int amount = (int) Float.parseFloat(instance.getValues()[0]);
        TrainingPointType type = TrainingPointType.valueOf(instance.getValues()[1]);
        EntityStatsCapability.getLazy(entity).ifPresent((props) -> {
            props.alterTrainingPoints(type, -amount);
            props.alterSpentTrainingPoints(amount);
        });
    }

    @Override
    public void onLock(LivingEntity entity, NodeUnlockInstance instance) {
        float amount = Float.parseFloat(instance.getValues()[0]);
        TrainingPointType type = TrainingPointType.valueOf(instance.getValues()[1]);
        EntityStatsCapability.getLazy(entity).ifPresent((props) -> {
            float keep = (float)(GeneralConfig.TRAINING_POINTS_KEEP_PERCENTAGE.get() / 100.0);
            props.alterTrainingPoints(type, Math.round(amount * keep));
            props.alterSpentTrainingPoints((int) amount);
        });
    }

    @Override
    public NodeUnlockInstance deserializeInstance(JsonObject json) {
        NodeUnlockInstance instance = new NodeUnlockInstance(this);
        instance.setValues(json.get("points").getAsString(), json.get("type").getAsString());
        return instance;
    }
}
