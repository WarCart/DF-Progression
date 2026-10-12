package net.warcar.fruit_progression.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.equations.Operator;
import xyz.pixelatedw.mineminenomi.api.WyHelper;

import java.util.function.Supplier;

public class ModOperators {
    public static final DeferredRegister<Operator> OPERATORS_REGISTER = DeferredRegister.create(ModRegistries.OPERATORS_KEY, DevilFruitProgressionMod.MOD_ID);

    private static <T extends Operator> void registerOperator(Supplier<T> supplier, String name) {
        OPERATORS_REGISTER.register(WyHelper.getResourceName(name), supplier);
    }

    public static void register(IEventBus bus) {
        OPERATORS_REGISTER.register(bus);
    }
}
