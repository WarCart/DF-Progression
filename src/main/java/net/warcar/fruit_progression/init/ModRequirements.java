package net.warcar.fruit_progression.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.requirements.*;
import xyz.pixelatedw.mineminenomi.api.ModRegistries;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;

public class ModRequirements {
    private static <T extends Requirement> void registerRequirement(T requirement, String resourceName) {
        ResourceLocation key = new ResourceLocation(DevilFruitProgressionMod.MOD_ID, resourceName);
        RegistryObject<AbilityCore<?>> ret = RegistryObject.of(key, ModRegistries.ABILITIES);
        if (!ModRegistry.REQUIREMENTS_REGISTER.getEntries().contains(ret)) {
            ModRegistry.REQUIREMENTS_REGISTER.register(resourceName, () -> requirement);
        }
    }
    public static void register() {
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
        registerRequirement(new DefaultReqirement(), "default");
        registerRequirement(new LoyaltyRequirement(), "loyalty");
        registerRequirement(new UsedAbilityRequirement(), "ability_used");
        if (ModList.get().isLoaded("dcintegration")) {
            registerRequirement(new DiscordRoleRequirement(), "discord_role");
        }
    }
}
