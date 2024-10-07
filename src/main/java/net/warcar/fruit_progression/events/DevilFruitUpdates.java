package net.warcar.fruit_progression.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.pixelatedw.mineminenomi.api.events.stats.DorikiEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.HakiExpEvent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = "fruit_progression")
public class DevilFruitUpdates {
	@SubscribeEvent
	public static void hakiAdd(HakiExpEvent event) {
		update(event);
	}

	@SubscribeEvent
	public static void dorikiAdd(DorikiEvent event) {
		update(event);
	}

	public static void update(@Nonnull PlayerEvent event) {
		PlayerEntity player = event.getPlayer();
		AbilityHelper.validateDevilFruitMoves(player);
		WyNetwork.sendToAllTrackingAndSelf(new SSyncAbilityDataPacket(player.getId(), AbilityDataCapability.get(player)), player);
	}
}
