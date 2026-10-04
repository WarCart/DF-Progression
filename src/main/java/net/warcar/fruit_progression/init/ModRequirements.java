package net.warcar.fruit_progression.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.warcar.fruit_progression.requirements.*;
import xyz.pixelatedw.mineminenomi.api.enums.TrainingPointType;

public class ModRequirements {
    public static final TrainingPointType DEVIL_FRUIT_POINTS = TrainingPointType.create("DEVIL_FRUIT");

    private static <T extends Requirement> void registerRequirement(T requirement, String resourceName) {
        ModRegistries.REQUIREMENTS_REGISTER.register(resourceName, () -> requirement);
    }
    public static void register(IEventBus bus) {
        ModRegistries.REQUIREMENTS_REGISTER.register(bus);

        registerRequirement(new AlwaysTrueRequirement(), "always");
        registerRequirement(new DorikiRequirement(), "doriki");
        registerRequirement(new HakiRequirement(), "haki");
        registerRequirement(new RaceRequirement(), "race");
        registerRequirement(new SubRaceRequirement(), "sub_race");
        registerRequirement(new StyleRequirement(), "fighting_style");
        registerRequirement(new FactionRequirement(), "faction");
        registerRequirement(new FruitRequirement(), "devil_fruit");
        registerRequirement(new SavedRequirement(), "devil_fruit_mastery");
        registerRequirement(new AwakeningRequirement(), "awakening");
        registerRequirement(new QuestRequirement(), "quest");
        registerRequirement(new HaoshokuBornRequirement(), "haoshoku_born");
        registerRequirement(new AbilityUnlockedRequirement(), "unlocked_ability");
        registerRequirement(new DefaultRequirement(), "default");
        registerRequirement(new LoyaltyRequirement(), "loyalty");
        registerRequirement(new UsedAbilityRequirement(), "ability_used");
        registerRequirement(new TrainingPointsRequirement(), "training_points");
        registerRequirement(new SavedRequirement(), "saved");
    }
}
