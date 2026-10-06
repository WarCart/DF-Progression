package net.warcar.fruit_progression.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.requirements.*;
import xyz.pixelatedw.mineminenomi.api.enums.TrainingPointType;

public class ModRequirements {
    public static final DeferredRegister<Requirement> REQUIREMENTS_REGISTER = DeferredRegister.create(ModRegistries.REQUIREMENTS_KEY, DevilFruitProgressionMod.MOD_ID);
    public static final TrainingPointType DEVIL_FRUIT_POINTS = TrainingPointType.create("DEVIL_FRUIT");
    public static final RegistryObject<SavedRequirement> SAVED = registerRequirement(new SavedRequirement(), "saved");

    private static <T extends Requirement> RegistryObject<T> registerRequirement(T requirement, String resourceName) {
        return REQUIREMENTS_REGISTER.register(resourceName, () -> requirement);
    }
    public static void register(IEventBus bus) {
        REQUIREMENTS_REGISTER.register(bus);

        registerRequirement(new AlwaysTrueRequirement(), "always");
        registerRequirement(new DorikiRequirement(), "doriki");
        registerRequirement(new HakiRequirement(), "haki");
        registerRequirement(new RaceRequirement(), "race");
        registerRequirement(new SubRaceRequirement(), "sub_race");
        registerRequirement(new StyleRequirement(), "fighting_style");
        registerRequirement(new FactionRequirement(), "faction");
        registerRequirement(new FruitRequirement(), "devil_fruit");
        registerRequirement(new DevilFruitMasteryRequirement(), "devil_fruit_mastery");
        registerRequirement(new AwakeningRequirement(), "awakening");
        registerRequirement(new QuestRequirement(), "quest");
        registerRequirement(new HaoshokuBornRequirement(), "haoshoku_born");
        registerRequirement(new AbilityUnlockedRequirement(), "unlocked_ability");
        registerRequirement(new DefaultRequirement(), "default");
        registerRequirement(new LoyaltyRequirement(), "loyalty");
        registerRequirement(new UsedAbilityRequirement(), "ability_used");
        registerRequirement(new TrainingPointsRequirement(), "training_points");
    }
}
