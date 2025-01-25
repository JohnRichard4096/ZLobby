package com.john4096.zLOBBY;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import  com.john4096.zLOBBY.Commands.*;
import java.util.logging.Logger;



public final class ZLOBBY extends JavaPlugin {

    private final Logger logger = getLogger();
    private final String version = "0.1";
    public FileConfiguration config;
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


}
