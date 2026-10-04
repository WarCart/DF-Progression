package net.warcar.fruit_progression.nodes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.mixin_interfaces.INodeMixin;
import net.warcar.fruit_progression.requirements.RequirementInstance;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;

import java.util.HashMap;
import java.util.Map;

public class AbilityNodeLink {
    private static final Map<ResourceLocation, AbilityNode> RESOLVED = new HashMap<>();
    private Component localizedName;
    private ResourceLocation icon;
    private AbilityNode.NodePos location;
    private RequirementInstance visibleIf = null;
    private RequirementSetInstance requirementSet;
    private ResourceLocation[] prerequisites = {};

    public AbilityNode create() {
        AbilityNode node = new AbilityNode(this.localizedName, this.icon, this.location);
        ((INodeMixin) node).ability_progression$setRequirement(this.visibleIf);
        //TODO: merge requirement set
        return node;
    }

    public static AbilityNode resolve(ResourceLocation location) {
        if (RESOLVED.containsKey(location)) {
            return RESOLVED.get(location);
        }
        AbilityNodeLink link = DevilFruitProgressionMod.ABILITY_TREE_READER.get(location);
        AbilityNode node = link.create();
        RESOLVED.put(location, node);
        return node;
    }

    public static AbilityNodeLink deserializeFromJson(JsonElement json, ResourceLocation location) {
        if (!json.isJsonObject()) {
            return null;
        }
        JsonObject main = json.getAsJsonObject();
        AbilityNodeLink link = new AbilityNodeLink();
        if (main.has("icon")) {
            link.icon = ResourceLocation.parse(main.get("icon").getAsString());
        } else {
            link.icon = location;
        }
        if (main.has("display_name")) {
            link.localizedName = Component.translatable(main.get("display_name").getAsString());
        } else {
            link.localizedName = Component.translatable(String.format("ability.%s.%s", location.getNamespace(), location.getPath()));
        }
        JsonObject pos = main.getAsJsonObject("pos");
        link.location = new AbilityNode.NodePos(pos.get("x").getAsFloat(), pos.get("y").getAsFloat());
        if (main.has("prerequisites")) {
            JsonArray prereqs = main.getAsJsonArray("prerequisites");
            link.prerequisites = new ResourceLocation[prereqs.size()];
            for (int i = 0; i < prereqs.size(); i++) {
                link.prerequisites[i] = ResourceLocation.parse(prereqs.get(i).getAsString());
            }
        }
        if (main.has("visible_if")) {
            link.visibleIf = RequirementInstance.deserialize(main.get("visible_if"));
        }
        link.requirementSet = RequirementSetInstance.getRequirementSetInstance(main.get("requirements"), location);
        //TODO: on_unlocked
        return link;
    }
}
