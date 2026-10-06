package net.warcar.fruit_progression.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.data.mixin_interfaces.IAbilityDataExtended;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CUnlockAbilityNodePacket {
	private ResourceLocation key;

	public CUnlockAbilityNodePacket() {}

	public CUnlockAbilityNodePacket(ResourceLocation key) {
		this.key = key;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.key);
	}

	public static CUnlockAbilityNodePacket decode(FriendlyByteBuf buffer) {
		CUnlockAbilityNodePacket msg = new CUnlockAbilityNodePacket();
		msg.key = buffer.readResourceLocation();
		return msg;
	}

	public static void handle(CUnlockAbilityNodePacket message, final Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
			ctx.get().enqueueWork(() -> {
				try {
					ServerPlayer player = ctx.get().getSender();

					IAbilityData props = AbilityCapability.get(player).orElse(null);

					if (props == null) {
						return;
					}

					AbilityNode node = ((IAbilityDataExtended) props).ability_progression$getSerialNode(message.key);
					if (node == null) {
						return;
					}

					if (node.canUnlock(player)) {
						node.unlockNode(player);

						List<Integer> unlockableNodes = new ArrayList<>();
						for (AbilityNode node2 : props.getNodes()) {
							if (node2.canUnlock(player)) {
								unlockableNodes.add(node2.hashCode());
							}
						}

						DevilFruitProgressionMod.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SUnlockAbilityNodePacket(message.key, unlockableNodes));
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		ctx.get().setPacketHandled(true);
	}
}
