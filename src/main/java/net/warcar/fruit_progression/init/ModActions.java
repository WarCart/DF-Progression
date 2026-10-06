package net.warcar.fruit_progression.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.nodes.NodeUnlock;
import net.warcar.fruit_progression.nodes.RemovePointsAction;
import net.warcar.fruit_progression.nodes.UnlockAbilityAction;
import xyz.pixelatedw.mineminenomi.api.WyHelper;

import java.util.function.Supplier;

public class ModActions {
    public static final DeferredRegister<NodeUnlock> REQUIREMENTS_REGISTER = DeferredRegister.create(ModRegistries.ACTIONS_KEY, DevilFruitProgressionMod.MOD_ID);

    private static <T extends NodeUnlock> void registerAction(Supplier<T> supplier, String name) {
        REQUIREMENTS_REGISTER.register(WyHelper.getResourceName(name), supplier);
    }

    public static void register(IEventBus bus) {
        REQUIREMENTS_REGISTER.register(bus);
        registerAction(RemovePointsAction::new, "remove_points");
        registerAction(UnlockAbilityAction::new, "unlock_ability");
    }
}
