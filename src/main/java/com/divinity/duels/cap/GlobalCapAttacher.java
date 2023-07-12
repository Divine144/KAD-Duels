package com.divinity.duels.cap;

import com.divinity.duels.Duels;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Duels.MODID)
public class GlobalCapAttacher extends CapabilityAttacher {
    private static final Class<GlobalCap> CAPABILITY_CLASS = GlobalCap.class;
    public static final Capability<GlobalCap> EXAMPLE_GLOBAL_LEVEL_CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation EXAMPLE_GLOBAL_LEVEL_CAPABILITY_RL = new ResourceLocation(Duels.MODID, "example_global_level_capability");

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static GlobalCap getGlobalLevelCapabilityUnwrap(Level level) {
        return getGlobalLevelCapability(level).orElse(null);
    }

    public static LazyOptional<GlobalCap> getGlobalLevelCapability(Level level) {
        return getGlobalLevelCapability(level, EXAMPLE_GLOBAL_LEVEL_CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<Level> event, Level level) {
        genericAttachCapability(event, new GlobalCap(level), EXAMPLE_GLOBAL_LEVEL_CAPABILITY, EXAMPLE_GLOBAL_LEVEL_CAPABILITY_RL);
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerGlobalLevelAttacher(GlobalCapAttacher::attach, GlobalCapAttacher::getGlobalLevelCapability);
    }
}
