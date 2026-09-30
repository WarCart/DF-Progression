package net.warcar.fruit_progression.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitBase;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;

@Mixin(DevilFruitBase.class)
public abstract class DevilFruitDataMixin implements IDevilFruit {
    @Shadow(remap = false) private boolean hasAwakenedFruit;

    @Override
    public boolean hasAwakenedFruit() {
        return this.hasAwakenedFruit;
    }
}
