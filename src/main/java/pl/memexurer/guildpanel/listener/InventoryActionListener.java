package pl.memexurer.guildpanel.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.memexurer.guildpanel.gui.GuiHolder;

public class InventoryActionListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof GuiHolder)) return;
        e.setCancelled(true);
        ((GuiHolder) e.getClickedInventory().getHolder()).execute(e);
    }
}
