package com.pavitraxd.sparkhomes.managers;

import com.pavitraxd.sparkhomes.SparkHomes;
import com.pavitraxd.sparkhomes.models.Home;
import com.pavitraxd.sparkhomes.utils.MessageUtils;
import com.pavitraxd.sparkhomes.utils.SoundUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {
    private final SparkHomes plugin;
    private final Map<UUID, BukkitTask> teleportTasks;
    private final Map<UUID, Location> startLocations;
    private final Map<UUID, Long> lastTeleport;
    private final Map<UUID, Long> teleportStartTimes;
    private final Map<UUID, Integer> countdowns;

    public TeleportManager(SparkHomes plugin) {
        this.plugin = plugin;
        this.teleportTasks = new HashMap<>();
        this.startLocations = new HashMap<>();
        this.lastTeleport = new HashMap<>();
        this.teleportStartTimes = new HashMap<>();
        this.countdowns = new HashMap<>();
    }

    public void startTeleport(Player player, Home home) {
        UUID uuid = player.getUniqueId();

        if (isOnCooldown(uuid)) {
            long remaining = getCooldownRemaining(uuid);
            MessageUtils.sendMessage(player, "<red>You must wait " + remaining + " seconds before teleporting again.");
            return;
        }

        int delay = plugin.getConfigManager().getTeleportDelay();
        if (delay <= 0) {
            teleportPlayer(player, home);
            return;
        }

        startLocations.put(uuid, player.getLocation().clone());
        teleportStartTimes.put(uuid, System.currentTimeMillis());
        countdowns.put(uuid, delay);

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int countdown = countdowns.getOrDefault(uuid, 0);
            if (countdown > 0) {
                if (plugin.getConfigManager().isSoundsEnabled()) {
                    SoundUtils.playCountdownSound(player);
                }
                player.sendActionBar(MessageUtils.format("<aqua>Teleporting in " + countdown + "..."));
                countdowns.put(uuid, countdown - 1);
            } else {
                teleportPlayer(player, home);
            }
        }, 0L, 20L);

        teleportTasks.put(uuid, task);
    }

    private void teleportPlayer(Player player, Home home) {
        UUID uuid = player.getUniqueId();
        cancelTeleport(uuid);

        Location location = new Location(Bukkit.getWorld(home.getWorld()), home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch());
        player.teleport(location);

        if (plugin.getConfigManager().isSoundsEnabled()) {
            SoundUtils.playSuccessSound(player);
        }

        if (plugin.getConfigManager().isMessagesEnabled()) {
            MessageUtils.sendMessage(player, "<green>Teleported to <aqua>" + home.getName() + "<green>!");
        }

        lastTeleport.put(uuid, System.currentTimeMillis());
    }

    public void cancelTeleport(UUID uuid) {
        BukkitTask task = teleportTasks.remove(uuid);
        if (task != null) {
            task.cancel();
        }
        startLocations.remove(uuid);
        teleportStartTimes.remove(uuid);
        countdowns.remove(uuid);
    }

    public void handleMove(Player player) {
        if (!plugin.getConfigManager().isCancelOnMove()) return;

        UUID uuid = player.getUniqueId();
        if (!isTeleporting(uuid)) return;

        Location start = startLocations.get(uuid);
        if (start != null && player.getLocation().distance(start) > 0.1) {
            cancelTeleport(uuid);
            if (plugin.getConfigManager().isSoundsEnabled()) {
                SoundUtils.playCancelSound(player);
            }
            if (plugin.getConfigManager().isMessagesEnabled()) {
                MessageUtils.sendMessage(player, "<red>Teleport cancelled! You moved.");
            }
        }
    }

    public void handleDamage(Player player) {
        if (!plugin.getConfigManager().isCancelOnDamage()) return;

        UUID uuid = player.getUniqueId();
        if (!isTeleporting(uuid)) return;

        cancelTeleport(uuid);
        if (plugin.getConfigManager().isSoundsEnabled()) {
            SoundUtils.playCancelSound(player);
        }
        if (plugin.getConfigManager().isMessagesEnabled()) {
            MessageUtils.sendMessage(player, "<red>Teleport cancelled! You took damage.");
        }
    }

    public boolean isTeleporting(UUID uuid) {
        return teleportTasks.containsKey(uuid);
    }

    private int getRemainingCountdown(UUID uuid, int totalDelay) {
        BukkitTask task = teleportTasks.get(uuid);
        if (task == null) return 0;

        long elapsed = (System.currentTimeMillis() - (lastTeleport.containsKey(uuid) ? lastTeleport.get(uuid) : System.currentTimeMillis())) / 1000;
        return Math.max(0, totalDelay - (int) elapsed);
    }

    private boolean isOnCooldown(UUID uuid) {
        if (!lastTeleport.containsKey(uuid)) return false;

        long cooldown = plugin.getConfigManager().getTeleportCooldown() * 1000L;
        if (cooldown <= 0) return false;

        long elapsed = System.currentTimeMillis() - lastTeleport.get(uuid);
        return elapsed < cooldown;
    }

    private long getCooldownRemaining(UUID uuid) {
        if (!lastTeleport.containsKey(uuid)) return 0;

        long cooldown = plugin.getConfigManager().getTeleportCooldown() * 1000L;
        if (cooldown <= 0) return 0;

        long elapsed = System.currentTimeMillis() - lastTeleport.get(uuid);
        return Math.max(0, (cooldown - elapsed) / 1000);
    }

    public void cleanup() {
        for (BukkitTask task : teleportTasks.values()) {
            task.cancel();
        }
        teleportTasks.clear();
        startLocations.clear();
        lastTeleport.clear();
        teleportStartTimes.clear();
        countdowns.clear();
    }
}
