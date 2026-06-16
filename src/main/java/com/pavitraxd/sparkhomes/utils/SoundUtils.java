package com.pavitraxd.sparkhomes.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {
    public static void playCountdownSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, 1.0f);
    }

    public static void playSuccessSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
    }

    public static void playCancelSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
    }
}
