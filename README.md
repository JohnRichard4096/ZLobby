[![IMG_202412145044_64x64](https://github.com/user-attachments/assets/296cdd50-dc6d-4b38-9267-f04d184e82a2)](https://zcraft.leaflow.cn)

# ZLOBBY 大厅插件

## 前言：本README仅为最新的ZLobby Release编写，最新版本请查看[Release](https://github.com/JohnRichard4096/ZLobby/releases)！

<hr />

### 链接

[![Jenkins](https://img.shields.io/badge/jenkins-%232C5263.svg?style=for-the-badge&logo=jenkins&logoColor=white)](https://jenkins.micro-wave.cc/job/ZLobby/) 
[![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)](https://www.jetbrains.com.cn/idea/)
[![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/cn/java/technologies/downloads/#java21)
[![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/JohnRichard4096/ZLobby/actions)
[![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)](https://github.com/JohnRichard4096/ZLobby)
[![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)](https://git-scm.com)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)](https://maven.apache.org)

<hr />

## 目录
- [简介](#简介)
- [插件内容](#插件内容)
- [插件配置文件](#插件配置文件)
- [Java版本支持](#java版本支持)
- [常见Q&A](#常见qa)
- [协议](#协议)
- [构建方式](#构建方式)

## 简介
ZLOBBY 是一个专为（1.20.5+）**群组服务器**大厅设计的 Minecraft 插件，旨在提供一个友好的游戏大厅体验。该插件包括玩家加入时的处理、传送位置设置、防止破坏方块和放置方块等功能，并提供了详细的命令和权限控制。

## 安装
从Release下载插件，解压到服务器的插件目录中。


游戏版本要求（插件版本>=1.2）:
- (Spigot/Paper及其分支)1.20.5+

游戏版本要求（插件版本<1.2）:
- (Spigot/Paper及其分支)1.21+


## 插件内容
### 命令
| 命令               | 描述         | 权限                                                    |
|------------------|------------|-------------------------------------------------------|
| `/zlobby`        | 主命令，显示插件信息 | `zlobby.main`                                         |
| `/zlobby reload` | 重新加载配置文件   | `zlobby.main.reload`                                  |
| `/zlobby info`   | 显示插件详细信息   | `zlobby.main.info`子节点`zlobby.main.info.more`拥有可获取更多信息 |

### 权限

<details><summary>查看详情</summary>

| 权限节点                        | 描述                     | 默认值    |
|-----------------------------|------------------------|--------|
| `zlobby.main`               | 允许使用部分命令（不是所有子命令）      | `默认`   |
| `zlobby.main.*`             | 允许使用所有主要命令             | `都不持有` |
| `zlobby.*`                  | 拥有所有权限                 | `都不持有` |
| `zlobby.main.reload`        | 允许重新加载 ZLobby          | `op`   |
| `zlobby.main.info`          | 允许获取 ZLobby 信息         | `默认`   |
| `zlobby.main.info.more`     | 允许获取更多 ZLobby 信息       | `op`   |
| `zlobby.lobby.*`            | 提供类似管理员的权限在 ZLobby 中   | `都不持有` |
| `zlobby.lobby.noChangeMode` | 模式不被更改                 | `都不持有` |
| `zlobby.lobby.break`        | 允许破坏方块                 | `op`   |
| `zlobby.lobby.place`        | 允许放置方块                 | `op`   |
| `zlobby.lobby.neverKick`    | 永远不会因为操作方块踢出服务器        | `都不持有` |
| `zlobby.lobby.tp`           | 允许传送到指定位置              | `默认`   |
| `zlobby.lobby.feed`         | 允许喂饱玩家                 | `默认`   |
| `zlobby.lobby.health`       | 允许治疗玩家生命值              | `默认`   |
| `zlobby.lobby.message`      | 玩家会收到欢迎消息              | `默认`   |
| `zlobby.effect`             | 当玩家加入时是否产生配置文件设置好的视觉效果 | `默认`   |

</details>

#### **特别说明**
| 权限节点            | 描述         | 默认值   |
|-----------------|------------|-------|
| `zlobby.tp`     | 允许传送到指定位置  | `非OP` |
| `zlobby.feed`   | 允许喂饱玩家     | `默认`  |
| `zlobby.health` | 允许治疗玩家生命值） | `默认`  |

这些节点从1.2开始全部移动到了zlobby.lobby.节点，请自行修改，这些节点将不再起作用

## 插件配置文件
插件配置文件位于 `config.yml`，您可以根据需要进行修改。

### 主要配置项
默认配置文件`config.yml`

<details><summary>查看详情</summary>

```yaml
# 插件配置
#当玩家进入服务器时的传送位置/掉入虚空时传送到安全位置
teleportLocation:
   # 是否启用
   enable: false
   # 传送位置
   x: 0.0
   y: 0.0
   z: 0.0
   # 朝向
   yaw: 0.0
   pitch: 0.0
# 当玩家加入时的其他事项
onPlayerJoin:
   # 是否启用玩家加入时的处理
   enable: false
   # 在玩家加入游戏时改变的游戏模式
   changeGameMode:
      # 是否启用
      enable: false
      # 游戏模式，可写：survival, creative, adventure, spectator
      gameMode: "adventure"
   # 欢迎消息
   welcomeMessage:
      # 是否启用
      enable: false
      # 服务器名称
      serverName: "服务器"
      # 消息（支持使用&来表示颜色，变量{player}表示玩家，{server}表示服务器名称）
      message: "欢迎{player}来到{server}"
Lobby:
   # 是否启用
   enable: false
   # 玩家进入游戏时传送到哪个世界
   world: "world"
   # 是否防止玩家破坏方块
   avoidBlockBreak: true
   # 是否防止玩家放置方块
   avoidBlockPlace: true
   # 是否在玩家多次尝试操作方块时踢出
   toKick: true
   # 玩家尝试操作方块的次数
   tryTimes: 5
   # 是否取消玩家被伤害
   cancelHurt: true
   # 是否给玩家补充满饥饿值与血量
   feedPlayer: true
```

</details>

`onJoin.yml`

<details><summary>查看详情</summary>

```yaml
# 玩家加入功能的扩展，可使用&表示颜色；变量{player}表示玩家，{server}表示config.yml的服务器名称
onJoin:
  title:
    # 是否启用标题
    enable: false
    # 标题
    title: "Welcome {player}"
    # 副标题
    subtitle: "to {server}"
    # 显示时间
    time: 5
  playSound:
    # 是否启用音效
    enable: false
    # 要播放的音效列表，ID可以参考https://zh.minecraft.wiki/w/Sounds.json的Java版内容
    sound:
      - "entity.experience_orb.pickup"
  firework:
    # 是否启用烟花
    enable: false
    # 烟花列表
    fireworks:
      -
        # 类型 BALL, BALL_LARGE, STAR, BURST, CREEPER
        type: BALL_LARGE
        # 颜色 使用Bukkit的DyeColor
        color: RED
        power: 3
```

</details>

`worldSetting.yml`

<details><summary>查看详情</summary>

```yaml
# 全局世界规则设置，规则设置权重低于单个世界设置，覆盖没有单独设置的世界的规则
global:
# 全局开关，关闭后所有世界规则都失效
enable: false
# pvp开关
pvp: false
# 怪物生成开关
mobSpawn: false
# 火焰燃烧开关
fireTick: false
# 天气变化开关
weatherChange: false
# 难度
difficulty: PEACEFUL
# 时间是否循环
daylightCycle: false
# 保留物品栏
keepInventory: true
# 特定世界规则设置
worlds:
-
# 是否启用
enable: false
# 世界名
world: "world"
# 规则，同上
pvp: false
mobSpawn: false
fireTick: false
weatherChange: false
difficulty: PEACEFUL
daylightCycle: false
keepInventory: true

```

</details>

## Java版本支持
本插件支持 `Java 21+` 版本（classfile version 65.0）。

## 常见Q&A
### Q: 如何重新加载配置文件？
A: 使用命令 `/zlobby reload`，需要 `zlobby.main.reload` 权限。

### Q: 如何查看插件详细信息？
A: 使用命令 `/zlobby info`，需要 `zlobby.main.info.more` 权限。

### Q: 如何设置传送位置？
A: 在 `config.yml` 文件中配置 `teleportLocation` 部分，将 `enable` 设置为 `true`，并填写相应的坐标和朝向。

### Q: 如何启用玩家加入时的处理？
A: 在 `config.yml` 文件中配置 `onPlayerJoin` 部分，将 `enable` 设置为 `true`，并根据需要启用欢迎消息、游戏模式更改等功能，在`onJoin.yml`中可以获得更多扩展的功能。

### Q: 如何启用游戏大厅功能？
A: 在 `config.yml` 文件中配置 `Lobby` 部分，将 `enable` 设置为 `true`，并根据需要启用防止破坏方块、防止放置方块等功能。

## 协议
GPL V3协议，请在在协议允许下操作源码。

## 构建方式
### 使用 Maven 构建
1. **克隆项目仓库**:
    ```bash
    git clone https://github.com/your-repo/ZLOBBY.git 
    cd ZLOBBY
    ```
2. **安装依赖并打包**:
   ```bash
   mvn clean package
   ```
   打包完成后，您可以在 `target` 文件夹中找到生成的 `.jar` 文件。
  