package com.pavitraxd.sparkhomes.commands;

import com.pavitraxd.sparkhomes.SparkHomes;
import com.pavitraxd.sparkhomes.models.Home;
import com.pavitraxd.sparkhomes.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HomesCommand implements CommandExecutor {
    private final SparkHomes plugin;

    public HomesCommand(SparkHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "<red>This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("sparkhomes.use")) {
            MessageUtils.sendMessage(player, "<red>You do not have permission to use this command.");
            return true;
        }

        UUID uuid = player.getUniqueId();

        plugin.getHomeManager().loadPlayerHomes(uuid).thenRun(() -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getHomesGUI().open(player);
            });
        });

        return true;
    }
}
