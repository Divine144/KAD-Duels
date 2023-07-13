package com.divinity.duels.mixin;

import com.divinity.duels.cap.GlobalCapAttacher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow private float pausePartialTick;

    @Shadow @Final private Timer timer;

    @Shadow private volatile boolean pause;

    @Redirect(method= "runTick", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/Timer;advanceTime(J)I"))
    public int advanceTime(Timer instance, long pGameTime) {
        var instanceM = (Minecraft) (Object) this;
        if (instanceM.level != null) {
            var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(instanceM.level);
            if (cap != null) {
                if (cap.isEnabled()) {
                    instance.advanceTime(pGameTime);
                    return 1; // When timer.partialTick = pausePartialTick, sometimes this can be 0, which prevents the for loop from executing the tick method
                }
            }
        }
        return instance.advanceTime(pGameTime);
    }

    @Inject(method= "runTick", at=@At(value = "TAIL"))
    public void runTick(boolean pRenderLevel, CallbackInfo ci) {
        var instanceM = (Minecraft) (Object) this;
        if (instanceM.level != null) {
            var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(instanceM.level);
            if (cap != null) {
                if (cap.isEnabled()) {
                    this.timer.partialTick = this.pausePartialTick;
                }
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tick(CallbackInfo ci) {
        var instance = (Minecraft) (Object) this;
        if (instance.level != null) {
            var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(instance.level);
            if (cap != null) {
                if (cap.isEnabled()) {
                   this.pause = false;
                }
            }
        }
    }
}
