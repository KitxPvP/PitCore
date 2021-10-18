package com.kitx.permanent.impl;

import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import com.kitx.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@PerkInfo(name = "&cFishingRod", desc = "&aReceive a fishing rod on load out", cost = 1000, icon = Material.FISHING_ROD)
public class FishingRodPerk extends Perk {

    @Override
    public void onClick(PlayerData player) {
        if (player.getPerks().contains(this)) {
            player.getPlayer().getInventory().remove(ItemUtils.createItem(Material.FISHING_ROD));
        } else {
            player.getPlayer().getInventory().addItem(ItemUtils.createItem(Material.FISHING_ROD));
        }
        super.onClick(player);
    }


    @Override
    public void onLayout(PlayerData player) {
        player.getPlayer().getInventory().addItem(ItemUtils.createItem(Material.FISHING_ROD));
        super.onLayout(player);
    }
}
