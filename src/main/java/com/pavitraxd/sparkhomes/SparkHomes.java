package com.pavitraxd.sparkhomes;

import com.pavitraxd.sparkhomes.commands.*;
import com.pavitraxd.sparkhomes.gui.HomesGUI;
import com.pavitraxd.sparkhomes.listeners.GUIListener;
import com.pavitraxd.sparkhomes.listeners.PlayerJoinListener;
import com.pavitraxd.sparkhomes.listeners.TeleportListener;
import com.pavitraxd.sparkhomes.managers.ConfigManager;
import com.pavitraxd.sparkhomes.managers.HomeManager;
import com.pavitraxd.sparkhomes.managers.TeleportManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SparkHomes extends JavaPlugin {
    private ConfigManager configManager;
    private HomeManager homeManager;
    private TeleportManager teleportManager;
    private HomesGUI homesGUI;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        configManager = new ConfigManager(this);
        homeManager = new HomeManager(this);
        teleportManager = new TeleportManager(this);
        homesGUI = new HomesGUI(this);

        registerCommands();
        registerListeners();

        getLogger().info("SparkHomes has been enabled!");
    }

    @Override
    public void onDisable() {
        if (homeManager != null) {
            homeManager.saveAllHomes().join();
        }
        if (teleportManager != null) {
            teleportManager.cleanup();
        }
        getLogger().info("SparkHomes has been disabled!");
    }

    private void registerCommands() {
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("delhome").setExecutor(new DelHomeCommand(this));
        getCommand("homes").setExecutor(new HomesCommand(this));
        getCommand("sparkhomes").setExecutor(new ReloadCommand(this));

        getCommand("sethome").setTabCompleter(new SetHomeCommand(this));
        getCommand("home").setTabCompleter(new HomeCommand(this));
        getCommand("delhome").setTabCompleter(new DelHomeCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public HomesGUI getHomesGUI() {
        return homesGUI;
    }
}
