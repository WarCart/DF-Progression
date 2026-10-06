package net.warcar.fruit_progression.mixins;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.warcar.fruit_progression.data.mixin_interfaces.IAbilityDataExtended;
import net.warcar.fruit_progression.data.mixin_interfaces.INodeMixin;
import net.warcar.fruit_progression.init.ModDataReaders;
import net.warcar.fruit_progression.nodes.AbilityNodeLink;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataBase;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(AbilityDataBase.class)
public abstract class AbilityDataMixin implements IAbilityData, IAbilityDataExtended {

    @Unique
    private final Map<ResourceLocation, AbilityNode> ability_progression$serialNodes = new HashMap<>();

    @Shadow
    private LivingEntity owner;

    @Shadow
    @Final
    private Map<Pair<AbilityCore<? extends IAbility>, Integer>, AbilityNode> nodes;

    @ModifyVariable(method = "deserializeNBT(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "STORE"), remap = false, name = "nodesTag")
    private ListTag modifyNodesTag(ListTag nodesTag) {
        for (int i = 0; i < nodesTag.size(); ++i) {
            CompoundTag nodeTag = nodesTag.getCompound(i);

            String coreId = nodeTag.getString("id");

            int index = nodeTag.getInt("index");

            AbilityNode node;

            ResourceLocation location = ResourceLocation.parse(coreId);

            if (ModDataReaders.ABILITY_TREE_READER.contains(location)) {
                node = AbilityNodeLink.resolve(location);

                if (node == null) {
                    continue;
                }

                node.load(nodeTag);

                this.ability_progression$serialNodes.put(location, node);
            } else {
                AbilityCore<? extends IAbility> core = AbilityCore.get(location);

                if (core == null) {
                    continue;
                }

                node = core.getNode(this.owner, index);

                if (node == null) {
                    continue;
                }

                node.load(nodeTag);

                ResourceLocation coreLoc;
                if (core.getNodeCount() == 1) {
                    coreLoc = core.getRegistryKey();
                } else {
                    coreLoc = ResourceLocation.fromNamespaceAndPath(core.getRegistryKey().getNamespace(), core.getRegistryKey().getPath() + "/" + index);
                }
                ((INodeMixin) node).ability_progression$setResourceLocation(coreLoc);
                this.ability_progression$serialNodes.put(coreLoc, node);
                this.addNode(core, index, node);
            }
        }

        for (ResourceLocation loc : ModDataReaders.ABILITY_TREE_READER.keys()) {
            if (this.ability_progression$serialNodes.containsKey(loc)) {
                continue;
            }

            AbilityNode node = AbilityNodeLink.resolve(loc);

            if (node == null) {
                continue;
            }

            this.ability_progression$serialNodes.put(loc, node);
        }

        for (AbilityCore<? extends IAbility> core : WyRegistry.ABILITIES.get().getValues()) {
            for (int i = 0; i < core.getNodeCount(); ++i) {
                ResourceLocation location;
                if (core.getNodeCount() == 1) {
                    location = core.getRegistryKey();
                } else {
                    location = ResourceLocation.fromNamespaceAndPath(core.getRegistryKey().getNamespace(), core.getRegistryKey().getPath() + "/" + i);
                }
                if (this.ability_progression$serialNodes.containsKey(location)) {
                    continue;
                }

                AbilityNode node = core.getNode(this.owner, i);

                if (node == null) {
                    continue;
                }

                this.addNode(core, i, node);
                this.ability_progression$serialNodes.put(location, node);
            }
        }
        return new ListTag();
    }

    @Inject(method = "deserializeNBT(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"), remap = false)
    private void undoNodes(CompoundTag props, CallbackInfo ci) {
        //this.nodes.clear();
    }

    @Inject(method = "serializeNBT()Lnet/minecraft/nbt/CompoundTag;", at = @At("RETURN"), remap = false, cancellable = true)
    private void saveNodes(CallbackInfoReturnable<CompoundTag> cir) {
        if (this.owner instanceof Player) {
            CompoundTag props = cir.getReturnValue();
            ListTag nodesTag = new ListTag();

            for (Map.Entry<ResourceLocation, AbilityNode> entry : this.ability_progression$serialNodes.entrySet()) {
                AbilityNode node = entry.getValue();

                if (node == null) {
                    continue;
                }

                Pair<AbilityCore<? extends IAbility>, Integer> pair = this.getCoreIndexPair(node);
                int index;
                if (pair == null) {
                    index = 0;
                } else {
                    index = pair.getValue();
                }

                CompoundTag nodeTag = node.save();

                nodeTag.putString("id", entry.getKey().toString());
                nodeTag.putInt("index", index);

                nodesTag.add(nodeTag);
            }

            props.put("nodes", nodesTag);
            cir.setReturnValue(props);
        }
    }

    @Override
    public Set<AbilityNode> getNodes() {
        return new HashSet<>(this.ability_progression$serialNodes.values());
    }

    @Override
    public AbilityNode ability_progression$getSerialNode(ResourceLocation key) {
        return this.ability_progression$serialNodes.get(key);
    }
}
