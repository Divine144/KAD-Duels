package com.divinity.duels.event;

import com.divinity.duels.Duels;
import com.divinity.duels.cap.GlobalCapAttacher;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Duels.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("start")
                .then(Commands.literal("game")
                        .executes(context -> {
                            var holder = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(context.getSource().getLevel());
                            if (holder != null) {
                                holder.setTimer(20);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
        dispatcher.register(Commands.literal("hearts")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(context -> {
                                    Player player = EntityArgument.getPlayer(context, "player");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                    var attribute = player.getAttribute(Attributes.MAX_HEALTH);
                                    if (attribute != null) {
                                        attribute.setBaseValue(amount);
                                        player.hurt(DamageSource.GENERIC, 0.1f);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
        dispatcher.register(Commands.literal("resume")
                .executes(context -> {
                    var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(context.getSource().getLevel());
                    if (cap != null) {
                        cap.startTimer();
                        cap.startCountdown();
                        cap.setEnabled();
                    }
                    return Command.SINGLE_SUCCESS;
                })
        );
        dispatcher.register(Commands.literal("game")
                .then(Commands.literal("interval")
                        .then(Commands.argument("seconds", IntegerArgumentType.integer())
                                .executes(context -> {
                                    var holder = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(context.getSource().getLevel());
                                    if (holder != null) {
                                        holder.setTimerInterval(IntegerArgumentType.getInteger(context, "seconds"));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level instanceof ServerLevel level && event.phase == TickEvent.Phase.END) {
            var cap = GlobalCapAttacher.getGlobalLevelCapabilityUnwrap(level);
            if (cap != null && !cap.isEnabled()) {
                if (cap.getAndLowerTimer() == 0) {
                    if (cap.getCountDown() >= 0) {
                        if (cap.getCountDown() % 20 == 0) {
                            sendSystemMessage(level, "" + (cap.getCountDown() / 20), ChatFormatting.RED);
                        }
                        if (level.dimension() == Level.OVERWORLD) { // Overworld should always be loaded
                            cap.setCountDown(cap.getCountDown() - 1);
                            if (cap.getCountDown() == 0) {
                                cap.startTimer();
                                cap.startCountdown();
                                cap.setEnabled();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void sendSystemMessage(ServerLevel level, String fakeText, ChatFormatting textStyle) {
        level.players().forEach(pl -> pl.displayClientMessage(Component.literal("%s".formatted(fakeText)).withStyle(textStyle), true));
    }
}
