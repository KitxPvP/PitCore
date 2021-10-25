package com.kitx.gui.impl;

import com.kitx.data.PlayerData;
import com.kitx.gui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class PrestigeGui extends Menu {
    public PrestigeGui(PlayerData data) {
        super(data, Bukkit.createInventory(null, 27, "Prestige"));
    }

    @Override
    public void loadMenu() {
        inventory.setItem(13, createGuiItem(Material.DIAMOND, "&bPrestige", "&7Current: " + data.getPrestige(), "&7Required Level: 120", "&7Costs:", "&cResets &blevel to 1", "&cResets &6gold to 0", "&cResets &aALL perks and upgrades", "", "&eClick to purchase!"));
    }
}
