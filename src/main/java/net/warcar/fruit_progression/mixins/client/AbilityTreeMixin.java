package net.warcar.fruit_progression.mixins.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.warcar.fruit_progression.data.mixin_interfaces.INodeMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.ui.screens.AbilityTreeScreen;

import java.util.Set;

@Mixin(value = AbilityTreeScreen.class, remap = false)
public abstract class AbilityTreeMixin extends Screen {
    @Shadow
    private IAbilityData abilityProps;
    @Shadow
    private LocalPlayer player;
    @Unique
    private Set<AbilityNode> ability_progression$cachedNodes = null;

    private AbilityTreeMixin() {
        super(Component.empty());
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lxyz/pixelatedw/mineminenomi/data/entity/ability/IAbilityData;getNodes()Ljava/util/Set;"))
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
}
