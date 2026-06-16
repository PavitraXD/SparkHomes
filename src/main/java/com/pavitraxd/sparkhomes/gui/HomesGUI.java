package com.pavitraxd.sparkhomes.gui;

import com.pavitraxd.sparkhomes.SparkHomes;
import com.pavitraxd.sparkhomes.models.Home;
import com.pavitraxd.sparkhomes.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomesGUI {
    private final SparkHomes plugin;
    private static final int[] HOME_SLOTS = {11, 12, 13, 14, 15};
    private static final int[] DELETE_BUTTON_SLOTS = {20, 21, 22, 23, 24};

    public HomesGUI(SparkHomes plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        UUID uuid = player.getUniqueId();
        var homes = plugin.getHomeManager().getPlayerHomes(uuid);
        int maxHomes = plugin.getHomeManager().getMaxHomes(player);

        Inventory inventory = Bukkit.createInventory(null, 27, Component.text("HOMES"));

        for (int i = 0; i < HOME_SLOTS.length; i++) {
            int slot = HOME_SLOTS[i];
            int deleteSlot = DELETE_BUTTON_SLOTS[i];

            if (i < homes.size()) {
                Home home = homes.get(i);
                ItemStack homeItem = createHomeItem(home);
                ItemStack deleteButton = createDeleteButton(home);

                inventory.setItem(slot, homeItem);
                inventory.setItem(deleteSlot, deleteButton);
            } else {
                if (i < maxHomes) {
                    ItemStack emptySlot = createEmptySlot();
                    inventory.setItem(slot, emptySlot);
                } else {
                    ItemStack lockedSlot = createLockedSlot();
                    inventory.setItem(slot, lockedSlot);
                }
                inventory.setItem(deleteSlot, null);
            }
        }

        player.openInventory(inventory);
    }

    private ItemStack createHomeItem(Home home) {
        ItemStack item = new ItemStack(Material.BLUE_BED);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(MessageUtils.format("<aqua>" + home.getName()));

        List<Component> lore = new ArrayList<>();
        lore.add(MessageUtils.format("<gray>Click to teleport"));
        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createEmptySlot() {
        ItemStack item = new ItemStack(Material.GRAY_BED);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(MessageUtils.format("<gray>No Home Set"));

        List<Component> lore = new ArrayList<>();
        lore.add(MessageUtils.format("<gray>Use /sethome <name>"));
        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createLockedSlot() {
        ItemStack item = new ItemStack(Material.GRAY_BED);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(MessageUtils.format("<red>Home Slot Locked"));

        List<Component> lore = new ArrayList<>();
        lore.add(MessageUtils.format("<gray>Upgrade your rank to"));
        lore.add(MessageUtils.format("<gray>unlock more homes"));
        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createDeleteButton(Home home) {
        ItemStack item = new ItemStack(Material.BLUE_DYE);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(MessageUtils.format("<red>Delete Home"));

        List<Component> lore = new ArrayList<>();
        lore.add(MessageUtils.format("<gray>Click to delete"));
        lore.add(MessageUtils.format("<aqua>" + home.getName()));
        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public static int[] getHomeSlots() {
        return HOME_SLOTS;
    }

    public static int[] getDeleteButtonSlots() {
        return DELETE_BUTTON_SLOTS;
    }
}
