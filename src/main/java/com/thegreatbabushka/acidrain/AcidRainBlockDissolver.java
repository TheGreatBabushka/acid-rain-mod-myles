package com.thegreatbabushka.acidrain;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class AcidRainBlockDissolver {
    private static final int CHECK_INTERVAL = 20; // Check every second
    private static final int BLOCKS_PER_CHECK = 5; // Check 5 random blocks per interval
    private static final double DISSOLVE_CHANCE = 0.1; // 10% chance to dissolve when checked
    private static final int DISSOLVE_RADIUS = 16; // Check within 16 blocks of random positions
    
    private int tickCounter = 0;
    private final Random random = new Random();

    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Level level = event.level;
        
        // Only run on server side
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // Check if it's raining and acid rain is enabled
        if (!level.isRaining() || !AcidRainState.isAcidRainActive(serverLevel)) {
            return;
        }

        // Only check at intervals
        tickCounter++;
        if (tickCounter < CHECK_INTERVAL) {
            return;
        }
        tickCounter = 0;

        // Check random blocks for dissolution
        for (int i = 0; i < BLOCKS_PER_CHECK; i++) {
            // Get random position in loaded chunks
            BlockPos randomPos = getRandomLoadedPosition(serverLevel);
            if (randomPos != null) {
                tryDissolveBlock(serverLevel, randomPos);
            }
        }
    }

    private BlockPos getRandomLoadedPosition(ServerLevel level) {
        // Get a random loaded chunk position
        var loadedChunks = level.getChunkSource().chunkMap.getChunks();
        if (loadedChunks.isEmpty()) {
            return null;
        }

        var chunkArray = loadedChunks.toArray();
        var randomChunk = chunkArray[random.nextInt(chunkArray.length)];
        
        // Get random position in chunk
        int x = randomChunk.getPos().getMinBlockX() + random.nextInt(16);
        int z = randomChunk.getPos().getMinBlockZ() + random.nextInt(16);
        int y = level.getHeight() - 1;
        
        return new BlockPos(x, y, z);
    }

    private void tryDissolveBlock(ServerLevel level, BlockPos startPos) {
        // Find the top block that's exposed to sky
        BlockPos.MutableBlockPos pos = startPos.mutable();
        
        // Move down to find the first solid block
        for (int y = startPos.getY(); y > level.getMinBuildHeight(); y--) {
            pos.setY(y);
            BlockState state = level.getBlockState(pos);
            
            if (!state.isAir() && level.canSeeSky(pos.above())) {
                // This block is exposed to acid rain
                if (random.nextDouble() < DISSOLVE_CHANCE && canDissolve(state)) {
                    // Dissolve the block
                    level.destroyBlock(pos, false); // false = no drops
                }
                break;
            }
        }
    }

    private boolean canDissolve(BlockState state) {
        // Don't dissolve bedrock, end portal frames, barriers, etc.
        if (state.is(Blocks.BEDROCK) || 
            state.is(Blocks.END_PORTAL_FRAME) ||
            state.is(Blocks.BARRIER) ||
            state.is(Blocks.COMMAND_BLOCK) ||
            state.is(Blocks.CHAIN_COMMAND_BLOCK) ||
            state.is(Blocks.REPEATING_COMMAND_BLOCK) ||
            state.is(Blocks.STRUCTURE_BLOCK) ||
            state.is(Blocks.JIGSAW)) {
            return false;
        }
        
        // Allow most other blocks to be dissolved
        return true;
    }
}
