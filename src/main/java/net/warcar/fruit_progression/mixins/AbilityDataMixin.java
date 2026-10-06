package net.warcar.fruit_progression.mixins;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.nodes.AbilityNodeLink;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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
public abstract class AbilityDataMixin implements IAbilityData {
    @Unique
    private final Map<ResourceLocation, AbilityNode> ability_progression$serialNodes = new HashMap<>();

    @Shadow
    private LivingEntity owner;

    @ModifyVariable(method = "deserializeNBT(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("STORE"), remap = false)
    private ListTag modifyNodesTag(ListTag nodesTag) {
        for (int i = 0; i < nodesTag.size(); ++i) {
            CompoundTag nodeTag = nodesTag.getCompound(i);

            String coreId = nodeTag.getString("id");

            int index = nodeTag.getInt("index");

            AbilityNode node;

            ResourceLocation location = ResourceLocation.parse(coreId);

            if (DevilFruitProgressionMod.ABILITY_TREE_READER.contains(location)) {
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

                this.addNode(core, index, node);
            }
        }

        for (ResourceLocation loc : DevilFruitProgressionMod.ABILITY_TREE_READER.keys()) {
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

    @Inject(method = "getNodes", at = @At("RETURN"), remap = false, cancellable = true)
    private void addNodes(CallbackInfoReturnable<Set<AbilityNode>> cir) {
        HashSet<AbilityNode> nodes = new HashSet<>(this.ability_progression$serialNodes.values());
        cir.setReturnValue(nodes);
    }
}
