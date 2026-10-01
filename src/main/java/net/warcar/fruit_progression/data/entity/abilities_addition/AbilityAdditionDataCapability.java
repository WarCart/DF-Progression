package net.warcar.fruit_progression.data.entity.abilities_addition;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

public class AbilityAdditionDataCapability implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<IAbilityAdditionData> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });
    private final IAbilityAdditionData instance;

    public static Optional<IAbilityAdditionData> get(LivingEntity entity) {
        return getLazy(entity).resolve();
    }

    public AbilityAdditionDataCapability() {
        this.instance = new AbilityAdditionDataBase();
    }

    public static LazyOptional<IAbilityAdditionData> getLazy(LivingEntity entity) {
        return entity.getCapability(INSTANCE, null);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return INSTANCE.orEmpty(cap, LazyOptional.of(() -> this.instance));
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.instance.deserializeNBT(nbt);
    }
}
