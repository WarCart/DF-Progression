package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
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
                    boolean reqOutput = requirement.getCore().requirementMet(player, core, requirement);
                    if (requirement.isInverted()) {
                        reqOutput = !reqOutput;
                    }
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
            List<List<RequirementInstance>> list = getListOfLists(jsonElement, location);
            boolean debug = jsonElement.getAsJsonObject().has("debug") && jsonElement.getAsJsonObject().get("debug").getAsBoolean();
            RequirementSetInstance reqs = new RequirementSetInstance(list, location.toString());
            if (debug) {
                DevilFruitProgressionMod.LOGGER.debug(reqs);
            }
            return reqs;
        } catch (Exception e) {
            e.printStackTrace();
            return UNFINISHABLE;
        }
    }

    public static List<List<RequirementInstance>> getListOfLists(JsonElement jsonElement, ResourceLocation location) {
        List<List<RequirementInstance>> list = new ArrayList<>();
        for (JsonElement jsonArr : jsonElement.getAsJsonObject().get("requirements").getAsJsonArray()) {
            List<RequirementInstance> innerList = new ArrayList<>();
            for (JsonElement json : jsonArr.getAsJsonArray()) {
                try {
                    RequirementInstance instance = RequirementInstance.deserialize(json);
                    if (instance != null) {
                        innerList.add(instance);
                    }
                } catch (Exception e) {
                    DevilFruitProgressionMod.LOGGER.warn("Error while trying to process ability data {}", location);
                    e.printStackTrace();
                }
            }
            if (!innerList.isEmpty()) {
                list.add(innerList);
            }
        }
        return list;
    }

}
