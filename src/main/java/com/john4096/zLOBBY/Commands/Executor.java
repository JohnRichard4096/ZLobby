package com.john4096.zLOBBY.Commands;

import com.john4096.zLOBBY.ZLOBBY;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;


public final class Executor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("zlobby")){
            if (args.length == 0){
                if (!sender.hasPermission("zlobby.main")) {
                    sender.sendMessage(ChatColor.RED+"You don't have permission to use this command!");
                    return true;
                }
                String version = ZLOBBY.getPlugin(ZLOBBY.class).getDescription().getVersion();
                String info = ChatColor.GREEN+"ZLOBBY "+version+" by JohnRichard4096";
                sender.sendMessage(info);
                return true;
            }
            if (args[0].equals("reload")){
                if (!sender.hasPermission("zlobby.main.reload")){
                    sender.sendMessage(ChatColor.RED+"You don't have permission to use this command!");
                    return true;
                }
                ZLOBBY.getPlugin(ZLOBBY.class).reloadConfig();
                sender.sendMessage(ChatColor.GREEN+"Reload complete");
                return true;
            } else if (args[0].equals("info")) {
                String version = ZLOBBY.getPlugin(ZLOBBY.class).getDescription().getVersion();
                String info = ChatColor.GREEN+"ZLOBBY "+version+" by "+ChatColor.GOLD+"JohnRichard4096";
                sender.sendMessage(info);
                if (sender.hasPermission("zlobby.main.info.more")){
                    Configuration config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
                    boolean debug = config.getBoolean("Main.debug");
                    boolean lobbyEnable = config.getBoolean("Lobby.enable");
                    String lobbyWorld = config.getString("Lobby.world");
                    boolean lobbyAvoidBlockBreak = config.getBoolean("Lobby.avoidBlockBreak");
                    boolean lobbyAvoidBlockPlace = config.getBoolean("Lobby.avoidBlockPlace");
                    boolean lobbyToKick = config.getBoolean("Lobby.toKick");
                    int lobbyTryTimes = config.getInt("Lobby.tryTimes");
                    boolean lobbyCancelHurt = config.getBoolean("Lobby.cancelHurt");
                    boolean lobbyFeedPlayer = config.getBoolean("Lobby.feedPlayer");
                    boolean onPlayerJoinEnable = config.getBoolean("onPlayerJoin.enable");
                    boolean onPlayerJoinChangeGameMode = config.getBoolean("onPlayerJoin.changeGameMode.enable");
                    String onPlayerJoinChangeGameModeGameMode = config.getString("onPlayerJoin.changeGameMode.gameMode");
                    boolean onPlayerJoinWelcomeMessageEnable = config.getBoolean("onPlayerJoin.welcomeMessage.enable");
                    String onPlayerJoinWelcomeMessageServerName = config.getString("onPlayerJoin.welcomeMessage.serverName");
                    String onPlayerJoinWelcomeMessageMessage = config.getString("onPlayerJoin.welcomeMessage.message");
                    boolean tpEnable = config.getBoolean("teleportLocation.enable");


                    sender.sendMessage(ChatColor.GREEN+"Debug: "+ChatColor.GOLD+debug);
                    sender.sendMessage(ChatColor.GREEN+"teleport location on player join: "+ChatColor.GOLD+tpEnable);
                    if (tpEnable){
                        double tpX = config.getDouble("teleportLocation.x");
                        double tpY = config.getDouble("teleportLocation.y");
                        double tpZ = config.getDouble("teleportLocation.z");
                        double tpYaw = config.getDouble("teleportLocation.yaw");
                        double tpPitch = config.getDouble("teleportLocation.pitch");
                        sender.sendMessage(ChatColor.GREEN+"teleport location: "+ChatColor.GOLD+tpX+","+tpY+","+tpZ+"\n"+ChatColor.GREEN+"pitch,yaw" +ChatColor.GOLD+ tpPitch+","+tpYaw);

                    }

                    sender.sendMessage(ChatColor.GREEN+"enable Lobby function: "+ChatColor.GOLD+lobbyEnable);
                    if (lobbyEnable){
                        sender.sendMessage(ChatColor.GREEN+"Lobby World: "+ChatColor.GOLD+lobbyWorld);
                        sender.sendMessage(ChatColor.GREEN+"Lobby Avoid Block Break: "+ChatColor.GOLD+lobbyAvoidBlockBreak);
                        sender.sendMessage(ChatColor.GREEN+"Lobby Avoid Block Place: "+ChatColor.GOLD+lobbyAvoidBlockPlace);
                        sender.sendMessage(ChatColor.GREEN+"kick player if too many times: "+ChatColor.GOLD+lobbyToKick);
                        sender.sendMessage(ChatColor.GREEN+"Lobby player max block action times: "+ChatColor.GOLD+lobbyTryTimes);
                        sender.sendMessage(ChatColor.GREEN+"Lobby Cancel Hurt: "+ChatColor.GOLD+lobbyCancelHurt);
                        sender.sendMessage(ChatColor.GREEN+"Lobby Feed Player: "+ChatColor.GOLD+lobbyFeedPlayer);
                    }
                    sender.sendMessage(ChatColor.GREEN+"onPlayerJoin: "+ChatColor.GOLD+onPlayerJoinEnable);
                    if (onPlayerJoinEnable){
                        sender.sendMessage(ChatColor.GREEN+"onPlayerJoin Change GameMode: "+ChatColor.GOLD+onPlayerJoinChangeGameMode);
                        if (onPlayerJoinChangeGameMode){
                            sender.sendMessage(ChatColor.GREEN+"onPlayerJoin Change GameMode GameMode: "+ChatColor.GOLD+onPlayerJoinChangeGameModeGameMode);
                        }
                        sender.sendMessage(ChatColor.GREEN+"onPlayerJoin Welcome Message: "+ChatColor.GOLD+onPlayerJoinWelcomeMessageEnable);
                        if (onPlayerJoinWelcomeMessageEnable){
                            sender.sendMessage(ChatColor.GREEN+"onPlayerJoin Welcome Message Server Name: "+ChatColor.GOLD+onPlayerJoinWelcomeMessageServerName);
                            sender.sendMessage(ChatColor.GREEN+"onPlayerJoin Welcome Message Message: "+ChatColor.GOLD+onPlayerJoinWelcomeMessageMessage);
                        }
                    }

                    return true;

                }
                return true;
            }else return false;
        }
        return false;
    }
}
