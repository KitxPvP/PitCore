package com.kitx.gui.impl;

import com.kitx.data.PlayerData;
import com.kitx.gui.Menu;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkLoader;
import org.bukkit.Bukkit;

public class PermGui extends Menu {
    public PermGui(PlayerData data) {
        super(data, Bukkit.createInventory(null, 27, "Perks"));
    }

    @Override
    public void loadMenu() {
        for (Perk perk : PerkLoader.INSTANCE.getPerkList()) {
            inventory.addItem(createGuiItem(perk.getIcon(), perk.getName(), perk.getDesc(), "&6Price&7: &6" + perk.getCost(), data.getPurchasedPerks().contains(perk) ? data.getPerks().contains(perk) ? "&cClick to unselect" : "&aClick to select" : "&aClick to buy"));
        }
    }
}
