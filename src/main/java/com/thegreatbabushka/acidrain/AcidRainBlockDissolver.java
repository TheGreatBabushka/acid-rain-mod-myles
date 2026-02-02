package com.thegreatbabushka.acidrain;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class AcidRainBlockDissolver {
    private static final int CHECK_INTERVAL = 20; // Check every second
    private static final int BLOCKS_PER_CHECK = 50; // Check 5 random blocks per interval
    private static final double DISSOLVE_CHANCE = 1; // 10% chance to dissolve when checked
    private static final int SEARCH_RADIUS = 32; // Search within 32 blocks of players
    
    private final RandomSource random = RandomSource.create();

    @SubscribeEvent
    public void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Level level = event.level;
        System.out.println("check");

        // Only run on server side
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        System.out.println("check 1");

        // Check if it's raining and acid rain is enabled
        if (!level.isRaining() || !AcidRainState.isAcidRainActive(serverLevel)) {
            return;
        }
        System.out.println("check 2");

        // Only check at intervals using level game time
        if (serverLevel.getGameTime() % CHECK_INTERVAL != 0) {
            return;
        }
        System.out.println("check 3");

        // Get list of players to find loaded areas
        List<ServerPlayer> players = serverLevel.players();
        if (players.isEmpty()) {
            return;
        }
        System.out.println("check 4");

        // Check random blocks for dissolution
        for (int i = 0; i < BLOCKS_PER_CHECK; i++) {
            // Get random position near a random player
            ServerPlayer randomPlayer = players.get(random.nextInt(players.size()));
            BlockPos randomPos = getRandomPositionNearPlayer(randomPlayer);
            if (randomPos != null) {
                tryDissolveBlock(serverLevel, randomPos);
            }
        }
    }

    private BlockPos getRandomPositionNearPlayer(ServerPlayer player) {
        BlockPos playerPos = player.blockPosition();
        
        // Get random position within search radius
        int x = playerPos.getX() + random.nextInt(SEARCH_RADIUS * 2) - SEARCH_RADIUS;
        int z = playerPos.getZ() + random.nextInt(SEARCH_RADIUS * 2) - SEARCH_RADIUS;
        int y = playerPos.getY() + random.nextInt(20) - 10; // +/- 10 blocks vertically
        
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
                    System.out.println("Acid rain dissolved block at " + pos);
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
