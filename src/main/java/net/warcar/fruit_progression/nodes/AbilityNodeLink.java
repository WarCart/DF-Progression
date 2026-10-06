package net.warcar.fruit_progression.nodes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.data.mixin_interfaces.INodeMixin;
import net.warcar.fruit_progression.init.ModDataReaders;
import net.warcar.fruit_progression.requirements.RequirementInstance;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.actions.NodeUnlockAction;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.conditions.NodeUnlockCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityNodeLink {
    private static final Map<ResourceLocation, AbilityNode> RESOLVED = new HashMap<>();
    private Component localizedName;
    private ResourceLocation icon;
    private AbilityNode.NodePos location;
    private RequirementInstance visibleIf = null;
    private RequirementSetInstance requirementSet;
    private ConditionedLink[] prerequisites = {};
    private NodeUnlockAction onUnlock;

    public AbilityNode create(LivingEntity player) {
        AbilityNode node = new AbilityNode(this.localizedName, this.icon, this.location);
        ((INodeMixin) node).ability_progression$setRequirement(this.visibleIf);
        if (this.prerequisites.length > 0) {
            node.addPrerequisites(this.resolveDependencies(player));
        }
        node.setUnlockRule(new SetCondition(this.requirementSet), this.onUnlock);
        return node;
    }

    public static AbilityNode resolve(ResourceLocation location, LivingEntity player) {
        if (RESOLVED.containsKey(location)) {
            return RESOLVED.get(location);
        }
        AbilityNodeLink link = ModDataReaders.ABILITY_TREE_READER.get(location);
        AbilityNode node;
        try {
            node = link.create(player);
            ((INodeMixin) node).ability_progression$setResourceLocation(location);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        RESOLVED.put(location, node);
        return node;
    }

    private AbilityNode[] resolveDependencies(LivingEntity player) {
        List<AbilityNode> out = new ArrayList<>();
        for (ConditionedLink prerequisite : this.prerequisites) {
            if (prerequisite.condition.isFulfilled(player, null)) {
                out.add(resolve(prerequisite.id, player));
            }
        }
        return out.toArray(AbilityNode[]::new);
    }

    public static AbilityNodeLink deserializeFromJson(JsonElement json, ResourceLocation location) {
        if (!json.isJsonObject()) {
            return null;
        }
        JsonObject main = json.getAsJsonObject();
        AbilityNodeLink link = new AbilityNodeLink();
        if (main.has("icon")) {
            link.icon = ResourceLocation.parse(main.get("icon").getAsString() + ".png");
        } else {
            link.icon = ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "textures/abilities/" + location.getPath() + ".png");
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
            link.prerequisites = new ConditionedLink[prereqs.size()];
            for (int i = 0; i < prereqs.size(); i++) {
                JsonElement element = prereqs.get(i);
                if (element.isJsonPrimitive()) {
                    link.prerequisites[i] = new ConditionedLink(ResourceLocation.parse(element.getAsString()), RequirementInstance.ALWAYS_TRUE);
                } else {
                    JsonObject prereq = element.getAsJsonObject();
                    link.prerequisites[i] = new ConditionedLink(ResourceLocation.parse(prereq.get("id").getAsString()), RequirementInstance.deserialize(prereq.get("only_if")));
                }
            }
        }
        if (main.has("visible_if")) {
            link.visibleIf = RequirementInstance.deserialize(main.get("visible_if"));
        }
        link.requirementSet = RequirementSetInstance.getRequirementSetInstance(main.get("requirements"), location);
        JsonArray onUnlock = main.getAsJsonArray("on_unlocked");
        NodeUnlockInstance[] onUnlocked = new NodeUnlockInstance[onUnlock.size()];
        for (int i = 0; i < onUnlock.size(); i++) {
            onUnlocked[i] = NodeUnlockInstance.deserialize(onUnlock.get(i));
        }
        link.onUnlock = bakeUnlocked(onUnlocked);
        return link;
    }

    private static NodeUnlockAction bakeUnlocked(NodeUnlockInstance[] onUnlocked) {
        NodeUnlockAction out = onUnlocked[0].convert();
        for (int i = 1; i < onUnlocked.length; i++) {
            out = out.andThen(onUnlocked[i].convert());
        }
        return out;
    }

    public static void clearCache() {
        RESOLVED.clear();
    }

    private static class SetCondition implements NodeUnlockCondition {
        RequirementSetInstance instance;

        public SetCondition(RequirementSetInstance instance) {
            this.instance = instance;
        }

        @Override
        public boolean test(LivingEntity livingEntity) {
            return instance.isFulfilled(livingEntity, null);
        }

        @Override
        public MutableComponent getTooltip() {
            return instance.getTooltip();
        }
    }

    public record ConditionedLink(ResourceLocation id, RequirementInstance condition) {
    }
}
