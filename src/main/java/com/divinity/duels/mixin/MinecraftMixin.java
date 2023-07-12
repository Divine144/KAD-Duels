package com.divinity.duels.mixin;

import com.divinity.duels.cap.GlobalCapAttacher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow private float pausePartialTick;

    @Shadow @Final private Timer timer;

    @Inject(method= "runTick", at=@At("TAIL"))
    public void runTick(boolean pRenderLevel, CallbackInfo ci) {
        var instance = (Minecraft) (Object) this;
        if (instance.level != null) {
            var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(instance.level);
            if (cap != null) {
                if (cap.isEnabled()) {
                    this.timer.partialTick = this.pausePartialTick;
                }
            }
        }
    }
}
