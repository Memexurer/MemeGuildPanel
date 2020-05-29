package pl.memexurer.guildpanel.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import pl.memexurer.guildpanel.util.chat.ChatUtil;

import java.util.HashMap;
import java.util.Map;

public class GuiBuilder {
    private int size;
    private String title;
    private InventoryType type;
    private HashMap<Integer, GuiItem> items;

    public GuiBuilder(int rows, String title) {
        this.size = rows * 9;
        this.title = ChatUtil.fixColor(title);
        this.items = new HashMap<>();
        this.type = null;
    }

    public GuiBuilder(int rows, String title, boolean allowMoveItem, ClickType... allowedClickTypes) {
        this.size = rows * 9;
        this.title = ChatUtil.fixColor(title);
        this.items = new HashMap<>();
        this.type = null;
    }

    public GuiBuilder(String title, InventoryType type) {
        this.title = ChatUtil.fixColor(title);
        this.items = new HashMap<>();
        this.type = type;
    }


    public GuiBuilder setItem(int y, int x, GuiItem item) {
        items.put((y * 9) + x, item);
        return this;
    }

    public GuiBuilder setItem(int raw, GuiItem item) {
        items.put(raw, item);
        return this;
    }

    public Inventory build() {
        Inventory inv;
        if (type != null) {
            inv = Bukkit.createInventory(new GuiHolder(this), type, title);
        } else {
            inv = Bukkit.createInventory(new GuiHolder(this), size, title);
        }
        for (Map.Entry<Integer, GuiItem> entry : items.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
        return inv;
    }

    public void handleClick(InventoryClickEvent e) {
        if (!items.containsKey(e.getRawSlot())) return;
        if (items.get(e.getRawSlot()) == null) return;
        items.get(e.getRawSlot()).execute(e);
    }

    public String getTitle() {
        return title;
    }

}
