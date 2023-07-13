package com.divinity.duels.event;

import com.divinity.duels.cap.GlobalCapAttacher;
import com.divinity.duels.network.NetworkHandler;
import com.divinity.duels.network.serverbound.UnfreezePacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void inputEvent(InputEvent.MouseButton.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(minecraft.level);
            if (cap != null && cap.isEnabled()) {
                event.setCanceled(true);
            }
        }
    }
}
