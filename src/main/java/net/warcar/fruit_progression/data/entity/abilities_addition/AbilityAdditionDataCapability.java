package net.warcar.fruit_progression.data.entity.abilities_addition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import xyz.pixelatedw.mineminenomi.api.ModRegistries;

public class AbilityAdditionDataCapability {
    @CapabilityInject(IAbilityAdditionData.class)
    public static final Capability<IAbilityAdditionData> INSTANCE = null;

    public static IAbilityAdditionData get(LivingEntity entity) {
        return getLazy(entity).orElse(new AbilityAdditionDataBase());
    }

    public static LazyOptional<IAbilityAdditionData> getLazy(LivingEntity entity) {
        return entity.getCapability(INSTANCE, null);
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IAbilityAdditionData.class, new Capability.IStorage<IAbilityAdditionData>() {
            public INBT writeNBT(Capability<IAbilityAdditionData> capability, IAbilityAdditionData instance, Direction side) {
                CompoundNBT props = new CompoundNBT();
                CompoundNBT map = new CompoundNBT();
                instance.getMap().forEach((core, q) -> {
                    if (core != null && core.getRegistryName() != null) {
                        map.putInt(core.getRegistryName().toString(), q);
                    }
                });
                props.put("map", map);
                return props;
            }
            public void readNBT(Capability<IAbilityAdditionData> capability, IAbilityAdditionData instance, Direction side, INBT nbt) {
                CompoundNBT compoundNBT = ((CompoundNBT) nbt).getCompound("map");
                for (String name : compoundNBT.getAllKeys()) {
                    instance.setUsages(ModRegistries.ABILITIES.getValue(new ResourceLocation(name)), compoundNBT.getInt(name));
                }
            }
        }, AbilityAdditionDataBase::new);
    }
}
