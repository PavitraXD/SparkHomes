package com.pavitraxd.sparkhomes.commands;

import com.pavitraxd.sparkhomes.SparkHomes;
import com.pavitraxd.sparkhomes.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final SparkHomes plugin;

    public ReloadCommand(SparkHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("sparkhomes.admin.reload")) {
            MessageUtils.sendMessage(sender, "<red>You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            MessageUtils.sendMessage(sender, "<red>Usage: /sparkhomes reload");
            return true;
        }

        plugin.getConfigManager().reload();
        plugin.getHomeManager().clearCache();
        MessageUtils.sendMessage(sender, "<green>SparkHomes configuration reloaded successfully!");
        return true;
    }
}
