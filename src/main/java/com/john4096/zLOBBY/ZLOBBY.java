package com.john4096.zLOBBY;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import  com.john4096.zLOBBY.Commands.*;
import java.io.File;
import java.util.logging.Logger;



public final class ZLOBBY extends JavaPlugin {
    public boolean Debug = false;
    private final Logger logger = getLogger();
    private final String version = getDescription().getVersion();
    public FileConfiguration config;
    public YamlConfiguration onJoinConfig;
    private File onJoinFile = new File(getDataFolder(), "onJoin.yml");
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
        logger.info("Running on version"+version);
        if(version.toLowerCase().contains("dev")){
            logger.warning("You are running a development version of the plugin, please do not use it in a production environment");
            logger.warning("See the latest build on https://jenkins.micro-wave.cc/job/ZLobby/");
        }
        logger.info("Loading config......");
        saveDefaultConfig();
        if (!onJoinFile.exists()){
            saveResource("onJoin.yml",false);
        }
        this.onJoinConfig = YamlConfiguration.loadConfiguration(onJoinFile);
        this.config = getConfig();
        logger.info("Loading listener class......");
        Bukkit.getPluginCommand("zlobby").setExecutor(new Executor());
        Bukkit.getPluginCommand("zlobby").setTabCompleter(new CommandTabCompleter());
        EventListener eventListener = new EventListener();
        eventListener.onEnable();
        Bukkit.getPluginManager().registerEvents(eventListener, this);
        logger.info("Listener loaded");
        logger.info("Loading BStats......");
        int pluginId =  24574;
        Metrics metrics = new Metrics(this, pluginId);

        logger.info("Loaded!");
    }

    @Override
    public void onDisable() {
        logger.info("Unloading ZCRAFT Lobby plugin");

        // Plugin shutdown logic
        logger.info("Unloaded!");
    }
    public YamlConfiguration getOnJoinConfig(){
        if (this.onJoinConfig==null){
            reloadConfig();
        }
        return this.onJoinConfig;

    }
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        if (!this.onJoinFile.exists()){
            saveResource("onJoin.yml",false);
        }
        this.onJoinConfig = YamlConfiguration.loadConfiguration(onJoinFile);
    }

}
