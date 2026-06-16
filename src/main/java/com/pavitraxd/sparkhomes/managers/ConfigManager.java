package com.pavitraxd.sparkhomes.managers;

import com.pavitraxd.sparkhomes.SparkHomes;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final SparkHomes plugin;
    private FileConfiguration config;

    public ConfigManager(SparkHomes plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
        this.config = this.plugin.getConfig();
    }

    public void reload() {
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();
    }

    public int getTeleportDelay() {
        return config.getInt("teleport-delay", 5);
    }

    public int getTeleportCooldown() {
        return config.getInt("teleport-cooldown", 3);
    }

    public boolean isCancelOnMove() {
        return config.getBoolean("cancel-on-move", true);
    }

    public boolean isCancelOnDamage() {
        return config.getBoolean("cancel-on-damage", true);
    }

    public int getMaxDefaultHomes() {
        return config.getInt("max-default-homes", 2);
    }

    public int getMaxNameLength() {
        return config.getInt("max-name-length", 16);
    }

    public boolean isSoundsEnabled() {
        return config.getBoolean("sounds-enabled", true);
    }

    public boolean isMessagesEnabled() {
        return config.getBoolean("messages-enabled", true);
    }
}
