package net.warcar.fruit_progression.data.entity.abilities_addition;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SSyncAdditionalDataPacket {
    private int entityId;
    private CompoundNBT data;

    private SSyncAdditionalDataPacket() {}

    public SSyncAdditionalDataPacket(Entity entity, IAbilityAdditionData data) {
        this.entityId = entity.getId();
        this.data = (CompoundNBT) AbilityAdditionDataCapability.INSTANCE.writeNBT(data, null);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeNbt(this.data);
    }

    public static SSyncAdditionalDataPacket decode(PacketBuffer buffer) {
        SSyncAdditionalDataPacket msg = new SSyncAdditionalDataPacket();
        msg.entityId = buffer.readInt();
        msg.data = buffer.readNbt();
        return msg;
    }

    public static void handle(SSyncAdditionalDataPacket message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() -> ClientHandler.handle(message));
        }

        ctx.get().setPacketHandled(true);
    }



    public static class ClientHandler {
        @OnlyIn(Dist.CLIENT)
        public static void handle(SSyncAdditionalDataPacket message) {
            Entity target = Minecraft.getInstance().level.getEntity(message.entityId);
            if (target instanceof LivingEntity) {
                IAbilityAdditionData props = AbilityAdditionDataCapability.get((LivingEntity)target);
                AbilityAdditionDataCapability.INSTANCE.getStorage().readNBT(AbilityAdditionDataCapability.INSTANCE, props, null, message.data);
            }
        }
    }
}
