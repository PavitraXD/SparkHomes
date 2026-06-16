package com.pavitraxd.sparkhomes.commands;

import com.pavitraxd.sparkhomes.SparkHomes;
import com.pavitraxd.sparkhomes.models.Home;
import com.pavitraxd.sparkhomes.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HomeCommand implements CommandExecutor, TabCompleter {
    private final SparkHomes plugin;

    public HomeCommand(SparkHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "<red>This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("sparkhomes.home")) {
            MessageUtils.sendMessage(player, "<red>You do not have permission to use this command.");
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (args.length == 0) {
            plugin.getHomeManager().loadPlayerHomes(uuid).thenRun(() -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getHomesGUI().open(player);
                });
            });
            return true;
        }

        String name = args[0];
        plugin.getHomeManager().loadPlayerHomes(uuid).thenRun(() -> {
            Home home = plugin.getHomeManager().getHome(uuid, name);
            if (home == null) {
                MessageUtils.sendMessage(player, "<red>Home <aqua>" + name + " <red>not found.");
                return;
            }

            plugin.getTeleportManager().startTeleport(player, home);
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
