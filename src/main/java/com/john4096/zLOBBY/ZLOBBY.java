package com.john4096.zLOBBY;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import com.john4096.zLOBBY.Commands.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public final class ZLOBBY extends JavaPlugin {
    private ResourceBundle Language;
    private String language = "en_US";
    public boolean Debug = false;
    private final Logger logger = getLogger();
    private final String version = getDescription().getVersion();
    public FileConfiguration config;
    public YamlConfiguration onJoinConfig;
    private final File onJoinFile = new File(getDataFolder(), "onJoin.yml");
    private final File worldSettingFile = new File(getDataFolder(), "worldSetting.yml");
    private final File languageConfigFile = new File(getDataFolder(), "lang.yml");
    public YamlConfiguration languageConfig;
    public YamlConfiguration worldSettingConfig;
    public EventListener eventListener;

    @Override
    public void onEnable() {
        logger.info("Loading ZCraft Lobby plugin......");
        logger.info("Loading config......");
        final String version = this.version;
        saveDefaultConfig();
        reloadConfig();
        logger.info("""
                                             \033[31m
                                             \s
                ,---,|         |    |        \s
                 .-' |    ,---.|---.|---.,   .
                |    |    |   ||   ||   ||   |
                `---'`---'`---'`---'`---'`---|
                                         `---'
                                         \033[0m""");
        String path = "messages/messages_"+this.language;
        this.Language = ResourceBundle.getBundle(path);
        logger.info(Language.getString("runningOn") + version);
        if (version.toLowerCase().contains("dev")) {
            logger.warning(Language.getString("devVersion"));
            logger.warning(Language.getString("latestBuild"));
        }

        this.onJoinConfig = YamlConfiguration.loadConfiguration(onJoinFile);
        this.worldSettingConfig = YamlConfiguration.loadConfiguration(worldSettingFile);
        this.config = getConfig();
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
        int pluginId = 24574;
        new Metrics(this, pluginId);

        logger.info(Language.getString("loaded"));
    }

    @Override
    public void onDisable() {
        logger.info(Language.getString("onDisable"));

        // Plugin shutdown logic
        logger.info(Language.getString("done"));
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
    public ResourceBundle getLanguage() {
        if (this.languageConfig == null) {
            reloadConfig();
        }
        return this.Language;
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
        if (!this.languageConfigFile.exists()) {
            saveResource("lang.yml", false);
        }

        this.onJoinConfig = YamlConfiguration.loadConfiguration(onJoinFile);
        this.worldSettingConfig = YamlConfiguration.loadConfiguration(worldSettingFile);
        this.languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
        switch (languageConfig.getString("language.lang")){
            case("zh_CN")-> this.language = "zh_CN";
            case ("en_US")-> this.language = "en_US";
            case null, default -> {
                logger.warning("Language config is illegal!");
                logger.warning("Please check your lang.yml file!");
            }
        }
    }

}
