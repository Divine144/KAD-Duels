package com.divinity.duels.network;

import com.divinity.duels.Duels;
import com.divinity.duels.cap.GlobalCapAttacher;
import com.divinity.duels.network.clientbound.MaxHealthPacket;
import com.divinity.duels.network.serverbound.UnfreezePacket;
import com.google.common.collect.ImmutableList;
import dev._100media.capabilitysyncer.network.SimpleLevelCapabilityStatusPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.function.BiConsumer;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Duels.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int nextId = 0;

    public static void register() {
        List<BiConsumer<SimpleChannel, Integer>> packets = ImmutableList.<BiConsumer<SimpleChannel, Integer>>builder()
                .add(SimpleLevelCapabilityStatusPacket::register)
                .add(UnfreezePacket::register)
                .add(MaxHealthPacket::register)
                .build();
        SimpleLevelCapabilityStatusPacket.registerRetriever(GlobalCapAttacher.EXAMPLE_GLOBAL_LEVEL_CAPABILITY_RL, GlobalCapAttacher::getGlobalLevelCapabilityUnwrap);
        packets.forEach(consumer -> consumer.accept(INSTANCE, getNextId()));
    }

    private static int getNextId() {
        return nextId++;
    }
}