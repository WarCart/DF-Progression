package net.warcar.fruit_progression.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.warcar.fruit_progression.requirements.*;

public class ModRequirements {
    private static <T extends Requirement> void registerRequirement(T requirement, String resourceName) {
        ModRegistries.REQUIREMENTS_REGISTER.register(resourceName, () -> requirement);
    }
    public static void register(IEventBus bus) {
        ModRegistries.REQUIREMENTS_REGISTER.register(bus);

        registerRequirement(new DorikiRequirement(), "doriki");
        registerRequirement(new HakiRequirement(), "haki");
        registerRequirement(new RaceRequirement(), "race");
        registerRequirement(new SubRaceRequirement(), "sub_race");
        registerRequirement(new StyleRequirement(), "fighting_style");
        registerRequirement(new FactionRequirement(), "faction");
        registerRequirement(new FruitRequirement(), "devil_fruit");
        registerRequirement(new AwakeningRequirement(), "awakening");
        registerRequirement(new QuestRequirement(), "quest");
        registerRequirement(new HaoshokuBornRequirement(), "haoshoku_born");
        registerRequirement(new AbilityUnlockedRequirement(), "unlocked_ability");
        registerRequirement(new DefaultRequirement(), "default");
        registerRequirement(new LoyaltyRequirement(), "loyalty");
        registerRequirement(new UsedAbilityRequirement(), "ability_used");
    }
}
