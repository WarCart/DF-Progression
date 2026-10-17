package net.warcar.fruit_progression.data.entity.abilities_addition;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AbilityAdditionDataBase implements IAbilityAdditionData {
    private final LivingEntity owner;
    private final Map<AbilityCore<? extends IAbility>, Integer> map;
    private float devilFruitMastery;
    private final Set<AkumaNoMiItem> seenFruits;
    private boolean hasReadOwnFruit;

    public AbilityAdditionDataBase(LivingEntity owner) {
        this.map = new HashMap<>();
        this.owner = owner;
        this.seenFruits = new HashSet<>();
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

    public float getDevilFruitMastery() {
        return this.devilFruitMastery;
    }

    public void setDevilFruitMastery(float mastery) {
        this.devilFruitMastery = Mth.clamp(mastery, 0, 100);
    }

    public CompoundTag serializeNBT() {
        CompoundTag props = new CompoundTag();
        CompoundTag map = new CompoundTag();
        this.getMap().forEach((core, uses) -> {
            if (core != null && core.getRegistryKey() != null) {
                map.putInt(core.getRegistryKey().toString(), uses);
            }
        });
        props.put("map", map);
        props.putFloat("devilFruitMastery", this.devilFruitMastery);
        ListTag seen = new ListTag();
        for (AkumaNoMiItem df : this.seenFruits) {
            seen.add(StringTag.valueOf(df.getRegistryKey().toString()));
        }
        props.put("seenFruits", seen);
        props.putBoolean("hasReadOwnFruit", this.hasReadOwnFruit);
        return props;
    }

    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag compoundNBT = nbt.getCompound("map");
        for (String name : compoundNBT.getAllKeys()) {
            this.setUsages(WyRegistry.ABILITIES.get().getValue(ResourceLocation.parse(name)), compoundNBT.getInt(name));
        }
        this.devilFruitMastery = nbt.getFloat("devilFruitMastery");
        ListTag seen = nbt.getList("seenFruits", 8);
        for (int i = 0; i < seen.size(); i++) {
            ResourceLocation id = ResourceLocation.tryParse(seen.getString(i));
            if (id == null)
                continue;
            if (ForgeRegistries.ITEMS.getValue(id) instanceof AkumaNoMiItem df)
                this.seenFruits.add(df);
        }
        this.hasReadOwnFruit = nbt.getBoolean("hasReadOwnFruit");
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    public boolean hasReadAbout(AkumaNoMiItem df) {
        return this.seenFruits.contains(df);
    }

    @Override
    public void setRead(AkumaNoMiItem df) {
        this.seenFruits.add(df);
    }

    @Override
    public Set<AkumaNoMiItem> allReadFruits() {
        return new HashSet<>(this.seenFruits);
    }

    @Override
    public boolean hasReadAboutOwnFruit() {
        return this.hasReadOwnFruit;
    }

    @Override
    public void readAboutOwnFruit() {
        this.hasReadOwnFruit = true;
    }
}
