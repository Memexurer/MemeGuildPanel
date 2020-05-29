package pl.memexurer.guildpanel.gui;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.memexurer.guildpanel.util.chat.ChatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuiItem {
    private GuiItemExecutor executor;
    private ItemStack itemStack;

    private GuiItem(GuiItemExecutor executor, ItemStack itemStack) {
        this.executor = executor;
        this.itemStack = itemStack;
    }

    public void execute(InventoryClickEvent e) {
        if (this.executor != null)
            this.executor.execute(e);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static class Builder {
        private GuiItemExecutor executor;
        private ItemStack itemStack;

        public Builder(Material material) {
            this.itemStack = new ItemStack(material, 1);
        }

        public Builder(ItemStack item) {
            this.itemStack = item;
        }

        public Builder withClickExecutor(GuiItemExecutor executor) {
            this.executor = executor;
            return this;
        }

        public Builder withItemName(String itemName) {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + ChatUtil.fixColor(itemName));
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder withLore(List<String> lore) {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(lore.stream().map(ChatUtil::fixColor).collect(Collectors.toList()));
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder withLore(String... lore) {
            return withLore(Arrays.asList(lore));
        }

        public Builder withAmount(int amount) {
            itemStack.setAmount(amount);
            return this;
        }

        public Builder withItemData(short data) {
            itemStack.setDurability(data);
            return this;
        }

        public Builder withColor(DyeColor color) {
            if (itemStack.getType() == Material.INK_SACK)
                return withItemData(color.getDyeData());
            else
                return withItemData(color.getWoolData());
        }

        public Builder withEnchantment(boolean enabled) {
            if (!enabled) return this;

            ItemMeta meta = itemStack.getItemMeta();
            meta.addEnchant(Enchantment.OXYGEN, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(meta);
            return this;
        }

        public Builder withSkullOwner(String ownerName) {
            Validate.isTrue(itemStack.getItemMeta() instanceof SkullMeta, "Not an skull.");
            if (itemStack.getDurability() != 3) itemStack.setDurability((short) 3);
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setOwner(ownerName);
            itemStack.setItemMeta(meta);
            return this;
        }

        public GuiItem build() {
            return new GuiItem(executor, itemStack);
        }

        public Builder addLore(String s) {
            List<String> lore = itemStack.getItemMeta().getLore();
            lore.add(s);

            return withLore(lore);
        }

        public Builder addLore(List<String> lore) {
            List<String> lor = itemStack.getItemMeta().getLore();
            lor.addAll(lore);

            return withLore(lor);
        }
    }
}
