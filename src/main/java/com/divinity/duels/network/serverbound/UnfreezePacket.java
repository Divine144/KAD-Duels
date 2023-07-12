package com.divinity.duels.network.serverbound;

import com.divinity.duels.cap.GlobalCapAttacher;
import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class UnfreezePacket implements IPacket {

    public UnfreezePacket() {
        super();
    }

    public static UnfreezePacket read(FriendlyByteBuf packetBuf) {
        return new UnfreezePacket();
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, UnfreezePacket.class, UnfreezePacket::read);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(player.getLevel());
                if (cap != null) {
                    cap.setEnabled();
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {

    }
}
