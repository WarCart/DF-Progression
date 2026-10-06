package net.warcar.fruit_progression.mixins.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.mixin_interfaces.INodeMixin;
import net.warcar.fruit_progression.network.CUnlockAbilityNodePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.ui.screens.AbilityTreeScreen;

import java.util.Set;

@Mixin(AbilityTreeScreen.class)
public abstract class AbilityTreeMixin extends Screen {
    @Shadow
    private IAbilityData abilityProps;
    @Shadow
    private LocalPlayer player;
    @Shadow
    private AbilityNode clickedNode;
    @Unique
    private Set<AbilityNode> ability_progression$cachedNodes = null;

    private AbilityTreeMixin() {
        super(Component.empty());
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lxyz/pixelatedw/mineminenomi/data/entity/ability/IAbilityData;getNodes()Ljava/util/Set;", remap = false))
    private Set<AbilityNode> trueNodes(IAbilityData props) {
        if (this.ability_progression$cachedNodes == null) {
            Set<AbilityNode> nodes = this.abilityProps.getNodes();
            nodes.removeIf(node -> !((INodeMixin) node).ability_progression$isVisible(this.player));
            this.ability_progression$cachedNodes = nodes;
            return nodes;
        }
        else {
            return this.ability_progression$cachedNodes;
        }
    }

    @Inject(method = "mouseReleased", at = @At(value = "INVOKE", target = "Lxyz/pixelatedw/mineminenomi/data/entity/ability/IAbilityData;getCoreIndexPair(Lxyz/pixelatedw/mineminenomi/api/abilities/nodes/AbilityNode;)Lorg/apache/commons/lang3/tuple/Pair;", remap = false))
    private void sendCustomPacket(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        ResourceLocation location = ((INodeMixin)this.clickedNode).ability_progression$getResourceLocation();
        if (location == null) {
            DevilFruitProgressionMod.LOGGER.warn("Node id is null!");
        } else {
            DevilFruitProgressionMod.INSTANCE.sendToServer(new CUnlockAbilityNodePacket(location));
        }
    }
}
