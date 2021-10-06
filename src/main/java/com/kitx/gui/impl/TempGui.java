package com.kitx.gui.impl;

import com.kitx.data.PlayerData;
import com.kitx.gui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TempGui extends Menu {
    public TempGui(PlayerData data) {
        super(data, Bukkit.createInventory(null, 27, "Non-permanent items"));
    }

    @Override
    public void loadMenu() {
        inventory.setItem(11, createGuiItem(Material.DIAMOND_SWORD, "&eDiamond Sword", "&7Equips in hand on buy!","","&7&oLost on death.", "&7Cost&f: &6150g", "&eClick to purchase!"));

        ItemStack itemStack = createGuiItem(Material.OBSIDIAN, "&eObsidian", "&7Remains for 120 seconds.", "", "&7&oLost on death.", "&7Cost&f: &650g", "&eClick to purchase!");
        itemStack.setAmount(8);
        inventory.setItem(12, itemStack);

        inventory.setItem(14, createGuiItem(Material.DIAMOND_CHESTPLATE, "&eDiamond Chestplate",  "&7Auto-equips on buy!", "", "&7&oLost on death.", "&7Cost&f: &6500g", "&eClick to purchase!"));
        inventory.setItem(15, createGuiItem(Material.DIAMOND_BOOTS, "&bDiamond Boots", "&7Auto-equips on buy!", "", "&7&oLost on death.", "&7Cost&f: &6300g", "&eClick to purchase!"));
    }
}
