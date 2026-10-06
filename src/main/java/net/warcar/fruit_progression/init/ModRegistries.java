package net.warcar.fruit_progression.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.nodes.NodeUnlock;
import net.warcar.fruit_progression.requirements.Requirement;

import java.util.function.Supplier;

public class ModRegistries {
    public static final ResourceKey<Registry<Requirement>> REQUIREMENTS_KEY = key("requirements");
    public static final ResourceKey<Registry<NodeUnlock>> ACTIONS_KEY = key("node_actions");
    public static final Supplier<IForgeRegistry<Requirement>> REQUIREMENTS = () -> RegistryManager.ACTIVE.getRegistry(REQUIREMENTS_KEY);
    public static final Supplier<IForgeRegistry<NodeUnlock>> ACTIONS = () -> RegistryManager.ACTIVE.getRegistry(ACTIONS_KEY);

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(DevilFruitProgressionMod.MOD_ID, name));
    }
}
