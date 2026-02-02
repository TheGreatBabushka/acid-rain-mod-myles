package com.thegreatbabushka.acidrain.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class AcidRainRenderer {
    
    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.isRaining()) {
            return;
        }

        // Check if acid rain is active
        // Note: On client side, we need to sync this from server
        // For now, we'll apply green tint when it's raining and thundering
        // This will need to be synced with server state in a full implementation
        if (isClientAcidRainActive()) {
            // Add green tint to fog color when acid rain is active
            float greenModifier = 0.5f;
            event.setRed(event.getRed() * 0.6f);
            event.setGreen(event.getGreen() * 1.2f + greenModifier);
            event.setBlue(event.getBlue() * 0.6f);
        }
    }

    // This is a placeholder - in a full implementation, this should be synced from server
    private static boolean isClientAcidRainActive() {
        // For now, we'll check if it's both raining and thundering as a proxy
        ClientLevel level = Minecraft.getInstance().level;
        return level != null && level.isRaining() && level.isThundering();
    }
}

