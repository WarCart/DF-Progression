package net.warcar.fruit_progression.data.entity.awakening;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

public class AwakeningDataProvider implements ICapabilitySerializable<CompoundNBT> {
    private IAwakeningData instance;

    public AwakeningDataProvider() {
        this.instance = AwakeningDataCapability.INSTANCE.getDefaultInstance();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return AwakeningDataCapability.INSTANCE.orEmpty(cap, LazyOptional.of(() -> this.instance));
    }

    public CompoundNBT serializeNBT() {
        return (CompoundNBT)AwakeningDataCapability.INSTANCE.getStorage().writeNBT(AwakeningDataCapability.INSTANCE, this.instance, null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        AwakeningDataCapability.INSTANCE.getStorage().readNBT(AwakeningDataCapability.INSTANCE, this.instance, null, nbt);
    }
}
