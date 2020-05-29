package pl.memexurer.guildpanel.util.chat;

import org.bukkit.ChatColor;

public class ChatUtil {
    public static String fixColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
