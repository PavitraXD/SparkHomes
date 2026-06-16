package com.pavitraxd.sparkhomes.listeners;

import com.pavitraxd.sparkhomes.SparkHomes;
import com.pavitraxd.sparkhomes.gui.HomesGUI;
import com.pavitraxd.sparkhomes.models.Home;
import com.pavitraxd.sparkhomes.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GUIListener implements Listener {
    private final SparkHomes plugin;

    public GUIListener(SparkHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getView().title().equals(net.kyori.adventure.text.Component.text("HOMES"))) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        int[] homeSlots = HomesGUI.getHomeSlots();
        int[] deleteSlots = HomesGUI.getDeleteButtonSlots();

        for (int i = 0; i < homeSlots.length; i++) {
            if (slot == homeSlots[i]) {
                handleHomeClick(player, i);
                return;
            }
            if (slot == deleteSlots[i]) {
                handleDeleteClick(player, i);
                return;
            }
        }
    }

    private void handleHomeClick(Player player, int index) {
        UUID uuid = player.getUniqueId();
        var homes = plugin.getHomeManager().getPlayerHomes(uuid);

        if (index >= homes.size()) {
            int maxHomes = plugin.getHomeManager().getMaxHomes(player);
            if (index >= maxHomes) {
                MessageUtils.sendMessage(player, "<red>This slot is locked. Upgrade your rank to unlock more homes.");
            }
            return;
        }

        Home home = homes.get(index);
        plugin.getTeleportManager().startTeleport(player, home);
        player.closeInventory();
    }

    private void handleDeleteClick(Player player, int index) {
        UUID uuid = player.getUniqueId();
        var homes = plugin.getHomeManager().getPlayerHomes(uuid);

        if (index >= homes.size()) return;

        Home home = homes.get(index);
        plugin.getHomeManager().deleteHome(uuid, home.getName()).thenAccept(success -> {
            if (success) {
                if (plugin.getConfigManager().isMessagesEnabled()) {
                    MessageUtils.sendMessage(player, "<green>Home <aqua>" + home.getName() + " <green>deleted successfully!");
                }
                plugin.getHomesGUI().open(player);
            } else {
                MessageUtils.sendMessage(player, "<red>Failed to delete home.");
            }
        });
    }
}
