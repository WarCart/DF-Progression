package net.warcar.fruit_progression;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.warcar.fruit_progression.data.entity.abilities_addition.SSyncAdditionalDataPacket;
import net.warcar.fruit_progression.init.ModActions;
import net.warcar.fruit_progression.init.ModRegistries;
import net.warcar.fruit_progression.init.ModRequirements;
import net.warcar.fruit_progression.new_data_reader.AbilityDataReader;
import net.warcar.fruit_progression.new_data_reader.RequirementModifiersPile;
import net.warcar.fruit_progression.nodes.AbilityNodeLink;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DevilFruitProgressionMod.MOD_ID)
public class DevilFruitProgressionMod {
    public static final String MOD_ID = "ability_progression";
    public static final Logger LOGGER = LogManager.getLogger();

    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(ResourceLocation.fromNamespaceAndPath(MOD_ID, "main_channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static final AbilityDataReader<RequirementModifiersPile> ABILITIES_READER = new AbilityDataReader<>("abilities", RequirementModifiersPile::getFromJson);
    public static final AbilityDataReader<RequirementSetInstance> AWAKENINGS_READER = new AbilityDataReader<>("awakenings", RequirementSetInstance::getRequirementSetInstance);
    public static final AbilityDataReader<RequirementSetInstance> SAVED_REQUIREMENTS_READER = new AbilityDataReader<>("saved_requirements", RequirementSetInstance::getRequirementSetInstance);
    public static final AbilityDataReader<AbilityNodeLink> ABILITY_TREE_READER = new AbilityDataReader<>("ability_tree_nodes", AbilityNodeLink::deserializeFromJson);

    public DevilFruitProgressionMod(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        bus.addListener(this::registerNewRegistries);
        ModRequirements.register(bus);
        ModActions.register(bus);
        INSTANCE.registerMessage(0, SSyncAdditionalDataPacket.class, SSyncAdditionalDataPacket::encode, SSyncAdditionalDataPacket::decode, SSyncAdditionalDataPacket::handle);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerNewRegistries(NewRegistryEvent event) {
        event.create(new RegistryBuilder<>().setName(ModRegistries.REQUIREMENTS_KEY.location()));
        event.create(new RegistryBuilder<>().setName(ModRegistries.ACTIONS_KEY.location()));
    }

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static class Events {
        @SubscribeEvent
        public static void addReloadListeners(AddReloadListenerEvent event) {
            event.addListener(SAVED_REQUIREMENTS_READER);
            event.addListener(ABILITIES_READER);
            event.addListener(AWAKENINGS_READER);
            event.addListener(ABILITY_TREE_READER);
        }
    }
}
