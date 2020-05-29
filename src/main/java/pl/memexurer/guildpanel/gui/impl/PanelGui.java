package pl.memexurer.guildpanel.gui.impl;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import pl.memexurer.guildpanel.data.PanelPermission;
import pl.memexurer.guildpanel.data.PanelUser;
import pl.memexurer.guildpanel.gui.GuiBuilder;
import pl.memexurer.guildpanel.gui.GuiItem;

import java.util.Map;

public class PanelGui extends GuiBuilder {
    private static final GuiItem ENABLED_ITEM = new GuiItem.Builder(Material.STAINED_GLASS_PANE)
            .withColor(DyeColor.LIME)
            .withItemName("&aWlaczone")
            .build();

    private static final GuiItem DISABLED_ITEM = new GuiItem.Builder(Material.STAINED_GLASS_PANE)
            .withColor(DyeColor.RED)
            .withItemName("&cWylaczone")
            .build();

    private PanelGui(String playerName, PanelUser user) {
        super(2, "Permisje gracza " + playerName);
        int slot = 0;

        for (Map.Entry<PanelPermission, Boolean> entry : user.getPermissions()) {
            setItem(slot, new GuiItem.Builder(entry.getKey().getPermissionIcon())
                    .withItemName("&7Permisja " + entry.getKey().getPermissionName())
                    .withLore("&7Kliknij aby " + (entry.getValue() ? "&awlaczyc" : "&cwylaczyc") + " &7permisje " + entry.getKey().getPermissionName())
                    .withClickExecutor(e -> {
                        user.togglePermission(entry.getKey());
                        openInventory(e.getWhoClicked(), playerName, user);
                    })
                    .build());
            setItem(slot + 9, entry.getValue() ? DISABLED_ITEM : ENABLED_ITEM);
            slot++;
        }
    }

    public static void openInventory(HumanEntity entity, String playerName, PanelUser user) {
        entity.openInventory(new PanelGui(playerName, user).build());
    }
}
