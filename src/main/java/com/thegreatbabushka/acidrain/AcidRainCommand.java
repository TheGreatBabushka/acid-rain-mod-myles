package com.thegreatbabushka.acidrain;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class AcidRainCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("acidrain")
                .requires(source -> source.hasPermission(2)) // Requires OP level 2
                .then(Commands.literal("start")
                    .executes(context -> startAcidRain(context.getSource()))
                )
                .then(Commands.literal("stop")
                    .executes(context -> stopAcidRain(context.getSource()))
                )
                .then(Commands.literal("toggle")
                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                        .executes(context -> toggleAcidRain(
                            context.getSource(), 
                            BoolArgumentType.getBool(context, "enabled")
                        ))
                    )
                )
        );
    }

    private static int startAcidRain(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        
        // Enable rain if not already raining
        if (!level.isRaining()) {
            level.setWeatherParameters(0, 6000, true, true); // Clear time, rain time, raining, thundering
        }
        
        // Enable acid rain
        AcidRainState.setAcidRainActive(level, true);
        
        source.sendSuccess(() -> Component.literal("Acid rain has started! Take cover!"), true);
        return 1;
    }

    private static int stopAcidRain(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        
        // Disable acid rain
        AcidRainState.setAcidRainActive(level, false);
        
        // Optionally stop rain completely
        level.setWeatherParameters(6000, 0, false, false);
        
        source.sendSuccess(() -> Component.literal("Acid rain has stopped."), true);
        return 1;
    }

    private static int toggleAcidRain(CommandSourceStack source, boolean enabled) {
        ServerLevel level = source.getLevel();
        
        if (enabled) {
            // Enable rain if not already raining
            if (!level.isRaining()) {
                level.setWeatherParameters(0, 6000, true, true);
            }
            AcidRainState.setAcidRainActive(level, true);
            source.sendSuccess(() -> Component.literal("Acid rain enabled."), true);
        } else {
            AcidRainState.setAcidRainActive(level, false);
            source.sendSuccess(() -> Component.literal("Acid rain disabled."), true);
        }
        
        return 1;
    }
}
