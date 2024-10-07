package net.warcar.fruit_progression.setup;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModCapabilities;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ModCapabilities.init();
    }
}
