package com.john4096.zLOBBY;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import  com.john4096.zLOBBY.Commands.*;
import java.io.File;
import java.util.logging.Logger;



public final class ZLOBBY extends JavaPlugin {
    public boolean Debug = false;
    private final Logger logger = getLogger();
    private final String version = "0.1";
    public FileConfiguration config;
    public YamlConfiguration onJoinConfig;
    private File onJoinFile = new File(getDataFolder(), "onJoin.yml");
    public EventListener eventListener = new EventListener();
    @Override
    public void onEnable() {
        final String version = this.version;

        // Plugin startup logic
        logger.info("Loading Simple ZCraft Lobby plugin");
        logger.info("Running on version"+version);

        eventListener.onEnable();
        Bukkit.getPluginManager().registerEvents(eventListener, this);
        logger.info("Listener loaded");
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
