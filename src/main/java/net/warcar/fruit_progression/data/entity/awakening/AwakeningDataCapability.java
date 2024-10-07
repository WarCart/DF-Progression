package net.warcar.fruit_progression.data.entity.awakening;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;

public class AwakeningDataCapability {
    @CapabilityInject(IAwakeningData.class)
    public static final Capability<IAwakeningData> INSTANCE = null;

    public static IAwakeningData get(LivingEntity entity) {
        return entity.getCapability(INSTANCE, null).orElse(new AwakeningDataBase());
    }

    public static LazyOptional<IAwakeningData> getLazy(LivingEntity entity) {
        return entity.getCapability(INSTANCE, null);
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IAwakeningData.class, new Capability.IStorage<IAwakeningData>() {
            public INBT writeNBT(Capability<IAwakeningData> capability, IAwakeningData instance, Direction side) {
                CompoundNBT props = new CompoundNBT();
                props.putBoolean("awake", instance.isAwakened());
                return props;
            }
            public void readNBT(Capability<IAwakeningData> capability, IAwakeningData instance, Direction side, INBT nbt) {
                instance.setAwake(((CompoundNBT) nbt).getBoolean("awake"));
            }
        }, AwakeningDataBase::new);
    }
}
