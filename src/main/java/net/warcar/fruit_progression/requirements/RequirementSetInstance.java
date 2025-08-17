package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModRegistry;
import net.warcar.fruit_progression.new_data_reader.AbilityDataReader;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

import java.util.ArrayList;
import java.util.List;

public class RequirementSetInstance {
    public final List<List<RequirementInstance>> reqs;
    public final String name;

    public RequirementSetInstance(List<List<RequirementInstance>> reqs, String name) {
        this.reqs = reqs;
        this.name = name;
    }

    public boolean isFulfilled(LivingEntity player, AbilityCore<?> core) {
        List<List<RequirementInstance>> reqsSquared = this.reqs;
        boolean stoppedOuter = false;
        if (reqsSquared != null) {
            for (List<RequirementInstance> requirements : reqsSquared) {
                boolean stoppedInner = true;
                for (RequirementInstance requirement : requirements) {
                    boolean reqOutput = requirement.getCore().requirementMet(player, core, requirement);
                    if (requirement.isInverted()) {
                        reqOutput = !reqOutput;
                    }
                    stoppedInner = stoppedInner && reqOutput;
                }
                stoppedOuter = stoppedOuter || stoppedInner;
            }
        }
        return stoppedOuter;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);
        builder.append(", [");
        for (List<RequirementInstance> requirements : this.reqs) {
            builder.append("[");
            for (RequirementInstance requirement : requirements) {
                builder.append(requirement);
                builder.append(", ");
            }
            builder.append("], ");
        }
        builder.append("]");
        return builder.toString();
    }

    public static RequirementSetInstance getRequirementSetInstance(JsonElement jsonElement, ResourceLocation location) {
        List<List<RequirementInstance>> list = getListOfLists(jsonElement, location);
        boolean debug = jsonElement.getAsJsonObject().has("debug");
        RequirementSetInstance reqs = new RequirementSetInstance(list, location.toString());
        if (debug) {
            DevilFruitProgressionMod.LOGGER.debug(reqs);
        }
        return reqs;
    }

    public static List<List<RequirementInstance>> getListOfLists(JsonElement jsonElement, ResourceLocation location) {
        List<List<RequirementInstance>> list = new ArrayList<>();
        for (JsonElement jsonArr : jsonElement.getAsJsonObject().get("requirements").getAsJsonArray()) {
            List<RequirementInstance> innerList = new ArrayList<>();
            for (JsonElement json : jsonArr.getAsJsonArray()) {
                try {
                    JsonObject object = json.getAsJsonObject();
                    String name = object.get("name").getAsString();
                    JsonObject args;
                    if (object.has("args")) {
                        args = object.get("args").getAsJsonObject();
                    } else {
                        args = new JsonObject();
                    }
                    innerList.add(ModRegistry.REQUIREMENTS.getValue(new ResourceLocation(name)).deserializeInstance(args));
                } catch (Exception e) {
                    DevilFruitProgressionMod.LOGGER.warn("Error while trying to process ability data {}", location);
                    if (e instanceof NullPointerException) {
                        DevilFruitProgressionMod.LOGGER.warn("'{}' requirement doesn't exist", json.getAsJsonObject().get("name").getAsString());
                    } else {
                        e.printStackTrace();
                    }
                }
                if (!innerList.isEmpty()) {
                    list.add(innerList);
                }
            }
        }
        return list;
    }
}
