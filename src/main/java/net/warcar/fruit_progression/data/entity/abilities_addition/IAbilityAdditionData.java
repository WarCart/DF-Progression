package net.warcar.fruit_progression.data.entity.abilities_addition;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.init.ModRequirements;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

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

    LivingEntity getOwner();

    boolean hasReadAbout(AkumaNoMiItem df);

    default boolean readAbout(AkumaNoMiItem df) {
        if (this.hasReadAbout(df)) {
            return false;
        }
        int points;
        if (DevilFruitCapability.hasDevilFruit(this.getOwner(), df)) {
            points = 5;
        } else {
            points = 1;
        }
        EntityStatsCapability.getLazy(this.getOwner()).ifPresent(props ->
                props.alterTrainingPoints(ModRequirements.DEVIL_FRUIT_POINTS, points));
        setRead(df);
        return true;
    }

    void setRead(AkumaNoMiItem df);
}
