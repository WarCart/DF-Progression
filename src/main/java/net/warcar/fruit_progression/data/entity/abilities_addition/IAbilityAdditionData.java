package net.warcar.fruit_progression.data.entity.abilities_addition;

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
}
