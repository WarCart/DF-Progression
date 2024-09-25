package net.warcar.fruit_progression;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.init.ModConfig;
import net.warcar.fruit_progression.init.ModRegistry;
import net.warcar.fruit_progression.init.ModRequirements;
import net.warcar.fruit_progression.integrations.clothconfig.ModConfigsIntegration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DevilFruitProgressionMod.MOD_ID)
public class DevilFruitProgressionMod
{
    public static final String MOD_ID = "ability_progression";
    public static final Logger LOGGER = LogManager.getLogger();

    public DevilFruitProgressionMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModRegistry.REQUIREMENTS_REGISTER.register(bus);
        bus.addListener(this::setup);
        ModRequirements.register();
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SPEC);
        //if (ModList.get().isLoaded("cloth-config")) DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ModConfigsIntegration::registerConfigBuilder);
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void setup(final FMLCommonSetupEvent event) {
        AbilityAdditionDataCapability.register();
    }
}
