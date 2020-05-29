package pl.memexurer.guildpanel.util.chat;

import org.bukkit.entity.Player;

public class Title {
    private String title;
    private String subtitle;

    public Title(String title, String subtitle) {
        this.title = ChatUtil.fixColor(title);
        this.subtitle = ChatUtil.fixColor(subtitle);
    }

    public void send(Player player) {
        player.sendTitle(title, subtitle);
    }

    public void send(Player player, String replaced, String replacement) {
        player.sendTitle(title.replace(replaced, replacement), subtitle.replace(replaced, replacement));
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
