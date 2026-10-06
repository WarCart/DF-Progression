package net.warcar.fruit_progression.nodes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModRegistries;
import net.warcar.fruit_progression.requirements.RequirementInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.actions.NodeUnlockAction;

import java.util.Arrays;

public class NodeUnlockInstance {
    private NodeUnlock core;
    private String[] args = {};
    private boolean debug;
    private RequirementInstance onlyIf;

    public NodeUnlockInstance(NodeUnlock core) {
        this.core = core;
    }

    public String[] getValues() {
        return args;
    }

    public NodeUnlock getCore() {
        return core;
    }

    public void setValues(String... args) {
        this.args = args;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void onUnlocked(LivingEntity player) {
        if (onlyIf.isFulfilled(player, null))
            this.core.onUnlock(player, this);
    }

    public void onLocked(LivingEntity player) {
        if (onlyIf.isFulfilled(player, null))
            this.core.onLock(player, this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.debug) {
            builder.append("D");
        }
        builder.append(ModRegistries.ACTIONS.get().getKey(core));
        builder.append(", args: ");
        builder.append(Arrays.toString(args));
        builder.append(", onlyIf: ");
        builder.append(onlyIf.toString());
        return builder.toString();
    }

    public static NodeUnlockInstance deserialize(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        String name = object.get("action").getAsString();
        boolean debug = false;
        if (name.startsWith("D")) {
            debug = true;
            name = name.substring(1);
        }
        JsonObject args;
        if (object.has("args")) {
            args = object.getAsJsonObject("args");
        } else {
            args = new JsonObject();
        }
        RequirementInstance onlyIf;
        if (object.has("onlyIf")) {
            onlyIf = RequirementInstance.deserialize(object.getAsJsonObject("onlyIf"));
        } else {
            onlyIf = RequirementInstance.ALWAYS_TRUE;
        }
        NodeUnlock value = ModRegistries.ACTIONS.get().getValue(ResourceLocation.parse(name));
        if (value == null) {
            DevilFruitProgressionMod.LOGGER.warn("'{}' unlock action doesn't exist", name);
            return null;
        }
        NodeUnlockInstance instance = value.deserializeInstance(args);
        instance.setDebug(debug);
        instance.onlyIf = onlyIf;
        return instance;
    }

    public NodeUnlockAction convert() {
        return new NodeUnlockAction() {
            @Override
            public void onUnlock(LivingEntity livingEntity) {
                NodeUnlockInstance.this.onUnlocked(livingEntity);
            }

            @Override
            public void onLock(LivingEntity livingEntity) {
                NodeUnlockInstance.this.onLocked(livingEntity);
            }
        };
    }
}
