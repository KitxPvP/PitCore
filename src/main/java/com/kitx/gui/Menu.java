package com.kitx.gui;

import com.kitx.data.PlayerData;
import com.kitx.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class Menu {
    public final PlayerData data;
    public final Inventory inventory;

    public Menu(PlayerData data, Inventory inventory) {
        this.data = data;
        this.inventory = inventory;
        loadMenu();
    }

    public void openGui() {
        data.getPlayer().openInventory(inventory);
    }

    public abstract void loadMenu();

    public ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ColorUtil.translate(name));

        meta.setLore(ColorUtil.translate(Arrays.asList(lore)));

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack createGuiItem(final ItemStack material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material);
        item.setAmount(1);
        final ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(ColorUtil.translate(name));

        meta.setLore(ColorUtil.translate(Arrays.asList(lore)));

        item.setItemMeta(meta);

        return item;
    }

}