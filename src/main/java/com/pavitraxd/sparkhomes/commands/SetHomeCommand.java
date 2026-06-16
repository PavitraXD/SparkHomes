package com.pavitraxd.sparkhomes.commands;

import com.pavitraxd.sparkhomes.SparkHomes;
import com.pavitraxd.sparkhomes.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetHomeCommand implements CommandExecutor, TabCompleter {
    private final SparkHomes plugin;

    public SetHomeCommand(SparkHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "<red>This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("sparkhomes.sethome")) {
            MessageUtils.sendMessage(player, "<red>You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            MessageUtils.sendMessage(player, "<red>Usage: /sethome <name>");
            return true;
        }

        String name = args[0];
        UUID uuid = player.getUniqueId();

        if (!isValidName(name)) {
            MessageUtils.sendMessage(player, "<red>Invalid home name. Use only letters, numbers, underscores, and hyphens.");
            return true;
        }

        if (name.length() > plugin.getConfigManager().getMaxNameLength()) {
            MessageUtils.sendMessage(player, "<red>Home name is too long. Maximum length: " + plugin.getConfigManager().getMaxNameLength() + " characters.");
            return true;
        }

        plugin.getHomeManager().loadPlayerHomes(uuid).thenRun(() -> {
            if (plugin.getHomeManager().hasHome(uuid, name)) {
                MessageUtils.sendMessage(player, "<red>A home with that name already exists.");
                return;
            }

            int maxHomes = plugin.getHomeManager().getMaxHomes(player);
            int currentHomes = plugin.getHomeManager().getHomeCount(uuid);

            if (currentHomes >= maxHomes) {
                MessageUtils.sendMessage(player, "<red>You have reached your home limit (" + maxHomes + ").");
                return;
            }

            plugin.getHomeManager().createHome(player, name).thenAccept(success -> {
                if (success) {
                    if (plugin.getConfigManager().isMessagesEnabled()) {
                        MessageUtils.sendMessage(player, "<green>Home <aqua>" + name + " <green>created successfully!");
                    }
                } else {
                    MessageUtils.sendMessage(player, "<red>Failed to create home. No available slots.");
                }
            });
        });

        return true;
    }

    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z0-9_-]+$");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (sender instanceof Player && args.length == 1) {
            completions.add("<name>");
        }
        return completions;
    }
}
