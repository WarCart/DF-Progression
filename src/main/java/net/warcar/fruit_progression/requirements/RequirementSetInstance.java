package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModRequirements;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RequirementSetInstance {
    public static final RequirementSetInstance UNFINISHABLE = new RequirementSetInstance(new ArrayList<>(), "Unfinishable"){
        @Override
        public boolean isFulfilled(LivingEntity player, AbilityCore<?> core) {
            return false;
        }
    };
    public final List<List<RequirementInstance>> reqs;
    public final String name;

    public RequirementSetInstance(List<List<RequirementInstance>> reqs, String name) {
        this.reqs = reqs;
        this.name = name;
    }

    public boolean isFulfilled(LivingEntity player, @Nullable AbilityCore<?> core) {
        List<List<RequirementInstance>> reqsSquared = this.reqs;
        boolean stoppedOuter = false;
        if (reqsSquared != null) {
            for (List<RequirementInstance> requirements : reqsSquared) {
                boolean stoppedInner = true;
                for (RequirementInstance requirement : requirements) {
                    boolean reqOutput = requirement.isFulfilled(player, core);
                    if (!reqOutput) {
                        stoppedInner = false;
                        break;
                    }
                }
                if (stoppedInner) {
                    stoppedOuter = true;
                    break;
                }
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
        try {
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.get("requirements").isJsonPrimitive()) {
                    return ofOnly(ModRequirements.SAVED.get().simpleInstance(jsonObject.get("requirements").getAsString()), location.toString());
                }
                List<List<RequirementInstance>> list = getListOfLists(jsonObject.getAsJsonArray("requirements"));
                boolean debug = jsonObject.has("debug") && jsonObject.get("debug").getAsBoolean();
                RequirementSetInstance reqs = new RequirementSetInstance(list, location.toString());
                if (debug) {
                    DevilFruitProgressionMod.LOGGER.debug(reqs);
                }
                return reqs;
            } else if (jsonElement.isJsonArray()) {
                List<List<RequirementInstance>> list = getListOfLists(jsonElement.getAsJsonArray());
                return new RequirementSetInstance(list, location.toString());
            }
        } catch (Exception e) {
            DevilFruitProgressionMod.LOGGER.warn("Error while trying to process ability data {}", location);
            e.printStackTrace();
        }
        return UNFINISHABLE;
    }

    public static List<List<RequirementInstance>> getListOfLists(JsonArray jsonElement) {
        List<List<RequirementInstance>> list = new ArrayList<>();
        for (JsonElement jsonArr : jsonElement) {
            List<RequirementInstance> innerList = new ArrayList<>();
            for (JsonElement json : jsonArr.getAsJsonArray()) {
                RequirementInstance instance = RequirementInstance.deserialize(json);
                if (instance != null) {
                    innerList.add(instance);
                }
            }
            if (!innerList.isEmpty()) {
                list.add(innerList);
            }
        }
        return list;
    }

    public MutableComponent getTooltip() {
        MutableComponent output = Component.empty();
        for (int i = 0; i < this.reqs.size(); i++) {
            List<RequirementInstance> instances = this.reqs.get(i);
            for (int j = 0; j < instances.size(); j++) {
                RequirementInstance instance = instances.get(j);
                output.append(instance.getTooltip());
                if (j < instances.size() - 1) {
                    output.append("\n");
                }
            }
            if (i < this.reqs.size() - 1) {
                output.append("\n    Or\n");
            }
        }
        return output;
    }

    public static RequirementSetInstance ofOnly(RequirementInstance instance, String name) {
        DevilFruitProgressionMod.LOGGER.info(instance.toString());
        return new RequirementSetInstance(List.of(List.of(instance)), name);
    }
}
