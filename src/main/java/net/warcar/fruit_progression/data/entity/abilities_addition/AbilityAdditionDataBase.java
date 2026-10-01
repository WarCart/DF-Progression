package net.warcar.fruit_progression.data.entity.abilities_addition;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;

import java.util.HashMap;
import java.util.Map;

public class AbilityAdditionDataBase implements IAbilityAdditionData {
    private final Map<AbilityCore<? extends IAbility>, Integer> map;

    public AbilityAdditionDataBase() {
        this.map = new HashMap<>();
    }

    public Map<AbilityCore<? extends IAbility>, Integer> getMap() {
        return this.map;
    }

    public int getUsages(AbilityCore<? extends IAbility> ability) {
        return this.map.computeIfAbsent(ability, abilityCore -> 0);
    }

    public void setUsages(AbilityCore<? extends IAbility> ability, int usages) {
        this.map.put(ability, usages);
    }

    public void addUsages(AbilityCore<? extends IAbility> ability, int usages) {
        if (this.map.containsKey(ability)) {
            this.setUsages(ability, this.getUsages(ability) + usages);
        } else {
            this.setUsages(ability, usages);
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag props = new CompoundTag();
        CompoundTag map = new CompoundTag();
        this.getMap().forEach((core, q) -> {
            if (core != null && core.getRegistryKey() != null) {
                map.putInt(core.getRegistryKey().toString(), q);
            }
        });
        props.put("map", map);
        return props;
    }

    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag compoundNBT = nbt.getCompound("map");
        for (String name : compoundNBT.getAllKeys()) {
            this.setUsages(WyRegistry.ABILITIES.get().getValue(ResourceLocation.parse(name)), compoundNBT.getInt(name));
        }
    }
}
