package net.warcar.fruit_progression.data.entity.abilities_addition;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class AbilityAdditionDataProvider implements ICapabilitySerializable<CompoundNBT> {
    private IAbilityAdditionData instance;

    public AbilityAdditionDataProvider() {
        this.instance = AbilityAdditionDataCapability.INSTANCE.getDefaultInstance();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return AbilityAdditionDataCapability.INSTANCE.orEmpty(cap, LazyOptional.of(() -> this.instance));
    }

    public CompoundNBT serializeNBT() {
        return (CompoundNBT) AbilityAdditionDataCapability.INSTANCE.getStorage().writeNBT(AbilityAdditionDataCapability.INSTANCE, this.instance, null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        AbilityAdditionDataCapability.INSTANCE.getStorage().readNBT(AbilityAdditionDataCapability.INSTANCE, this.instance, null, nbt);
    }
}
