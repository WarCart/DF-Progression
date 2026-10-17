package net.warcar.fruit_progression.data.entity.abilities_addition;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.config.ModConfig;
import net.warcar.fruit_progression.init.ModRequirements;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.Map;
import java.util.Set;

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
        LivingEntity owner = this.getOwner();
        if (DevilFruitCapability.hasDevilFruit(owner, df)) {
            points = ModConfig.getPointsForOwnFruit();
            this.readAboutOwnFruit();
        } else {
            if (owner.getRandom().nextDouble() <= ModConfig.getChanceForOtherFruit()) {
                points = ModConfig.getPointsForOtherFruit();
            } else {
                points = 0;
            }
        }
        EntityStatsCapability.getLazy(owner).ifPresent(props -> {
            props.alterTrainingPoints(ModRequirements.DEVIL_FRUIT_POINTS, points);
        });
        setRead(df);
        return true;
    }

    void setRead(AkumaNoMiItem df);

    Set<AkumaNoMiItem> allReadFruits();

    boolean hasReadAboutOwnFruit();

    void readAboutOwnFruit();
}
