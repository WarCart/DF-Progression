package net.warcar.fruit_progression;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.data.entity.abilities_addition.SSyncAdditionalDataPacket;
import net.warcar.fruit_progression.init.ModRegistries;
import net.warcar.fruit_progression.init.ModRequirements;
import net.warcar.fruit_progression.new_data_reader.AbilityDataReader;
import net.warcar.fruit_progression.new_data_reader.RequirementModifiersPile;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DevilFruitProgressionMod.MOD_ID)
public class DevilFruitProgressionMod {
    public static final String MOD_ID = "ability_progression";
    public static final Logger LOGGER = LogManager.getLogger();

    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "main_channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    public static final AbilityDataReader<RequirementModifiersPile> ABILITIES_READER = new AbilityDataReader<>("abilities", RequirementModifiersPile::getFromJson);
    public static final AbilityDataReader<RequirementSetInstance> AWAKENINGS_READER = new AbilityDataReader<>("awakenings", RequirementSetInstance::getRequirementSetInstance);

    public DevilFruitProgressionMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModRegistries.REQUIREMENTS_REGISTER.register(bus);
        bus.addListener(this::setup);
        ModRequirements.register();
        INSTANCE.registerMessage(0, SSyncAdditionalDataPacket.class, SSyncAdditionalDataPacket::encode, SSyncAdditionalDataPacket::decode, SSyncAdditionalDataPacket::handle);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        AbilityAdditionDataCapability.register();
    }

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static class Events {
        @SubscribeEvent
        public static void addReloadListeners(AddReloadListenerEvent event) {
            event.addListener(ABILITIES_READER);
            event.addListener(AWAKENINGS_READER);
        }
    }
}
