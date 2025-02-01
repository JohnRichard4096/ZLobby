# ZLOBBY Lobby plugin

## Before start: This readme is only for the LATEST release,please view [Release](https://github.com/JohnRichard4096/ZLobby/releases) page for more information!

<hr />

### Links

[![Jenkins](https://img.shields.io/badge/jenkins-%232C5263.svg?style=for-the-badge&logo=jenkins&logoColor=white)](https://jenkins.micro-wave.cc/job/ZLobby/)
[![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)](https://www.jetbrains.com.cn/idea/)
[![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/cn/java/technologies/downloads/#java21)
[![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/JohnRichard4096/ZLobby/actions)
[![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)](https://github.com/JohnRichard4096/ZLobby)
[![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)](https://git-scm.com)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)](https://maven.apache.org)

<hr />

## Table of Contents
- [Introduction](#Introduction)
- [Contents](#Contents)
- [Config files](#Configs)
- [Supported Java versions](#JavaVersion)
- [Q&A](#QA)
- [LICENSE](#LICENSE)
- [How to Build](#Build)

## Introduction
ZLOBBY is a Minecraft plugin designed for a **group server** lobby (1.20.5+), aimed at providing a friendly lobby experience. The plugin includes features such as player join handling, teleportation point settings, block protection (preventing block breaking and placing), and detailed command and permission controls.

## Installation
Download the plugin from the Release section and unzip it into the server's plugin directory.

Game version requirements (Plugin version >= 1.2):
- (Spigot/Paper and its forks) 1.20.5+

Game version requirements (Plugin version < 1.2):
- (Spigot/Paper and its forks) 1.21+



## Contents
### Commands
| Command          | Description                               | Permission                                                                                 |
|------------------|-------------------------------------------|--------------------------------------------------------------------------------------------|
| `/zlobby`        | Main command, displays plugin information | `zlobby.main`                                                                              |
| `/zlobby reload` | Reload configuration file                 | `zlobby.main.reload`                                                                       |
| `/zlobby info`   | Display detailed plugin information       | `zlobby.main.info` sub-node `zlobby.main.info.more` has permission to get more information |

### Permissions

<details><summary>More</summary>

| Permission Node             | Description                                                                   | Default   |
|-----------------------------|-------------------------------------------------------------------------------|-----------|
| `zlobby.main`               | Allows using some commands (not all subcommands)                              | `Default` |
| `zlobby.main.*`             | Allows using all main commands                                                | `None`    |
| `zlobby.*`                  | Has all permissions                                                           | `None`    |
| `zlobby.main.reload`        | Allows reloading ZLobby                                                       | `op`      |
| `zlobby.main.info`          | Allows getting ZLobby information                                             | `Default` |
| `zlobby.main.info.more`     | Allows getting more ZLobby information                                        | `op`      |
| `zlobby.lobby.*`            | Provides admin-like permissions in ZLobby                                     | `None`    |
| `zlobby.lobby.noChangeMode` | Mode is not changed                                                           | `None`    |
| `zlobby.lobby.break`        | Allows breaking blocks                                                        | `op`      |
| `zlobby.lobby.place`        | Allows placing blocks                                                         | `op`      |
| `zlobby.lobby.neverKick`    | Never kicked from the server for block operations                             | `None`    |
| `zlobby.lobby.tp`           | Allows teleporting to specified locations                                     | `Default` |
| `zlobby.lobby.feed`         | Allows feeding players                                                        | `Default` |
| `zlobby.lobby.health`       | Allows healing player health                                                  | `Default` |
| `zlobby.lobby.message`      | Players receive welcome messages                                              | `Default` |
| `zlobby.effect`             | Whether visual effects set in the config file are generated when players join | `Default` |

</details>

#### **Specials**
| Permission Node | Description                               | Default   |
|-----------------|-------------------------------------------|-----------|
| `zlobby.tp`     | Allows teleporting to specified locations | `Non-OP`  |
| `zlobby.feed`   | Allows feeding players                    | `Default` |
| `zlobby.health` | Allows healing player health              | `Default` |

These nodes have been moved to the `zlobby.lobby.` nodes starting from version 1.2. Please update them accordingly. These nodes will no longer be in effect.

## Configs
All config files are located in the `ZLOBBY` folder.

### Main config
main config file `config.yml`

<details><summary>More</summary>

```yaml
# Plugin Configuration
# Teleport location when a player joins the server or falls into the void
teleportLocation:
  # Whether to enable
  enable: false
  # Teleport location coordinates
  x: 0.0
  y: 0.0
  z: 0.0
  # Orientation
  yaw: 0.0
  pitch: 0.0
# Other matters when a player joins
onPlayerJoin:
  # Whether to enable player join handling
  enable: false
  # Game mode to change when a player joins the game
  changeGameMode:
    # Whether to enable
    enable: false
    # Game mode, can be: survival, creative, adventure, spectator
    gameMode: "adventure"
  # Welcome message
  welcomeMessage:
    # Whether to enable
    enable: false
    # Server name
    serverName: "Server"
    # Message (supports using & to represent colors, variable {player} represents the player, {server} represents the server name)
    message: "Welcome {player} to {server}"
Lobby:
  # Whether to enable
  enable: false
  # World to teleport players to when they join the game
  world: "world"
  # Whether to prevent players from breaking blocks
  avoidBlockBreak: true
  # Whether to prevent players from placing blocks
  avoidBlockPlace: true
  # Whether to kick players after multiple attempts to interact with blocks
  toKick: true
  # Number of times a player can attempt to interact with blocks
  tryTimes: 5
  # Whether to cancel player damage
  cancelHurt: true
  # Whether to fully feed and heal players
  feedPlayer: true

```

</details>

`onJoin.yml`

<details><summary>More</summary>

```yaml
# Extension of the player join feature, use & to represent colors; variables {player} represents the player, {server} represents the server name in config.yml
onJoin:
  title:
    # Whether to enable title
    enable: false
    # Title
    title: "Welcome {player}"
    # Subtitle
    subtitle: "to {server}"
    # Display time
    time: 5
  playSound:
    # Whether to enable sound effects
    enable: false
    # List of sounds to play, ID can be referenced from https://minecraft.wiki/w/Sounds.json for Java edition
    sound:
      - "entity.experience_orb.pickup"
  firework:
    # Whether to enable fireworks
    enable: false
    # List of fireworks
    fireworks:
      -
        # Type BALL, BALL_LARGE, STAR, BURST, CREEPER
        type: BALL_LARGE
        # Color using Bukkit's DyeColor
        color: RED
        power: 3

```

</details>

`worldSetting.yml`

<details><summary>More</summary>

```yaml
# Global world rules settings. Rule settings have lower priority than individual world settings and override the rules of worlds that do not have separate settings.
global:
  # Global switch, when disabled all world rules become ineffective
  enable: false
  # PVP switch
  pvp: false
  # Mob spawning switch
  mobSpawn: false
  # Fire burning switch
  fireTick: false
  # Weather change switch
  weatherChange: false
  # Difficulty
  difficulty: PEACEFUL
  # Whether time cycles
  daylightCycle: false
  # Keep inventory
  keepInventory: true

# Specific world rule settings
worlds:
  -
    # Whether enabled
    enable: false
    # World name
    world: "world"
    # Rules, same as above
    pvp: false
    mobSpawn: false
    fireTick: false
    weatherChange: false
    difficulty: PEACEFUL
    daylightCycle: false
    keepInventory: true


```

</details>

`lang.yml`

<details><summary>More</summary>

```yaml
# set language
language:
  # Language,available: zh_CN, en_US
  lang: "en_US"
```

</details>

## JavaVersion
This plugin only supports `Java 21` or **higher**.

## QA

### Q: How do I reload the configuration file?
A: Use the command `/zlobby reload`. The `zlobby.main.reload` permission is required.

### Q: How do I view detailed plugin information?
A: Use the command `/zlobby info`. The `zlobby.main.info.more` permission is required.

### Q: How do I set up the teleport location?
A: Configure the `teleportLocation` section in the `config.yml` file. Set `enable` to `true` and fill in the corresponding coordinates and facing direction.

### Q: How do I enable player join processing?
A: Configure the `onPlayerJoin` section in the `config.yml` file. Set `enable` to `true` and enable features such as welcome messages and game mode changes as needed. Additional extended functionalities can be found in `onJoin.yml`.

### Q: How do I enable the lobby feature?
A: Configure the `Lobby` section in the `config.yml` file. Set `enable` to `true` and enable features such as preventing block destruction and block placement as needed.

## LICENSE
We use the GPL 3.0 license.

## Build
### Use Maven
1. **Clone project**:
    ```bash
    git clone https://github.com/your-repo/ZLOBBY.git 
    cd ZLOBBY
    ```
2. **Use maven to package**:
   ```bash
   mvn clean package
   ```
Then you can find the jar file in the `target` directory.
  