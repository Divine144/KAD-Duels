package com.divinity.duels.network.clientbound;

import com.divinity.duels.network.ClientHandler;
import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;

public class MaxHealthPacket implements IPacket {

    private UUID player;
    private double amount;

    public MaxHealthPacket(UUID player, double amount) {
        super();
        this.player = player;
        this.amount = amount;
    }

    public static MaxHealthPacket read(FriendlyByteBuf packetBuf) {
        return new MaxHealthPacket(packetBuf.readUUID(), packetBuf.readDouble());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, MaxHealthPacket.class, MaxHealthPacket::read);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> ClientHandler.syncMaxHealth(player, amount));
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeUUID(player);
        packetBuf.writeDouble(amount);
    }
}

