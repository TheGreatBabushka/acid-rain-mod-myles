# acid-rain-mod-myles
A Minecraft mod that introduces acid rain and the acid bucket!

## Features

### Acid Rain Weather System

This mod adds a dangerous acid rain weather effect to Minecraft with the following features:

- **Command-Controlled Weather**: Summon acid rain using simple commands
- **Player Damage**: Players exposed to acid rain (not under blocks) will take damage over time
- **Block Dissolution**: Acid rain randomly dissolves blocks that are exposed to the sky
- **Greenish Visual Effect**: The rain has a distinctive greenish tint when active

## Commands

All commands require operator permissions (OP level 2).

### `/acidrain start`
Starts acid rain in the current dimension. If it's not currently raining, this command will also start regular rain and enable the acid rain effect.

### `/acidrain stop`
Stops acid rain and clears the weather entirely in the current dimension.

### `/acidrain toggle <true|false>`
Toggle acid rain on or off. When set to `true`, it will start raining if not already raining. When set to `false`, it disables the acid rain effect but may leave regular rain active.

## Gameplay Mechanics

### Player Damage
- Players take **0.5 hearts (1.0 damage)** every second when exposed to acid rain
- Damage only occurs when the player can see the sky (not under any blocks)
- Taking shelter under any solid block will protect you from the acid rain

### Block Dissolution
- Blocks exposed to the sky have a chance to be dissolved (destroyed) by acid rain
- Dissolution occurs randomly across the world
- Protected blocks (bedrock, command blocks, barriers, etc.) cannot be dissolved
- Dissolved blocks do not drop items - they are completely destroyed

### Visual Effects
- When acid rain is active, the fog and atmosphere take on a greenish tint
- The effect is most noticeable during thunderstorms when acid rain is enabled

## Technical Details

### Implementation
- **AcidRainEvent**: Handles player damage when exposed to acid rain
- **AcidRainBlockDissolver**: Manages random block dissolution mechanics
- **AcidRainCommand**: Provides command interface for controlling acid rain
- **AcidRainState**: Tracks acid rain state per dimension
- **AcidRainRenderer**: Adds greenish visual effects on the client side

### Configuration
The following parameters can be modified in the source code if needed:
- Damage interval: 20 ticks (1 second)
- Damage amount: 1.0 (half a heart)
- Block check interval: 20 ticks
- Blocks checked per interval: 5
- Dissolution chance: 10% per check

## Mod Scaffold Structure

This repository contains a complete Minecraft Forge mod scaffold for Minecraft 1.20.1.

### Structure

- `src/main/java/` - Java source code for the mod
- `src/main/resources/` - Mod resources (textures, models, sounds, data)
- `build.gradle` - Gradle build configuration
- `gradle.properties` - Mod and build properties
- `settings.gradle` - Gradle settings

### Building the Mod

To build the mod, you need:
- Java 17 or higher
- Internet connection for downloading dependencies

Run:
```bash
./gradlew build
```

The built mod jar will be in `build/libs/`

### Development

Open this project in your IDE (IntelliJ IDEA or Eclipse) and import it as a Gradle project.

#### Running the Mod

- Client: `./gradlew runClient`
- Server: `./gradlew runServer`
- Data Generation: `./gradlew runData`

### Mod Information

- **Mod ID**: acidrain
- **Version**: 1.0.0
- **Minecraft Version**: 1.20.1
- **Forge Version**: 47.1.0
- **Author**: Myles

## License

All Rights Reserved

## Contributing

Feel free to submit issues and pull requests!
