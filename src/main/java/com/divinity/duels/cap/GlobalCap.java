package com.divinity.duels.cap;

import com.divinity.duels.network.NetworkHandler;
import dev._100media.capabilitysyncer.core.GlobalLevelCapability;
import dev._100media.capabilitysyncer.network.LevelCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleLevelCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.simple.SimpleChannel;

public class GlobalCap extends GlobalLevelCapability {
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int TICKS_PER_SECOND = 20;
    private int timerInterval = 10 * SECONDS_IN_MINUTE;
    private int timer = timerInterval * TICKS_PER_SECOND;
    private int countDown = TICKS_PER_SECOND * 10;
    private boolean enabled = false;

    public GlobalCap(Level level) {
        super(level);
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public int getTimer() {
        return timer;
    }

    public int getAndLowerTimer() {
        if (--timer < 0) {
            timer = 0;
        }
        return timer;
    }

    public void startCountdown() {
        this.countDown = TICKS_PER_SECOND * 10;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void startTimer () {
        this.timer = timerInterval * TICKS_PER_SECOND;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getTimerInterval() {
        return timerInterval;
    }

    public void setTimerInterval(int interval) {
        this.timerInterval = interval;
    }

    public void setEnabled() {
        enabled = !enabled;
        updateTracking();
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("timer", this.timer);
        nbt.putInt("countDown", this.countDown);
        nbt.putBoolean("enabled", this.enabled);
        nbt.putInt("timerInterval", this.timerInterval);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.timer = nbt.getInt("timer");
        this.countDown = nbt.getInt("countDown");
        this.enabled = nbt.getBoolean("enabled");
        this.timerInterval = nbt.getInt("timerInterval");
    }

    @Override
    public LevelCapabilityStatusPacket createUpdatePacket() {
        // Make sure to register this update packet to your network channel!
        return new SimpleLevelCapabilityStatusPacket(GlobalCapAttacher.EXAMPLE_GLOBAL_LEVEL_CAPABILITY_RL, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        // Return the network channel here
        return NetworkHandler.INSTANCE;
    }
}
