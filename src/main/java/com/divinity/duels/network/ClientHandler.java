package com.divinity.duels.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class ClientHandler {

    public static void syncMaxHealth(UUID uuid, double maxHealth) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level != null) {
            var player = level.getPlayerByUUID(uuid);
            if (player != null) {
                var attribute = player.getAttribute(Attributes.MAX_HEALTH);
                if (attribute != null) {
                    attribute.setBaseValue(maxHealth);
                }
            }
        }
    }
}
