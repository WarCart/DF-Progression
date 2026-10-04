package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModRegistries;

import java.util.Arrays;

public class RequirementInstance {
    private final Requirement core;
    private String[] args = {};
    private boolean inverted;
    private boolean debug;

    public RequirementInstance(Requirement core) {
        this.core = core;
    }

    public String[] getValues() {
        return args;
    }

    public Requirement getCore() {
        return core;
    }

    public void setValues(String... args) {
        this.args = args;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.debug) {
            builder.append("D");
        }
        if (this.inverted) {
            builder.append("!");
        }
        builder.append(ModRegistries.REQUIREMENTS.get().getKey(core));
        builder.append(", ");
        builder.append(Arrays.toString(args));
        return builder.toString();
    }

    static RequirementInstance deserialize(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        String name = object.get("name").getAsString();
        boolean debug = false;
        boolean inverted = false;
        if (name.startsWith("D")) {
            debug = true;
            name = name.substring(1);
        }
        if (name.startsWith("!")) {
            inverted = true;
            name = name.substring(1);
        }
        JsonObject args;
        if (object.has("args")) {
            args = object.get("args").getAsJsonObject();
        } else {
            args = new JsonObject();
        }
        Requirement value = ModRegistries.REQUIREMENTS.get().getValue(ResourceLocation.parse(name));
        if (value == null) {
            DevilFruitProgressionMod.LOGGER.warn("'{}' requirement doesn't exist", json.getAsJsonObject().get("name").getAsString());
            return null;
        }
        RequirementInstance instance = value.deserializeInstance(args);
        instance.setDebug(debug);
        instance.setInverted(inverted);
        return instance;
    }
}
