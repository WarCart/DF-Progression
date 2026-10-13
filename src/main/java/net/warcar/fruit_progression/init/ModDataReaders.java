package net.warcar.fruit_progression.init;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.new_data_reader.AbilityDataReader;
import net.warcar.fruit_progression.data.new_data_reader.RequirementModifiersPile;
import net.warcar.fruit_progression.nodes.AbilityNodeLink;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.init.ModNetwork;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;

import java.util.Map;


@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class ModDataReaders {
    public static final AbilityDataReader<RequirementModifiersPile> ABILITIES_READER = new AbilityDataReader<>("abilities", RequirementModifiersPile::getFromJson);
    public static final AbilityDataReader<RequirementSetInstance> AWAKENINGS_READER = new AbilityDataReader<>("awakenings", RequirementSetInstance::getRequirementSetInstance);
    public static final AbilityDataReader<RequirementSetInstance> SAVED_REQUIREMENTS_READER = new AbilityDataReader<>("saved_requirements", RequirementSetInstance::getRequirementSetInstance);
    public static final AbilityDataReader<AbilityNodeLink> ABILITY_TREE_READER = new AbilityDataReader<>("ability_tree_nodes", AbilityNodeLink::deserializeFromJson){
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> elementMap, ResourceManager manager, ProfilerFiller profiler) {
            super.apply(elementMap, manager, profiler);
            DevilFruitProgressionMod.LOGGER.info("Reloading player nodes");
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                DevilFruitProgressionMod.LOGGER.warn("Server is null!");
                return;
            }
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                AbilityCapability.get(player).ifPresent(data -> {
                    CompoundTag tag = data.serializeNBT();
                    data.deserializeNBT(tag);
                    ModNetwork.sendTo(new SSyncAbilityDataPacket(player, data), player);
                });
            }
            DevilFruitProgressionMod.LOGGER.info("Finished reloading players");
        }
    };

    public static void init() {}

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(SAVED_REQUIREMENTS_READER);
        event.addListener(ABILITIES_READER);
        event.addListener(AWAKENINGS_READER);
        event.addListener(ABILITY_TREE_READER);
    }
}
