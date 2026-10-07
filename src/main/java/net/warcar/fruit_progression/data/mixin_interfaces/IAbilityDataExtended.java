package net.warcar.fruit_progression.data.mixin_interfaces;

import net.minecraft.resources.ResourceLocation;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;

public interface IAbilityDataExtended {
    AbilityNode ability_progression$getSerialNode(ResourceLocation key);
    void ability_progression$addSerialNode(ResourceLocation key, AbilityNode node);
}
