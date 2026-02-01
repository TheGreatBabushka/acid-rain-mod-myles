package com.thegreatbabushka.acidrain;

import net.minecraft.server.level.ServerLevel;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the acid rain state per dimension
 */
public class AcidRainState {
    private static final Map<UUID, Boolean> dimensionAcidRainState = new HashMap<>();

    public static boolean isAcidRainActive(ServerLevel level) {
        UUID dimensionId = level.dimension().location().hashCode() != 0 
            ? UUID.nameUUIDFromBytes(level.dimension().location().toString().getBytes())
            : UUID.randomUUID();
        return dimensionAcidRainState.getOrDefault(dimensionId, false);
    }

    public static void setAcidRainActive(ServerLevel level, boolean active) {
        UUID dimensionId = UUID.nameUUIDFromBytes(level.dimension().location().toString().getBytes());
        dimensionAcidRainState.put(dimensionId, active);
    }

    public static void clearAcidRain(ServerLevel level) {
        UUID dimensionId = UUID.nameUUIDFromBytes(level.dimension().location().toString().getBytes());
        dimensionAcidRainState.remove(dimensionId);
    }
}
