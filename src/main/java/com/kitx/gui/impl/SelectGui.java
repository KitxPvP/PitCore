package com.kitx.gui.impl;

import com.kitx.data.PlayerData;
import com.kitx.gui.Menu;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class SelectGui extends Menu {
    public SelectGui(PlayerData data, int slot) {
        super(data, Bukkit.createInventory(null, 27, "Perks " + slot));
    }

    @Override
    public void loadMenu() {
        for (Perk perk : PerkLoader.INSTANCE.getPerkList()) {
            inventory.addItem(createGuiItem(perk.getIcon(), perk.getName(), perk.getDesc(), "&7Price: &6" + perk.getCost(), data.getPurchasedPerks().contains(perk) ? data.getPerks().contains(perk) ? "&cAlready selected" : "&aClick to select" : "&aClick to buy"));
        }

        inventory.setItem(26, createGuiItem(Material.ARROW, "&cBack"));
    }
}
