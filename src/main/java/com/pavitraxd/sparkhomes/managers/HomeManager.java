package com.pavitraxd.sparkhomes.managers;

import com.pavitraxd.sparkhomes.SparkHomes;
import com.pavitraxd.sparkhomes.models.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class HomeManager {
    private final SparkHomes plugin;
    private final Map<UUID, List<Home>> homesCache;
    private final File homesFile;
    private FileConfiguration homesConfig;

    private static final int[] HOME_SLOTS = {12, 13, 14, 15, 16};

    public HomeManager(SparkHomes plugin) {
        this.plugin = plugin;
        this.homesCache = new ConcurrentHashMap<>();
        this.homesFile = new File(plugin.getDataFolder(), "homes.yml");
        loadHomesConfig();
    }

    private void loadHomesConfig() {
        if (!homesFile.exists()) {
            plugin.saveResource("homes.yml", false);
        }
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);
    }

    public CompletableFuture<Void> loadPlayerHomes(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            ConfigurationSection playerSection = homesConfig.getConfigurationSection(uuid.toString());
            if (playerSection != null) {
                List<Home> homes = new ArrayList<>();
                for (String homeName : playerSection.getKeys(false)) {
                    ConfigurationSection homeSection = playerSection.getConfigurationSection(homeName);
                    if (homeSection != null) {
                        String world = homeSection.getString("world");
                        double x = homeSection.getDouble("x");
                        double y = homeSection.getDouble("y");
                        double z = homeSection.getDouble("z");
                        float yaw = (float) homeSection.getDouble("yaw");
                        float pitch = (float) homeSection.getDouble("pitch");
                        int slot = homeSection.getInt("slot", -1);

                        Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                        Home home = new Home(uuid, homeName, location, slot);
                        homes.add(home);
                    }
                }
                homesCache.put(uuid, homes);
            } else {
                homesCache.put(uuid, new ArrayList<>());
            }
        });
    }

    public CompletableFuture<Void> savePlayerHomes(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            List<Home> homes = homesCache.get(uuid);
            if (homes == null || homes.isEmpty()) {
                homesConfig.set(uuid.toString(), null);
            } else {
                ConfigurationSection playerSection = homesConfig.createSection(uuid.toString());
                for (Home home : homes) {
                    ConfigurationSection homeSection = playerSection.createSection(home.getName());
                    homeSection.set("world", home.getWorld());
                    homeSection.set("x", home.getX());
                    homeSection.set("y", home.getY());
                    homeSection.set("z", home.getZ());
                    homeSection.set("yaw", home.getYaw());
                    homeSection.set("pitch", home.getPitch());
                    homeSection.set("slot", home.getSlot());
                }
            }
            try {
                homesConfig.save(homesFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> saveAllHomes() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (UUID uuid : homesCache.keySet()) {
            futures.add(savePlayerHomes(uuid));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public List<Home> getPlayerHomes(UUID uuid) {
        return homesCache.computeIfAbsent(uuid, k -> new ArrayList<>());
    }

    public Home getHome(UUID uuid, String name) {
        List<Home> homes = getPlayerHomes(uuid);
        for (Home home : homes) {
            if (home.getName().equalsIgnoreCase(name)) {
                return home;
            }
        }
        return null;
    }

    public boolean hasHome(UUID uuid, String name) {
        return getHome(uuid, name) != null;
    }

    public CompletableFuture<Boolean> createHome(Player player, String name) {
        return CompletableFuture.supplyAsync(() -> {
            UUID uuid = player.getUniqueId();
            if (hasHome(uuid, name)) {
                return false;
            }

            List<Home> homes = getPlayerHomes(uuid);
            int slot = findAvailableSlot(homes);
            if (slot == -1) {
                return false;
            }

            Home home = new Home(uuid, name, player.getLocation(), slot);
            homes.add(home);
            homesCache.put(uuid, homes);
            savePlayerHomes(uuid);
            return true;
        });
    }

    public CompletableFuture<Boolean> deleteHome(UUID uuid, String name) {
        return CompletableFuture.supplyAsync(() -> {
            List<Home> homes = getPlayerHomes(uuid);
            Home homeToRemove = null;
            for (Home home : homes) {
                if (home.getName().equalsIgnoreCase(name)) {
                    homeToRemove = home;
                    break;
                }
            }

            if (homeToRemove != null) {
                homes.remove(homeToRemove);
                homesCache.put(uuid, homes);
                savePlayerHomes(uuid);
                return true;
            }
            return false;
        });
    }

    public int getHomeCount(UUID uuid) {
        return getPlayerHomes(uuid).size();
    }

    public int getMaxHomes(Player player) {
        if (player.isOp()) {
            return Integer.MAX_VALUE;
        }

        int max = plugin.getConfigManager().getMaxDefaultHomes();
        for (int i = 5; i >= 2; i--) {
            if (player.hasPermission("sparkhomes.limit." + i)) {
                max = i;
                break;
            }
        }
        return max;
    }

    private int findAvailableSlot(List<Home> homes) {
        Set<Integer> usedSlots = new HashSet<>();
        for (Home home : homes) {
            usedSlots.add(home.getSlot());
        }

        for (int slot : HOME_SLOTS) {
            if (!usedSlots.contains(slot)) {
                return slot;
            }
        }
        return -1;
    }

    public int[] getHomeSlots() {
        return HOME_SLOTS;
    }

    public void clearCache() {
        homesCache.clear();
    }
}
