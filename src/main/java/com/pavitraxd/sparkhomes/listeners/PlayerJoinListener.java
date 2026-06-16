package com.pavitraxd.sparkhomes.listeners;

import com.pavitraxd.sparkhomes.SparkHomes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {
    private final SparkHomes plugin;

    public PlayerJoinListener(SparkHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        plugin.getHomeManager().loadPlayerHomes(uuid);
    }
}
