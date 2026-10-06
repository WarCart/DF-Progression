package net.warcar.fruit_progression;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.warcar.fruit_progression.init.*;
import net.warcar.fruit_progression.network.CUnlockAbilityNodePacket;
import net.warcar.fruit_progression.network.SSyncAdditionalDataPacket;
import net.warcar.fruit_progression.network.SUnlockAbilityNodePacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DevilFruitProgressionMod.MOD_ID)
public class DevilFruitProgressionMod {
    public static final String MOD_ID = "ability_progression";
    public static final Logger LOGGER = LogManager.getLogger();

    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(ResourceLocation.fromNamespaceAndPath(MOD_ID, "main_channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public DevilFruitProgressionMod(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        bus.addListener(this::registerNewRegistries);
        ModDataReaders.init();
        ModTexts.init();
        ModRequirements.register(bus);
        ModActions.register(bus);
        INSTANCE.registerMessage(0, SSyncAdditionalDataPacket.class, SSyncAdditionalDataPacket::encode, SSyncAdditionalDataPacket::decode, SSyncAdditionalDataPacket::handle);
        INSTANCE.registerMessage(1, CUnlockAbilityNodePacket.class, CUnlockAbilityNodePacket::encode, CUnlockAbilityNodePacket::decode, CUnlockAbilityNodePacket::handle);
        INSTANCE.registerMessage(2, SUnlockAbilityNodePacket.class, SUnlockAbilityNodePacket::encode, SUnlockAbilityNodePacket::decode, SUnlockAbilityNodePacket::handle);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerNewRegistries(NewRegistryEvent event) {
        event.create(new RegistryBuilder<>().setName(ModRegistries.REQUIREMENTS_KEY.location()));
        event.create(new RegistryBuilder<>().setName(ModRegistries.ACTIONS_KEY.location()));
    }
}
