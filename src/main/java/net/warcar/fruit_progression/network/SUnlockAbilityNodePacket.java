package net.warcar.fruit_progression.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.warcar.fruit_progression.data.mixin_interfaces.IAbilityDataExtended;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.ui.screens.AbilityTreeScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SUnlockAbilityNodePacket {
	private ResourceLocation key;

	private List<Integer> nodes;

	public SUnlockAbilityNodePacket() {}

	public SUnlockAbilityNodePacket(ResourceLocation key, List<Integer> nodes) {
		this.key = key;
		this.nodes = nodes;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.key);
		buffer.writeInt(this.nodes.size());
		for (int hash : this.nodes) {
			buffer.writeInt(hash);
		}
	}

	public static SUnlockAbilityNodePacket decode(FriendlyByteBuf buffer) {
		SUnlockAbilityNodePacket msg = new SUnlockAbilityNodePacket();

		msg.key = buffer.readResourceLocation();
		msg.nodes = new ArrayList<>();
		int bufferSize = buffer.readInt();
		for (int i = 0; i < bufferSize; i++) {
			msg.nodes.add(buffer.readInt());
		}

		return msg;
	}

	public static void handle(SUnlockAbilityNodePacket message, final Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
			ctx.get().enqueueWork(() -> ClientHandler.handle(message));
		}

		ctx.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	public static class ClientHandler {
		public static void handle(SUnlockAbilityNodePacket message) {
			try {
				Minecraft mc = Minecraft.getInstance();

				LocalPlayer player = mc.player;

				IAbilityData props = AbilityCapability.get(player).orElse(null);

				if (props == null) {
					return;
				}

				AbilityNode node = ((IAbilityDataExtended) props).ability_progression$getSerialNode(message.key);
				if (node == null) {
					return;
				}

				node.unlockNode(player);

				if (mc.screen instanceof AbilityTreeScreen treeScreen) {
					treeScreen.unlockableNodes.clear();
					for (AbilityNode node2 : props.getNodes()) {
						if (message.nodes.contains(node2.hashCode())) {
							treeScreen.unlockableNodes.add(node2);
						}
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
