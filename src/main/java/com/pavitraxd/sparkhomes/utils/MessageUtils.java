package com.pavitraxd.sparkhomes.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class MessageUtils {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static void sendMessage(CommandSender sender, String message) {
        if (message == null || message.isEmpty()) return;
        sender.sendMessage(MINI_MESSAGE.deserialize(message));
    }

    public static Component format(String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }
        return MINI_MESSAGE.deserialize(message);
    }

    public static String formatString(String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }
        return MINI_MESSAGE.stripTags(message);
    }
}
