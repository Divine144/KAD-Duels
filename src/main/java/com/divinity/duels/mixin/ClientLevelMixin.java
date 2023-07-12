package com.divinity.duels.mixin;

import com.divinity.duels.cap.GlobalCapAttacher;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Inject(method= "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("HEAD"), cancellable = true)
    public void tick(BooleanSupplier pHasTimeLeft, CallbackInfo ci) {
        var instance = (ClientLevel) (Object) this;
        if (instance != null) {
            var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(instance);
            if (cap != null) {
                if (cap.isEnabled()) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method= "tickEntities()V", at=@At("HEAD"), cancellable = true)
    public void tickEntities(CallbackInfo ci) {
        var instance = (ClientLevel) (Object) this;
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
