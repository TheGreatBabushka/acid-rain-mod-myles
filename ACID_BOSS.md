# Acid Boss

## Overview
The Acid Boss is a hostile mob added to the Acid Rain Mod. It's a large, slime-like creature with a distinctive big mouth filled with sharp teeth. This boss enemy provides a challenging encounter with multiple attack patterns.

## Features

### Appearance
- Large green slime-like body (2x2 blocks in size)
- Visible mouth with teeth that opens during attacks
- Translucent green appearance with darker spots
- Animated squashing motion like a slime

### Stats
- **Health**: 100 HP (50 hearts)
- **Attack Damage**: 8 HP (4 hearts) melee
- **Movement Speed**: 0.25 (slower than player)
- **Knockback Resistance**: 0.8 (very resistant)
- **Follow Range**: 35 blocks
- **Experience**: 50 XP on death

### Attack Patterns

1. **Melee Attack**
   - Basic bite attack at close range
   - Deals 8 damage
   - Opens mouth during attack animation

2. **Acid Projectile**
   - Shoots acid projectiles at medium to long range (3-20 blocks)
   - Deals 6 damage on hit
   - Sets target on fire for 5 seconds
   - 3-second cooldown between shots
   - Opens mouth when shooting

3. **Eat Attack** (Special Ability)
   - Can "eat" players when they get too close (within 3 blocks)
   - Player is teleported inside the boss and trapped for 5 seconds
   - Deals 6 initial damage plus continuous positional restriction
   - Player is spit out after 5 seconds with additional knockback and 4 damage
   - 20-second cooldown between eat attempts
   - Only affects survival/adventure mode players

## Spawning

### Spawn Egg
The Acid Boss can be spawned using the **Acid Boss Spawn Egg**:
- Found in the Spawn Eggs creative tab
- Bright green egg with darker green spots
- Right-click on a block to spawn

### Natural Spawning
Currently uses standard monster spawn rules:
- Spawns on solid ground
- Light level requirements same as other monsters
- Can spawn in any dimension where monsters spawn

## AI Behavior
- Actively seeks out and attacks players
- Floats in water
- Avoids water when pathfinding
- Retaliates when attacked
- Maintains line of sight for ranged attacks

## Sounds
Uses slime sounds for consistency:
- Squishing sound when moving
- Slime hurt sound when damaged
- Slime death sound on defeat
- Ghast shoot sound for projectiles

## Technical Details

### Entity ID
`acidrain:acid_boss`

### Item ID
`acidrain:acid_boss_spawn_egg`

### Files
- Entity: `AcidBossEntity.java`
- Projectile: `AcidProjectileEntity.java`
- Model: `AcidBossModel.java`
- Renderer: `AcidBossRenderer.java`
- Textures: 
  - `assets/acidrain/textures/entity/acid_boss.png`
  - `assets/acidrain/textures/item/acid_boss_spawn_egg.png`

## Future Enhancements
Potential improvements that could be added:
- Custom spawn locations/structures
- Loot table with unique drops
- Boss health bar display
- Custom sounds
- Particles during eat attack
- Multiple size variants
- Summon smaller acid slimes
