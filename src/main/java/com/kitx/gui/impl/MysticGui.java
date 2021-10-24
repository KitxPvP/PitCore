package com.kitx.gui.impl;

import com.kitx.data.PlayerData;
import com.kitx.gui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class MysticGui extends Menu {
    public MysticGui(PlayerData data) {
        super(data, Bukkit.createInventory(null, 27, "Mystic Well"));
    }

    @Override
    public void loadMenu() {
        inventory.setItem(13, createGuiItem(Material.ENDER_CHEST, "&dMystic Well", "&7Find a &bMystic Bow &7or", "&eMystic Sword &7from killing", "&7players.", "", "&7Enchant these items in the well for tons of buffs.", "", "&dClick an item in your inventory."));
        //Hard coded cirlce lmfao
        inventory.setItem(9, createGuiItem(Material.STAINED_GLASS_PANE, ""));
        inventory.setItem(0, createGuiItem(Material.STAINED_GLASS_PANE, ""));
        inventory.setItem(18, createGuiItem(Material.STAINED_GLASS_PANE, ""));
        inventory.setItem(19, createGuiItem(Material.STAINED_GLASS_PANE, ""));
        inventory.setItem(20, createGuiItem(Material.STAINED_GLASS_PANE, ""));
        inventory.setItem(11, createGuiItem(Material.STAINED_GLASS_PANE, ""));
        inventory.setItem(2, createGuiItem(Material.STAINED_GLASS_PANE, ""));
        inventory.setItem(1, createGuiItem(Material.STAINED_GLASS_PANE, ""));
    }
}
