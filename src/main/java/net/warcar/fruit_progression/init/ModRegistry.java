package net.warcar.fruit_progression.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.requirements.Requirement;
import xyz.pixelatedw.mineminenomi.api.ModRegistries;

public class ModRegistry {
    public static final IForgeRegistry<Requirement> REQUIREMENTS;
    public static final DeferredRegister<Requirement> REQUIREMENTS_REGISTER;

    static {
        ModRegistries.make(new ResourceLocation(DevilFruitProgressionMod.MOD_ID, "reqirements"), Requirement.class);
        REQUIREMENTS = RegistryManager.ACTIVE.getRegistry(Requirement.class);
        REQUIREMENTS_REGISTER = DeferredRegister.create(REQUIREMENTS, DevilFruitProgressionMod.MOD_ID);
    }
}
