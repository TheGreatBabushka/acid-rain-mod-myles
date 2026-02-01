# acid-rain-mod-myles
A Minecraft mod that introduces acid rain and the acid bucket!

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
