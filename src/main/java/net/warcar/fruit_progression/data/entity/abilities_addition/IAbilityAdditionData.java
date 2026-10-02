package net.warcar.fruit_progression.data.entity.abilities_addition;

import net.minecraft.nbt.CompoundTag;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;

import java.util.Map;

public interface IAbilityAdditionData {
    Map<AbilityCore<? extends IAbility>, Integer> getMap();

    int getUsages(AbilityCore<? extends IAbility> ability);

    void setUsages(AbilityCore<? extends IAbility> ability, int usages);

    void addUsages(AbilityCore<? extends IAbility> ability, int usages);

    default void addUsages(AbilityCore<? extends IAbility> ability) {
        this.addUsages(ability, 1);
    }

    float getDevilFruitMastery();

    void setDevilFruitMastery(float mastery);

    default void addDevilFruitMastery(float mastery) {
        this.setDevilFruitMastery(this.getDevilFruitMastery() + mastery);
    }

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag nbt);
}
