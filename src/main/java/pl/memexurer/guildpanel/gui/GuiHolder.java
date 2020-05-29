package pl.memexurer.guildpanel.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiHolder implements InventoryHolder {
    private GuiBuilder builder;

    public GuiHolder(GuiBuilder builder) {
        this.builder = builder;
    }

    public void execute(InventoryClickEvent e) {
        builder.handleClick(e);
    }

    @Override
    public Inventory getInventory() {
        return builder.build();
    }

}
