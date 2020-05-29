package pl.memexurer.guildpanel.data;

import org.bukkit.Material;
import pl.memexurer.guildpanel.util.chat.ChatUtil;

public enum PanelPermission {
    BEACON(Material.BEACON, "zarzadzania bekonem"),
    BLOCK_PLACE(Material.GRASS, "stawiania klocow"),
    BLOCK_BREAK(Material.STONE, "niszczenia klocow"),
    TNT_PLACEMENT(Material.TNT, "stawiania tnt"),
    FLUID_PLACEMENT(Material.BUCKET, "rozlewanie wody/lavy"),
    TELEPORTATION_USE(Material.NETHER_STAR, "uzywanie /tpaccept na terenie gildii"),
    MEMBER_INVITE(Material.PAPER, "zapraszanie czlonkow"),
    MEMBER_KICK(Material.BARRIER, "wyrzucanie czlonkow"),
    GUILD_PROLONG(Material.BONE, "przedluzanie dlugosci gildii");

    private String message;
    private Material permissionIcon;
    private String permissionName;

    PanelPermission(Material permissionIcon, String permissionName) {
        this.permissionIcon = permissionIcon;
        this.permissionName = permissionName;
    }

    public static PanelPermission byName(String str) {
        for (PanelPermission permission : values())
            if (permission.name().equalsIgnoreCase(str))
                return permission;
        return null;
    }

    public Material getPermissionIcon() {
        return permissionIcon;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = ChatUtil.fixColor(message);
    }
}
