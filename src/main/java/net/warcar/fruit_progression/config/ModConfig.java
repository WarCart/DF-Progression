package net.warcar.fruit_progression.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.warcar.fruit_progression.init.ModDataReaders;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;

import javax.annotation.Nullable;

public class ModConfig {
    public static final ForgeConfigSpec SPEC;

    private static ForgeConfigSpec.BooleanValue encyclopediaOverhaul;
    private static ForgeConfigSpec.ConfigValue<String> defaultAwakeningRequirement;

    private static ForgeConfigSpec.IntValue pointsForOwnFruit;
    private static ForgeConfigSpec.IntValue pointsForOtherFruit;
    private static ForgeConfigSpec.DoubleValue chanceForOtherFruit;
    private static ForgeConfigSpec.DoubleValue masteryUse;
    private static ForgeConfigSpec.DoubleValue masteryTick;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Systems");
        encyclopediaOverhaul = builder.comment("Encyclopedia rework, allowing to get df training points from quests or from reading about fruits").define("Encyclopedia Rework", true);
        defaultAwakeningRequirement = builder.comment("Saved requirement set that will be applied for all awakenings unless there exists specific awakening").define("Default awakening", "none");
        builder.pop();
        builder.push("DF mastery");
        pointsForOwnFruit = builder.comment("Points player gets for reading about fruit that they have already").defineInRange("Points for own fruit", 5, 0, 999);
        pointsForOtherFruit = builder.comment("Points player gets for reading about any fruit that they don't have").defineInRange("Points for other fruit", 1, 0, 999);
        chanceForOtherFruit = builder.comment("Chance that player will get points for fruit that isn't theirs").defineInRange("Chance for other fruit", 0.5, 0, 1);
        masteryUse = builder.comment("Devil fruit mastery player gets for actively using an ability").defineInRange("Mastery on use", 0.5, 0, 100);
        masteryTick = builder.comment("Devil fruit mastery player gets per tick (20 times per second) while in zoan form").defineInRange("Mastery per tick", 0.005, 0, 100);
    }

    public static int getPointsForOwnFruit() {
        return pointsForOwnFruit.get();
    }

    public static int getPointsForOtherFruit() {
        return pointsForOtherFruit.get();
    }

    public static double getChanceForOtherFruit() {
        return chanceForOtherFruit.get();
    }

    public static boolean isEncyclopediaOverhauled() {
        return encyclopediaOverhaul.get();
    }

    public static float getMasteryPerTick() {
        return (float) (double) masteryTick.get();
    }

    public static float getMasteryPerUse() {
        return (float) (double) masteryUse.get();
    }

    @Nullable
    public static RequirementSetInstance awakeningDefault() {
        return ModDataReaders.SAVED_REQUIREMENTS_READER.get(ResourceLocation.tryParse(defaultAwakeningRequirement.get()));
    }
}
