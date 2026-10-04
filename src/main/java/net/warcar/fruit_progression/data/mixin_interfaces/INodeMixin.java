package net.warcar.fruit_progression.data.mixin_interfaces;

import net.minecraft.world.entity.player.Player;

public interface INodeMixin {
    default boolean isVisible(Player player) {
        return true;
    };
}
