package com.john4096.zLOBBY;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import com.john4096.zLOBBY.Commands.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;


public final class ZLOBBY extends JavaPlugin {
    public boolean Debug = false;
    private final Logger logger = getLogger();
    private final String version = getDescription().getVersion();
    public FileConfiguration config;
    public YamlConfiguration onJoinConfig;
    private final File onJoinFile = new File(getDataFolder(), "onJoin.yml");
    private final File worldSettingFile = new File(getDataFolder(), "worldSetting.yml");
    public YamlConfiguration worldSettingConfig;
    public EventListener eventListener;

    @Override
    public void onEnable() {
        final String version = this.version;

        // Plugin startup logic
        logger.info("Loading Simple ZCraft Lobby plugin");
        logger.info("""
                                             \033[31m
                                             \s
                ,---,|         |    |        \s
                 .-' |    ,---.|---.|---.,   .
                |    |    |   ||   ||   ||   |
                `---'`---'`---'`---'`---'`---|
                                         `---'
                                         \033[0m""");
        logger.info("Running on version" + version);
        if (version.toLowerCase().contains("dev")) {
            logger.warning("You are running a development version of the plugin, please do not use it in a production environment");
            logger.warning("See the latest build on https://jenkins.micro-wave.cc/job/ZLobby/");
        }
        logger.info("Loading config......");
        saveDefaultConfig();
        reloadConfig();
        this.onJoinConfig = YamlConfiguration.loadConfiguration(onJoinFile);
        this.worldSettingConfig = YamlConfiguration.loadConfiguration(worldSettingFile);
        this.config = getConfig();
        logger.info("Loading listener class......");
        Objects.requireNonNull(Bukkit.getPluginCommand("zlobby")).setExecutor(new Executor());
        Objects.requireNonNull(Bukkit.getPluginCommand("zlobby")).setTabCompleter(new CommandTabCompleter());
        eventListener = new EventListener();
        eventListener.onEnable();
        new BukkitRunnable() {
            @Override
            public void run() {
                eventListener.onMapLoading();
            }
        }.runTaskLater(this, 1L);
        Bukkit.getPluginManager().registerEvents(eventListener, this);
        logger.info("Listener loaded");
        logger.info("Loading BStats......");
        int pluginId = 24574;
        new Metrics(this, pluginId);

        logger.info("Loaded!");
    }

    @Override
    public void onDisable() {
        logger.info("Unloading ZCRAFT Lobby plugin");

        // Plugin shutdown logic
        logger.info("Unloaded!");
    }

    public YamlConfiguration getOnJoinConfig() {
        if (this.onJoinConfig == null) {
            reloadConfig();
        }
        return this.onJoinConfig;

    }

    public YamlConfiguration getWorldSettingConfig() {
        if (this.worldSettingConfig == null) {
            reloadConfig();
        }
        return this.worldSettingConfig;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        if (!this.onJoinFile.exists()) {
            saveResource("onJoin.yml", false);
        }
        if (!this.worldSettingFile.exists()) {
            saveResource("worldSetting.yml", false);
        }
        this.onJoinConfig = YamlConfiguration.loadConfiguration(onJoinFile);
        this.worldSettingConfig = YamlConfiguration.loadConfiguration(worldSettingFile);
    }

}
