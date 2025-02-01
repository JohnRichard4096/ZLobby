package com.john4096.zLOBBY.Commands;

import com.john4096.zLOBBY.ZLOBBY;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;


public final class Executor implements CommandExecutor {
    YamlConfiguration onJoinConfig = ZLOBBY.getPlugin(ZLOBBY.class).getOnJoinConfig();
    ResourceBundle language = ZLOBBY.getPlugin(ZLOBBY.class).getLanguage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.onJoinConfig = ZLOBBY.getPlugin(ZLOBBY.class).getOnJoinConfig();
        this.language = ZLOBBY.getPlugin(ZLOBBY.class).getLanguage();
        if (command.getName().equalsIgnoreCase("zlobby")) {
            if (args.length == 0) {
                if (!sender.hasPermission("zlobby.main")) {
                    sender.sendMessage(ChatColor.RED + language.getString("hasNoPermission"));
                    return true;
                }
                String version = ZLOBBY.getPlugin(ZLOBBY.class).getDescription().getVersion();
                String info = ChatColor.GREEN + "ZLOBBY " + version + ChatColor.GOLD + " by JohnRichard4096";
                sender.sendMessage(info);
                return true;
            } else if (args[0].equals("reload")) {
                if (!sender.hasPermission("zlobby.main.reload")) {
                    sender.sendMessage(ChatColor.RED + language.getString("hasNoPermission"));
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + language.getString("reloading"));
                ZLOBBY.getPlugin(ZLOBBY.class).reloadConfig();
                ZLOBBY.getPlugin(ZLOBBY.class).eventListener.onMapLoading();
                sender.sendMessage(ChatColor.GREEN + language.getString("done"));
                return true;
            } else if (args[0].equals("info")) {
                String version = ZLOBBY.getPlugin(ZLOBBY.class).getDescription().getVersion();
                String info = ChatColor.GREEN + "ZLOBBY " + version + " by " + ChatColor.GOLD + "JohnRichard4096";
                sender.sendMessage(info);
                if (sender.hasPermission("zlobby.main.info.more")) {
                    Configuration config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();

                    boolean lobbyEnable = config.getBoolean("Lobby.enable");
                    boolean lobbyAvoidBlockBreak = config.getBoolean("Lobby.avoidBlockBreak");
                    boolean lobbyAvoidBlockPlace = config.getBoolean("Lobby.avoidBlockPlace");
                    boolean lobbyToKick = config.getBoolean("Lobby.toKick");
                    int lobbyTryTimes = config.getInt("Lobby.tryTimes");
                    boolean lobbyCancelHurt = config.getBoolean("Lobby.cancelHurt");
                    boolean lobbyFeedPlayer = config.getBoolean("Lobby.feedPlayer");
                    boolean onPlayerJoinEnable = config.getBoolean("onPlayerJoin.enable");
                    boolean onPlayerJoinChangeGameMode = config.getBoolean("onPlayerJoin.changeGameMode.enable");
                    boolean onPlayerJoinWelcomeMessageEnable = config.getBoolean("onPlayerJoin.welcomeMessage.enable");
                    boolean tpEnable = config.getBoolean("teleportLocation.enable");
                    boolean titleEnable = onJoinConfig.getBoolean("onJoin.title.enable");
                    boolean playSoundEnable = onJoinConfig.getBoolean("onJoin.playSound.enable");
                    boolean fireWorkEnable = onJoinConfig.getBoolean("onJoin.firework.enable");


                    sender.sendMessage(ChatColor.GREEN + "teleport location on player join: " + ChatColor.GOLD + tpEnable);

                    sender.sendMessage(ChatColor.GREEN + "enable Lobby function: " + ChatColor.GOLD + lobbyEnable);
                    if (lobbyEnable) {
                        sender.sendMessage(ChatColor.GREEN + "Lobby Avoid Block Break: " + ChatColor.GOLD + lobbyAvoidBlockBreak);
                        sender.sendMessage(ChatColor.GREEN + "Lobby Avoid Block Place: " + ChatColor.GOLD + lobbyAvoidBlockPlace);
                        sender.sendMessage(ChatColor.GREEN + "kick player if too many times: " + ChatColor.GOLD + lobbyToKick);
                        sender.sendMessage(ChatColor.GREEN + "Lobby player max block action times: " + ChatColor.GOLD + lobbyTryTimes);
                        sender.sendMessage(ChatColor.GREEN + "Lobby Cancel Hurt: " + ChatColor.GOLD + lobbyCancelHurt);
                        sender.sendMessage(ChatColor.GREEN + "Lobby Feed Player: " + ChatColor.GOLD + lobbyFeedPlayer);
                    }
                    sender.sendMessage(ChatColor.GREEN + "onPlayerJoin: " + ChatColor.GOLD + onPlayerJoinEnable);
                    if (onPlayerJoinEnable) {
                        sender.sendMessage(ChatColor.GREEN + "onPlayerJoin Change GameMode: " + ChatColor.GOLD + onPlayerJoinChangeGameMode);

                        sender.sendMessage(ChatColor.GREEN + "onPlayerJoin Welcome Message: " + ChatColor.GOLD + onPlayerJoinWelcomeMessageEnable);
                    }
                    sender.sendMessage(ChatColor.GREEN + "Title enable: " + ChatColor.GOLD + titleEnable);
                    sender.sendMessage(ChatColor.GREEN + "PlaySound enable: " + ChatColor.GOLD + playSoundEnable);
                    sender.sendMessage(ChatColor.GREEN + "FireWorks enable: " + ChatColor.GOLD + fireWorkEnable);
                    return true;

                }
                return true;
            } else if (args[0].equals("debugger")) {
                if (sender instanceof Player player) {
                    if (!player.hasPermission("zlobby.main.debugger")) {
                        sender.sendMessage(ChatColor.RED + language.getString("hasNoPermission"));
                        return true;
                    }
                }
                if (!(args.length >= 2)) {
                    sender.sendMessage(ChatColor.RED + "Please use /zlobby debugger on/off");
                    return true;
                }
                if (args[1].equals("on")) {
                    if (!ZLOBBY.getPlugin(ZLOBBY.class).Debug) {
                        ZLOBBY.getPlugin(ZLOBBY.class).Debug = true;
                        sender.sendMessage(ChatColor.GREEN + "Debugger is now setting on");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Debugger is already on");
                    }
                    return true;
                } else if (args[1].equals("off")) {
                    if (ZLOBBY.getPlugin(ZLOBBY.class).Debug) {
                        ZLOBBY.getPlugin(ZLOBBY.class).Debug = false;
                        sender.sendMessage(ChatColor.GREEN + "Debugger is now setting off");

                    } else {
                        sender.sendMessage(ChatColor.RED + "Debugger is already off");
                    }
                    return true;

                } else return false;

            } else return false;
        }
        return false;
    }
}
