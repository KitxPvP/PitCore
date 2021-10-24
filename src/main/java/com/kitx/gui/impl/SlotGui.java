package com.kitx.gui.impl;

import com.kitx.data.PlayerData;
import com.kitx.gui.Menu;
import com.kitx.permanent.Perk;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class SlotGui extends Menu {
    public SlotGui(PlayerData data) {
        super(data, Bukkit.createInventory(null, 27, "Perk Slots"));
    }

    @Override
    public void loadMenu() {
        for (int i = 0; i < 3; i++) {
            Perk perk = null;
            boolean isNull = true;
            try {
                isNull = data.getPerks().get(i) == null;
                perk = data.getPerks().get(i);
            } catch (Exception ignored) {
                //Ignored
            }

            if (i == 0) {
                inventory.setItem(12 + i, createGuiItem(isNull ? Material.DIAMOND_BLOCK : perk.getIcon(),
                        "&ePerk Slot #" + (i + 1),
                        "&7Selected: " + (isNull ? "None" : perk.getName()),
                        "",
                        "&eClick to choose perk!"));
            } else {
                if ((i == 1 && data.getLevel() >= 35) || (i == 2 && data.getLevel() >= 75)) {
                    inventory.setItem(12 + i, createGuiItem(isNull ? Material.GOLD_BLOCK : perk.getIcon(),
                            "&ePerk Slot #" + (i + 1),
                            "&7Selected: " + (isNull ? "None" : perk.getName()),
                            "",
                            "&eClick to choose perk!"));
                } else {
                    inventory.setItem(12 + i, createGuiItem(Material.BEDROCK, "&ePerk Slot #" + (i + 1), "&cRequires level " + (i == 1 ? "35" : "75")));
                }
            }
        }
    }
}