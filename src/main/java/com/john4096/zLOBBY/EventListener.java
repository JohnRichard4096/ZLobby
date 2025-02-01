package com.john4096.zLOBBY;

import org.bukkit.*;

import java.util.*;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


public class EventListener implements Listener {
    private boolean Debug = ZLOBBY.getPlugin(ZLOBBY.class).Debug;
    private final Logger logger = ZLOBBY.getPlugin(ZLOBBY.class).getLogger();
    private Location TPL;
    private FileConfiguration config;
    private FileConfiguration onJoinConfig;
    private final Map<String, Integer> playerAttemptCounts = new HashMap<>();
    private YamlConfiguration worldSettingConfig;

    public void onEnable() {
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        this.onJoinConfig = ZLOBBY.getPlugin(ZLOBBY.class).getOnJoinConfig();
        this.worldSettingConfig = ZLOBBY.getPlugin(ZLOBBY.class).getWorldSettingConfig();
        logger.info("EventHandler loaded");
    }

    public void onMapLoading() {
        try {
            this.worldSettingConfig = ZLOBBY.getPlugin(ZLOBBY.class).getWorldSettingConfig();
            if (worldSettingConfig.getBoolean("global.enable")) {
                logger.info("Setting worlds by global setting......");
                for (World world : Bukkit.getWorlds()) {
                    world.setPVP(worldSettingConfig.getBoolean("global.pvp"));
                    world.setGameRule(GameRule.DO_FIRE_TICK, worldSettingConfig.getBoolean("global.fireTick"));
                    world.setGameRule(GameRule.DO_MOB_SPAWNING, worldSettingConfig.getBoolean("global.mobSpawn"));
                    world.setGameRule(GameRule.KEEP_INVENTORY, worldSettingConfig.getBoolean("global.keepInventory"));
                    world.setGameRule(GameRule.DO_WEATHER_CYCLE, worldSettingConfig.getBoolean("global.weatherChange"));
                    world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, worldSettingConfig.getBoolean("global.daylightCycle"));

                }
                for (Map<?, ?> WorldConf : worldSettingConfig.getMapList("worlds")) {
                    try {
                        if (WorldConf.get("world") != null) {
                            logger.info("Setting world " + WorldConf.get("world"));
                            World world = Bukkit.getWorld(WorldConf.get("world").toString());
                            boolean pvp = (Boolean) WorldConf.get("pvp");
                            boolean mobSpawn = (Boolean) WorldConf.get("mobSpawn");
                            boolean fireTick = (Boolean) WorldConf.get("fireTick");
                            boolean weatherChange = (Boolean) WorldConf.get("weatherChange");
                            String difficulty = WorldConf.get("difficulty").toString();
                            boolean daylightCycle = (Boolean) WorldConf.get("daylightCycle");
                            boolean keepInventory = (Boolean) WorldConf.get("keepInventory");
                            if (world != null) {
                                world.setGameRule(GameRule.KEEP_INVENTORY, keepInventory);
                                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, daylightCycle);
                                world.setGameRule(GameRule.DO_WEATHER_CYCLE, weatherChange);
                                world.setGameRule(GameRule.DO_MOB_SPAWNING, mobSpawn);
                                world.setGameRule(GameRule.DO_FIRE_TICK, fireTick);
                                world.setPVP(pvp);
                                world.setDifficulty(Difficulty.valueOf(difficulty.toUpperCase(Locale.ROOT)));
                            } else {
                                logger.severe("World " + WorldConf.get("world") + " not found!");
                                logger.severe("Please check your config!");
                            }
                        }
                    } catch (ClassCastException e) {
                        logger.severe("Illegal config when setting world setting!");
                        logger.warning("World setting config error!");
                        e.printStackTrace();
                    } catch (Exception e) {
                        logger.severe("Something went wrong!");
                        e.printStackTrace();
                    }

                }
                logger.info("Setting world finished!");
            }
        } catch (Exception e) {
            logger.severe("Could not set worlds!");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.onJoinConfig = ZLOBBY.getPlugin(ZLOBBY.class).getOnJoinConfig();
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        this.TPL = loadTPLocation();

        boolean enable = config.getBoolean("onPlayerJoin.enable");
        this.Debug = ZLOBBY.getPlugin(ZLOBBY.class).Debug;
        Location location = player.getLocation();

        if (!enable) {
            return;
        }
        logger.info("Player " + event.getPlayer().getName() + " joined the server");
        try {
            if (config.getBoolean("teleportLocation.enable") && player.hasPermission("zlobby.lobby.tp")) {

                logger.info("Teleporting player " + event.getPlayer().getName() + " to " + TPL.toString());
                event.getPlayer().teleport(TPL);
                Objects.requireNonNull(TPL.getWorld()).setSpawnLocation(TPL);


            }
        } catch (Exception e) {
            logger.warning("Teleportation failed!");
            logger.log(Level.WARNING, "Teleport player" + event.getPlayer().getName() + "to" + TPL.toString() + "failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (config.getBoolean("onPlayerJoin.welcomeMessage.enable") && player.hasPermission("zlobby.message")) {
                logger.info("Sending welcome message to player " + event.getPlayer().getName());
                String welcomeMessage = config.getString("onPlayerJoin.welcomeMessage.message");
                if (Debug) {
                    logger.info("welcome message:" + welcomeMessage);
                }
                if (welcomeMessage == null) {
                    logger.warning("Welcome message is null, please check your config.yml!");
                    throw new NullPointerException("onPlayerJoin.welcomeMessage.message is null!But it was enabled!");
                }
                if (welcomeMessage.contains("{player}")) {
                    if (Debug) logger.info("Welcome message contains {player}");
                    welcomeMessage = welcomeMessage.replace("{player}", event.getPlayer().getName());
                }
                if (welcomeMessage.contains("{server}")) {
                    if (Debug) logger.info("Welcome message contains {server}");
                    welcomeMessage = welcomeMessage.replace("{server}", Objects.requireNonNull(config.getString("onPlayerJoin.welcomeMessage.serverName")));
                }
                if (welcomeMessage.contains("&")) {
                    if (Debug) logger.info("Welcome message contains &");
                    welcomeMessage = welcomeMessage.replace("&", "§");
                }
                if (Debug) logger.info("Welcome message-finish:" + welcomeMessage);
                event.getPlayer().sendMessage(welcomeMessage);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Welcome message failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (config.getBoolean("onPlayerJoin.changeGameMode.enable") && !player.hasPermission("zlobby.lobby.noChangeMode")) {

                logger.info("Changing player " + event.getPlayer().getName() + " game mode to " + config.getString("onPlayerJoin.changeGameMode.gameMode"));
                event.getPlayer().setGameMode(GameMode.valueOf(Objects.requireNonNull(config.getString("onPlayerJoin.changeGameMode.gameMode")).toUpperCase(Locale.ROOT)));


            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Change game mode failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (config.getBoolean("Lobby.feedPlayer") && player.hasPermission("zlobby.lobby.feed")) {
                logger.info("Feeding player " + event.getPlayer().getName());

                event.getPlayer().setFoodLevel(20);
                event.getPlayer().setSaturation(20);


                player.setArrowsInBody(0);
                player.setFoodLevel(20);
                player.setHealth(20);


            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Feed player failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (onJoinConfig.getBoolean("onJoin.title.enable") && player.hasPermission("zlobby.lobby.effect")) {
                logger.info("Sending title to player " + event.getPlayer().getName());
                String title = onJoinConfig.getString("onJoin.title.title");
                String subtitle = onJoinConfig.getString("onJoin.title.subtitle");
                if (title == null || subtitle == null) {
                    logger.warning("Title is null, please check your config.yml!");
                    throw new NullPointerException("onPlayerJoin.title.title or onPlayerJoin.title.subtitle is null!But it was enabled!");
                }
                if (title.contains("{player}")) {
                    title = title.replace("{player}", event.getPlayer().getName());
                }
                if (title.contains("{server}")) {
                    title = title.replace("{server}", Objects.requireNonNull(config.getString("onPlayerJoin.welcomeMessage.serverName")));
                }
                if (title.contains("&")) {
                    title = title.replace("&", "§");
                }
                if (subtitle.contains("{player}")) {
                    subtitle = subtitle.replace("{player}", event.getPlayer().getName());
                }
                if (subtitle.contains("{server}")) {
                    subtitle = subtitle.replace("{server}", Objects.requireNonNull(config.getString("onPlayerJoin.welcomeMessage.serverName")));
                }
                if (subtitle.contains("&")) {
                    subtitle = subtitle.replace("&", "§");
                }
                final String finalTitle = title;
                final String finalSubtitle = subtitle;
                if (Debug) {
                    logger.info("Title:" + finalTitle + "Subtitle:" + finalSubtitle);
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getPlayer().sendTitle(finalTitle, finalSubtitle, 10, onJoinConfig.getInt("onJoin.title.time"), 10);
                    }
                }.runTaskLater(ZLOBBY.getPlugin(ZLOBBY.class), 20L);


            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Send title failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (onJoinConfig.getBoolean("onJoin.playSound.enable") && player.hasPermission("zlobby.lobby.effect")) {
                logger.info("Sending sound to player " + event.getPlayer().getName());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (String sound : onJoinConfig.getStringList("onJoin.playSound.sound")) {
                            event.getPlayer().playSound(event.getPlayer().getLocation(), sound, 1, 1);
                        }
                    }
                }.runTaskLater(ZLOBBY.getPlugin(ZLOBBY.class), 10L);

            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Sound send failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (onJoinConfig.getBoolean("onJoin.firework.enable") && player.hasPermission("zlobby.lobby.effect")) {
                logger.info("Sending firework to player " + event.getPlayer().getName());
                final List<Map<?, ?>> fireworks = onJoinConfig.getMapList("onJoin.firework.fireworks");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Map<?, ?> fireworkConfig : fireworks) {
                            String typeStr = (String) fireworkConfig.get("type");
                            String colorStr = (String) fireworkConfig.get("color");
                            int power = (int) fireworkConfig.get("power");

                            FireworkEffect.Type type = FireworkEffect.Type.valueOf(typeStr);
                            Color color;
                            try {
                                DyeColor dyeColor = DyeColor.valueOf(colorStr.toUpperCase(Locale.ROOT));
                                color = dyeColor.getColor();
                            } catch (IllegalArgumentException e) {
                                logger.warning("Invalid color name: " + colorStr);
                                continue; // skip this firework
                            }

                            FireworkEffect effect = FireworkEffect.builder()
                                    .with(type)
                                    .withColor(color)
                                    .build();

                            Firework firework = Objects.requireNonNull(location.getWorld()).spawn(location, Firework.class);
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.addEffect(effect);
                            meta.setPower(power);
                            firework.setFireworkMeta(meta);
                        }
                    }
                }.runTaskLater(ZLOBBY.getPlugin(ZLOBBY.class), 10L);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Firework send failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        this.onJoinConfig = ZLOBBY.getPlugin(ZLOBBY.class).getOnJoinConfig();
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        this.TPL = loadTPLocation();
        boolean enable = config.getBoolean("onPlayerJoin.enable");
        this.Debug = ZLOBBY.getPlugin(ZLOBBY.class).Debug;

        if (!enable) {
            return;
        }
        logger.info("Player " + event.getPlayer().getName() + " respawn in the server.");
        try {
            if (config.getBoolean("teleportLocation.enable") && player.hasPermission("zlobby.lobby.tp")) {

                logger.info("Teleporting player " + event.getPlayer().getName() + " to " + TPL.toString());

                event.getPlayer().teleport(TPL);
                Objects.requireNonNull(TPL.getWorld()).setSpawnLocation(TPL);

            }
        } catch (Exception e) {
            logger.warning("Teleportation failed!");
            logger.log(Level.WARNING, "Teleport player" + event.getPlayer().getName() + "to" + TPL.toString() + "failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }

        try {
            if (config.getBoolean("onPlayerJoin.changeGameMode.enable") && !player.hasPermission("zlobby.lobby.noChangeMode")) {
                logger.info("Changing player " + event.getPlayer().getName() + " game mode to " + config.getString("onPlayerJoin.changeGameMode.gameMode"));
                event.getPlayer().setGameMode(GameMode.valueOf(Objects.requireNonNull(config.getString("onPlayerJoin.changeGameMode.gameMode")).toUpperCase(Locale.ROOT)));
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Change game mode failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (config.getBoolean("Lobby.feedPlayer") && player.hasPermission("zlobby.lobby.feed")) {
                logger.info("Feeding player " + event.getPlayer().getName());
                event.getPlayer().setFoodLevel(20);
                event.getPlayer().setSaturation(20);
                player.setArrowsInBody(0);
                player.setFoodLevel(20);
                player.setHealth(20);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Feed player failed!");
            logger.log(Level.WARNING, "Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        this.onJoinConfig = ZLOBBY.getPlugin(ZLOBBY.class).getOnJoinConfig();
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        this.Debug = ZLOBBY.getPlugin(ZLOBBY.class).Debug;
        Player player = event.getPlayer();
        boolean enable = config.getBoolean("Lobby.enable");
        if (!enable) {
            return;
        }
        if (config.getBoolean("Lobby.avoidBlockBreak") && !player.hasPermission("zlobby.lobby.break")) {

            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                player.sendMessage(ChatColor.RED + "You can't break blocks.");
                logger.warning("Player " + player.getName() + " tried to break block on " + event.getBlock().getLocation());
                if (config.getBoolean("Lobby.toKick")) {
                    int attemptCount = getOrIncrementAttemptCount(player.getName());
                    int tried_times = config.getInt("Lobby.tryTimes");
                    if (attemptCount >= tried_times) {
                        kickPlayer(player, "tried to break blocks for " + tried_times + " times");
                        logger.warning("Player " + player.getName() + " was kicked because tried to break block too many times!");

                    }
                }
                event.setCancelled(true);

            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        this.onJoinConfig = ZLOBBY.getPlugin(ZLOBBY.class).getOnJoinConfig();
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        this.Debug = ZLOBBY.getPlugin(ZLOBBY.class).Debug;
        Player player = event.getPlayer();
        boolean enable = config.getBoolean("Lobby.enable");
        if (!enable) {
            return;
        }
        if (config.getBoolean("Lobby.avoidBlockPlace") && !player.hasPermission("zlobby.lobby.place")) {

            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                logger.warning("Player " + player.getName() + " tried to place block on " + event.getBlock().getLocation());
                player.sendMessage(ChatColor.RED + "You can't place blocks.");
                if (config.getBoolean("Lobby.toKick") && !player.hasPermission("zlobby.lobby.neverKick")) {
                    int attemptCount = getOrIncrementAttemptCount(player.getName());
                    int tried_times = config.getInt("Lobby.tryTimes");
                    if (Debug) {
                        logger.info("Player " + player.getName() + " tried to place block for " + attemptCount + " times.");
                        logger.info("Attempt count: " + attemptCount);
                    }
                    if (attemptCount >= tried_times) {
                        kickPlayer(player, "tried to place blocks for " + tried_times + " times");
                        logger.warning("Player " + player.getName() + " was kicked because tried to place block too many times!");

                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        this.onJoinConfig = ZLOBBY.getPlugin(ZLOBBY.class).getOnJoinConfig();
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        this.Debug = ZLOBBY.getPlugin(ZLOBBY.class).Debug;
        Player player = event.getPlayer();
        Location location = player.getLocation();
        World world = player.getWorld();
        int minHeight = world.getMinHeight();
        this.TPL = loadTPLocation();
        if (location.getY() < minHeight) {
            if (player.getGameMode() == GameMode.CREATIVE) return;
            if (config.getBoolean("teleportLocation.enable") && player.hasPermission("zlobby.lobby.tp")) {
                logger.warning("Player " + player.getName() + " fell into the void!");
                player.sendMessage(ChatColor.GOLD + "You will be sent to a safe place.");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                player.teleport(TPL);
            }

        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        this.Debug = ZLOBBY.getPlugin(ZLOBBY.class).Debug;
        this.onJoinConfig = ZLOBBY.getPlugin(ZLOBBY.class).getOnJoinConfig();
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        Player player;
        try {
            player = (Player) event.getEntity();
        } catch (ClassCastException e) {
            return;
        }
        if (Debug) {
            logger.info("Player '" + player.getName() + "' was hurt by " + event.getCause());
            logger.info("Value" + event.getDamage());
        }
        if (!(event instanceof EntityDamageByEntityEvent) && !(event instanceof EntityDamageByBlockEvent)) {
            if (!(event.getCause() == EntityDamageEvent.DamageCause.VOID) && !(event.getCause() == EntityDamageEvent.DamageCause.FALL) && !(event.getCause() == EntityDamageEvent.DamageCause.DROWNING)) {
                if (Debug) logger.info("Player was hurt by command");
                return;
            }
        }
        boolean enable = config.getBoolean("Lobby.enable");
        if (!enable) {
            return;
        }
        if (config.getBoolean("Lobby.cancelHurt") && player.hasPermission("zlobby.lobby.health")) {
            player.setArrowsInBody(0);
            player.setHealth(20);
            event.setCancelled(true);


        }
        if (config.getBoolean("Lobby.feedPlayer") && player.hasPermission("zlobby.lobby.feed")) {

            player.setFoodLevel(20);
            player.setSaturation(20);


        }
    }

    @NotNull
    public Location loadTPLocation() {
        FileConfiguration config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        double x = config.getDouble("teleportLocation.x");
        double y = config.getDouble("teleportLocation.y");
        double z = config.getDouble("teleportLocation.z");
        float yaw = (float) config.getDouble("teleportLocation.yaw");
        float pitch = (float) config.getDouble("teleportLocation.pitch");
        return new Location(Bukkit.getWorlds().getFirst(), x, y, z, yaw, pitch);
    }

    private void kickPlayer(Player player, String reason) {

        player.kickPlayer(ChatColor.RED + "You were been kicked with reason " + reason);
        logger.warning("Player " + player.getName() + " has been kicked for " + reason);
        playerAttemptCounts.remove(player.getName()); // 移除玩家的尝试次数记录
    }

    private int getOrIncrementAttemptCount(String playerName) {
        return playerAttemptCounts.compute(playerName, (name, count) -> count == null ? 1 : count + 1);
    }
}
