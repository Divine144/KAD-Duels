package com.divinity.duels.mixin;

import com.divinity.duels.cap.GlobalCapAttacher;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V",at=@At("HEAD"),cancellable = true)
    public void tick(BooleanSupplier pHasTimeLeft, CallbackInfo ci) {
        var instance = (ServerLevel) (Object) this;
        if (instance != null) {
            var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(instance);
            if (cap != null) {
                if (cap.isEnabled()) {
                    ci.cancel();
                }
            }
        }
    }
}
