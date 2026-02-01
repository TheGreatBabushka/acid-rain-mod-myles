# Acid Rain Mod - Quick Start Guide

## Installation

1. Make sure you have Minecraft 1.20.1 and Forge 47.1.0 installed
2. Download the mod JAR file from the releases page
3. Place the JAR file in your `mods` folder
4. Launch Minecraft with Forge

## Getting Started

### Prerequisites
- You need to be an operator (OP) on the server or have permission level 2
- Use `/op <your_username>` to give yourself operator status (on single player or if you're the server admin)

### Basic Commands

Start acid rain:
```
/acidrain start
```

Stop acid rain:
```
/acidrain stop
```

Toggle acid rain on/off:
```
/acidrain toggle true
/acidrain toggle false
```

## What to Expect

### Visual Effects
- When acid rain is active, you'll notice a greenish tint to the atmosphere
- The effect is most visible during thunderstorms
- Regular rain particles will appear, but the overall environment will look more sinister

### Gameplay Effects

**Player Damage:**
- If you're exposed to the sky during acid rain, you'll take damage
- Damage: 0.5 hearts every second
- Taking shelter under ANY block will protect you
- Even a single block above your head is enough to keep you safe

**Block Dissolution:**
- Random blocks exposed to the sky will slowly dissolve
- You'll see blocks disappearing over time
- Most blocks can be dissolved, but some are protected:
  - Bedrock
  - Command blocks
  - Barriers
  - Structure blocks
  - Jigsaw blocks
  - End portal frames

## Survival Tips

1. **Build Shelters**: Always have a roof over your head during acid rain
2. **Underground is Safe**: Mining or being in caves protects you completely
3. **Protect Your Builds**: Important structures should have roofs or be protected from the sky
4. **Watch the Sky**: If you see the greenish tint, find shelter immediately
5. **Time It Right**: Plan outdoor activities for when acid rain is not active

## Multiplayer Considerations

- Only server operators can control acid rain
- Acid rain affects all players in the dimension where it's active
- Different dimensions (Overworld, Nether, End) can have independent acid rain states
- Coordinate with other players to warn them when starting acid rain

## Troubleshooting

**Commands not working?**
- Make sure you have operator permissions
- Check that you're using the correct command syntax
- Look for error messages in chat

**Not taking damage?**
- Check if you're truly exposed to the sky (use F3 to see if you can see sky)
- Make sure acid rain is actually active (look for greenish tint)
- Verify the mod is installed correctly

**Blocks not dissolving?**
- This is a random effect and may take time to notice
- Check that blocks are exposed to the sky
- Some blocks are protected and won't dissolve

## Configuration

Currently, the mod does not have a configuration file. To adjust parameters like damage amount or dissolution rate, you'll need to modify the source code and rebuild the mod.

Default values:
- Damage: 1.0 (0.5 hearts) every 20 ticks (1 second)
- Block checks: 5 random blocks per second
- Dissolution chance: 10% per check

## For Server Administrators

### Recommended Server Settings
- Consider limiting who has access to `/acidrain` commands
- Set up protected areas where important builds are safe
- Communicate with players before enabling acid rain
- Consider setting up a schedule for acid rain events

### Performance Notes
- The mod is designed to be lightweight
- Block dissolution checks are spread out over time
- Only loaded chunks are affected
- No significant performance impact on well-configured servers

## Advanced Usage

### Automatic Events
While the mod currently requires manual activation, you could use command blocks or other mods to:
- Schedule periodic acid rain events
- Trigger acid rain based on in-game conditions
- Create custom challenges or mini-games

### Integration Ideas
- Use with adventure maps for added challenge
- Combine with other weather mods for variety
- Create custom scenarios with command blocks

## Support

For bugs, feature requests, or questions:
- Open an issue on the GitHub repository
- Include your Minecraft version, Forge version, and mod version
- Describe what you expected vs. what happened
- Include any error messages or logs

## Credits

- **Mod Author**: Myles
- **Mod ID**: acidrain
- **Version**: 1.0.0
- **Minecraft Version**: 1.20.1
- **Forge Version**: 47.1.0

Enjoy the acid rain! Stay safe and keep a roof over your head! ☔🟢
