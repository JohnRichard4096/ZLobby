# ZLOBBY 插件

## 目录
- [简介](#简介)
- [插件内容](#插件内容)
- [插件配置文件](#插件配置文件)
- [Java版本支持](#java版本支持)
- [常见Q&A](#常见qa)
- [协议](#协议)
- [构建方式](#构建方式)

## 简介
ZLOBBY 是一个专为**群组服务器**设计的 Minecraft 插件，旨在提供一个友好的游戏大厅体验。该插件包括玩家加入时的处理、传送位置设置、防止破坏方块和放置方块等功能，并提供了详细的命令和权限控制。

## 安装
从Release下载插件，解压到服务器的插件目录中。


## 插件内容
### 命令
| 命令 | 描述 | 权限 |
| --- | --- | --- |
| `/zlobby` | 主命令，显示插件信息 | `zlobby.main` |
| `/zlobby reload` | 重新加载配置文件 | `zlobby.main.reload` |
| `/zlobby info` | 显示插件详细信息 | `zlobby.main.info`子节点`zlobby.main.info.more`拥有可获取更多信息 |

### 权限
| 权限 | 描述 | 默认值 |
| --- | --- | --- |
| `zlobby.main` | 允许使用主命令（不包括所有子命令） | `true` |
| `zlobby.*` | 允许使用所有命令 | `op` |
| `zlobby.main.reload` | 允许重新加载插件配置 | `op` |
| `zlobby.main.info` | 允许获取插件信息 | `true` |
| `zlobby.main.info.more` | 允许获取更多插件信息 | `op` |

## 插件配置文件
插件配置文件位于 `config.yml`，您可以根据需要进行修改。

### 主要配置项
默认配置文件

```yaml
# 插件配置
Main:
  # 是否启用调试选项
  debug: false
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
A: 在 `config.yml` 文件中配置 `onPlayerJoin` 部分，将 `enable` 设置为 `true`，并根据需要启用欢迎消息、游戏模式更改等功能。

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
  