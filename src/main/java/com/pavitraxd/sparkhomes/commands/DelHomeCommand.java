package com.pavitraxd.sparkhomes.commands;

import com.pavitraxd.sparkhomes.SparkHomes;
import com.pavitraxd.sparkhomes.models.Home;
import com.pavitraxd.sparkhomes.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DelHomeCommand implements CommandExecutor, TabCompleter {
    private final SparkHomes plugin;

    public DelHomeCommand(SparkHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "<red>This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("sparkhomes.delhome")) {
            MessageUtils.sendMessage(player, "<red>You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            MessageUtils.sendMessage(player, "<red>Usage: /delhome <name>");
            return true;
        }

        String name = args[0];
        UUID uuid = player.getUniqueId();

        plugin.getHomeManager().loadPlayerHomes(uuid).thenRun(() -> {
            if (!plugin.getHomeManager().hasHome(uuid, name)) {
                MessageUtils.sendMessage(player, "<red>Home <aqua>" + name + " <red>not found.");
                return;
            }

            plugin.getHomeManager().deleteHome(uuid, name).thenAccept(success -> {
                if (success) {
                    if (plugin.getConfigManager().isMessagesEnabled()) {
                        MessageUtils.sendMessage(player, "<green>Home <aqua>" + name + " <green>deleted successfully!");
                    }
                } else {
                    MessageUtils.sendMessage(player, "<red>Failed to delete home.");
                }
            });
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (sender instanceof Player && args.length == 1) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            completions = plugin.getHomeManager().getPlayerHomes(uuid).stream()
                    .map(Home::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return completions;
    }
}
