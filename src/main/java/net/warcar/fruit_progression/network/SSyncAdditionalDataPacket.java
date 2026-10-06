package net.warcar.fruit_progression.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.data.entity.abilities_addition.IAbilityAdditionData;

import java.util.Optional;
import java.util.function.Supplier;

public class SSyncAdditionalDataPacket {
    private int entityId;
    private CompoundTag data;

    private SSyncAdditionalDataPacket() {}

    public SSyncAdditionalDataPacket(Entity entity, IAbilityAdditionData data) {
        this.entityId = entity.getId();
        this.data = (CompoundTag) data;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeNbt(this.data);
    }

    public static SSyncAdditionalDataPacket decode(FriendlyByteBuf buffer) {
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
            if (target instanceof LivingEntity entity) {
                Optional<IAbilityAdditionData> props = AbilityAdditionDataCapability.get(entity);
                props.ifPresent(data -> data.deserializeNBT(message.data));
            }
        }
    }
}
