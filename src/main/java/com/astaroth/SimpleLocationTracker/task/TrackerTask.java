package com.astaroth.SimpleLocationTracker.task;

import com.astaroth.SimpleLocationTracker.SimpleTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collection;

public class TrackerTask extends BukkitRunnable {

    private boolean enable;

    private final SimpleTracker simpleTracker;

    public TrackerTask(SimpleTracker simpleTracker){
        this.simpleTracker = simpleTracker;
        this.enable = true;
    }

    @Override
    public void run() {
        if (!enable) {
            cancel();
            return;
        }
        long i;
        if ((i = simpleTracker.logs.parallelStream().count()) >= simpleTracker.config.getTracker().getUpdateCount()){
            simpleTracker.update().thenRun(() -> {
                simpleTracker.logs.clear();
                Bukkit.getLogger().info(ChatColor.GREEN + String.format("Logged %d locations into the system!", i));
            });
            return;
        }
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (players.size() == 0) return;
        players.parallelStream().forEach(player -> {
            String data = String.format("'%d', '%s', '%f', '%f', '%s'",
                    Instant.now().getEpochSecond(),
                    player.getUniqueId(),
                    player.getLocation().getX(),
                    player.getLocation().getZ(),
                    player.getLocation().getWorld().getName());
            try {
                if (!player.hasPermission("simpletracker.notrack")) simpleTracker.logs.add(String.format("('%s', " + data + ")", SimpleTracker.hash(data)));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
