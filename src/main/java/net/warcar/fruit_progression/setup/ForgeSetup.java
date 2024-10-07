package net.warcar.fruit_progression.setup;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.commands.AwakeCommand;

@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeSetup {
    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommands().getDispatcher();
        AwakeCommand.register(dispatcher);
    }
}
