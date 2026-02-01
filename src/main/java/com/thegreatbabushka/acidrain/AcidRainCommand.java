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
        
        // Weather parameters: clearWeatherTime, rainTime, isRaining, isThundering
        final int CLEAR_WEATHER_TIME = 0;
        final int RAIN_DURATION = 6000;
        final boolean IS_RAINING = true;
        final boolean IS_THUNDERING = true;
        
        // Enable rain if not already raining
        if (!level.isRaining()) {
            level.setWeatherParameters(CLEAR_WEATHER_TIME, RAIN_DURATION, IS_RAINING, IS_THUNDERING);
        }
        
        // Enable acid rain
        AcidRainState.setAcidRainActive(level, true);
        
        source.sendSuccess(() -> Component.literal("Acid rain has started! Take cover!"), true);
        return 1;
    }

    private static int stopAcidRain(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        
        // Weather parameters: clearWeatherTime, rainTime, isRaining, isThundering
        final int CLEAR_WEATHER_DURATION = 6000;
        final int RAIN_TIME = 0;
        final boolean IS_RAINING = false;
        final boolean IS_THUNDERING = false;
        
        // Disable acid rain
        AcidRainState.setAcidRainActive(level, false);
        
        // Stop rain completely
        level.setWeatherParameters(CLEAR_WEATHER_DURATION, RAIN_TIME, IS_RAINING, IS_THUNDERING);
        
        source.sendSuccess(() -> Component.literal("Acid rain has stopped."), true);
        return 1;
    }

    private static int toggleAcidRain(CommandSourceStack source, boolean enabled) {
        ServerLevel level = source.getLevel();
        
        if (enabled) {
            // Weather parameters for starting rain
            final int CLEAR_WEATHER_TIME = 0;
            final int RAIN_DURATION = 6000;
            final boolean IS_RAINING = true;
            final boolean IS_THUNDERING = true;
            
            // Enable rain if not already raining
            if (!level.isRaining()) {
                level.setWeatherParameters(CLEAR_WEATHER_TIME, RAIN_DURATION, IS_RAINING, IS_THUNDERING);
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
