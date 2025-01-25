package com.john4096.zLOBBY;
import org.bukkit.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import  org.bukkit.event.block.BlockBreakEvent;
import  org.bukkit.event.block.BlockPlaceEvent;
import java.util.Locale;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;


public class EventListener  implements Listener {

    private final Logger logger = ZLOBBY.getPlugin(ZLOBBY.class).getLogger();
    private Location TPL;
    private FileConfiguration config;

    private Map<String, Integer> playerAttemptCounts = new HashMap<>();

    public void onEnable() {
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        logger.info("EventHandler loaded");
    }
    @EventHandler
    public void  onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        this.TPL = loadTPLocation();
        boolean enable = config.getBoolean("onPlayerJoin.enable");


        if (!enable){
            return;
        }
        logger.info("Player " + event.getPlayer().getName() + " joined the server");
        try {
            if (config.getBoolean("teleportLocation.enable")) {
                logger.info("Teleporting player " + event.getPlayer().getName() + " to " + TPL.toString());
                event.getPlayer().teleport(TPL);
                player.setRespawnLocation(TPL);
            }
        }catch (Exception e){
            logger.warning("Teleportation failed!");
            logger.log(Level.WARNING,"Teleport player"+ event.getPlayer().getName()+"to"+TPL.toString()+"failed!");
            logger.log(Level.WARNING,"Exception:"+e.getMessage());
            e.printStackTrace();
        }
        try{
        if (config.getBoolean("onPlayerJoin.welcomeMessage.enable")) {
            logger.info("Sending welcome message to player " + event.getPlayer().getName());
            String welcomeMessage = config.getString("onPlayerJoin.welcomeMessage.message");
            if (welcomeMessage == null) {
                logger.warning("Welcome message is null, please check your config.yml!");
                throw new NullPointerException("onPlayerJoin.welcomeMessage.message is null!But it was enabled!");
            }
            event.getPlayer().sendMessage(welcomeMessage.replace("&", "§").replace("{player}", event.getPlayer().getName()).replace("{server}",config.getString("welcomeMessage.serverName")));
        }
        }catch (Exception e){
            logger.log(Level.WARNING,"Welcome message failed!");
            logger.log(Level.WARNING,"Exception:"+e.getMessage());
            e.printStackTrace();
        }
        try{
        if (config.getBoolean("onPlayerJoin.changeGameMode.enable")) {
            logger.info("Changing player " + event.getPlayer().getName() + " game mode to " + config.getString("onPlayerJoin.changeGameMode.gameMode"));
            event.getPlayer().setGameMode(GameMode.valueOf(config.getString("onPlayerJoin.changeGameMode.gameMode").toUpperCase(Locale.ROOT)));
        }
        }catch (Exception e){
            logger.log(Level.WARNING,"Change game mode failed!");
            logger.log(Level.WARNING,"Exception:"+e.getMessage());
            e.printStackTrace();
        }
        try{
            if (config.getBoolean("onPlayerJoin.feedPlayer")) {
                logger.info("Feeding player " + event.getPlayer().getName());
                event.getPlayer().setFoodLevel(20);
                event.getPlayer().setSaturation(20);
                player.setArrowsInBody(0);
                player.setFoodLevel(20);
                player.setHealth(20);

            }
        }catch (Exception e){
            logger.log(Level.WARNING,"Feed player failed!");
            logger.log(Level.WARNING,"Exception:"+e.getMessage());
            e.printStackTrace();
        }
    }
    private void kickPlayer(Player player, String reason) {

        player.kickPlayer(ChatColor.RED + "You were been kicked with reason " + reason);
        logger.warning("Player " + player.getName() + " has been kicked for " + reason);
        playerAttemptCounts.remove(player.getName()); // 移除玩家的尝试次数记录
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        Player player = event.getPlayer();
        boolean enable = config.getBoolean("Lobby.enable");
        if (!enable){
            return;
        }
        if(config.getBoolean("Lobby.avoidBlockBreak")){
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE && !event.getPlayer().isOp()) {
                logger.warning("Player " + player.getName() + " tried to break block on " + event.getBlock().getLocation());
                    if (config.getBoolean("Lobby.toKick")) {
                        int attemptCount = getOrIncrementAttemptCount(player.getName());
                        int tried_times = config.getInt("Lobby.tryTimes");
                        if (attemptCount >= tried_times) {
                            kickPlayer(player, "tried to break blocks for " + tried_times + " times");
                            logger.warning("Player " + player.getName() + " was kicked because tried to break block too many times!");
                            return;
                        }
                    }
                    player.sendMessage(ChatColor.RED + "You can't break blocks.");

                    event.setCancelled(true);
            }
        }


    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        Player player = event.getPlayer();
        boolean enable = config.getBoolean("Lobby.enable");
        if (!enable){
            return;
        }
        if (config.getBoolean("Lobby.avoidBlockPlace")){
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE && !event.getPlayer().isOp()) {
                logger.warning("Player " + player.getName() + " tried to place block on " + event.getBlock().getLocation());
                if (config.getBoolean("Lobby.toKick")) {
                    int attemptCount = getOrIncrementAttemptCount(player.getName());
                    int tried_times = config.getInt("Lobby.tryTimes");
                    if (attemptCount >= tried_times) {
                        kickPlayer(player, "tried to place blocks for " + tried_times + " times");
                        logger.warning("Player " + player.getName() + " was kicked because tried to place block too many times!");
                        return;
                    }
                }
                player.sendMessage(ChatColor.RED + "You can't place blocks.");
                event.setCancelled(true);
            }
        }
    }
    private int getOrIncrementAttemptCount(String playerName) {
        return playerAttemptCounts.compute(playerName, (name, count) -> count == null ? 1 : count + 1);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        World world = player.getWorld();
        int minHeight = world.getMinHeight();
        this.TPL = loadTPLocation();
        if (location.getY() < minHeight) {
            if (player.getGameMode() == GameMode.CREATIVE)return;

            logger.warning("Player " + player.getName() + " fell into the void!");
            if (config.getBoolean("teleportLocation")){
                player.sendMessage(ChatColor.GOLD+"You will be sent to a safe place.");
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                player.teleport(TPL);
            }

        }
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        this.config = ZLOBBY.getPlugin(ZLOBBY.class).getConfig();
        Player player = (Player) event.getEntity();

        boolean enable = config.getBoolean("Lobby.enable");
        if (!enable){
            return;
        }
        if (config.getBoolean("Lobby.cancelHurt")){
            player.setArrowsInBody(0);
            player.setFoodLevel(20);
            player.setHealth(20);
            event.setCancelled(true);
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
}
