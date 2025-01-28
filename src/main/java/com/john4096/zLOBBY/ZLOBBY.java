package com.john4096.zLOBBY;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import  com.john4096.zLOBBY.Commands.*;
import java.io.File;
import java.util.logging.Logger;



public final class ZLOBBY extends JavaPlugin {

    private final Logger logger = getLogger();
    private final String version = "0.1";
    public FileConfiguration config;
    public FileConfiguration onJoinConfig;
    private File onJoinFile = new File(getDataFolder(), "onJoin.yml");
    @Override
    public void onEnable() {
        final String version = this.version;

        // Plugin startup logic
        logger.info("Loading Simple ZCraft Lobby plugin");
        logger.info("Running on version"+version);
        EventListener eventListener = new EventListener();
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

        logger.info("Loaded!");
    }

    @Override
    public void onDisable() {
        logger.info("Unloading ZCRAFT Lobby plugin");

        // Plugin shutdown logic
        logger.info("Unloaded!");
    }
    public FileConfiguration getOnJoinConfig(){
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
