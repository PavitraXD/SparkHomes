package com.pavitraxd.sparkhomes.listeners;

import com.pavitraxd.sparkhomes.SparkHomes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleportListener implements Listener {
    private final SparkHomes plugin;

    public TeleportListener(SparkHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getX() == event.getTo().getX() &&
            event.getFrom().getY() == event.getTo().getY() &&
            event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }
        plugin.getTeleportManager().handleMove(player);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        plugin.getTeleportManager().handleDamage(player);
    }
}
