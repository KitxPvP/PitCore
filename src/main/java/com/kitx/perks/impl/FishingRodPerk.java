package com.kitx.perks.impl;

import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import com.kitx.utils.ItemUtils;
import org.bukkit.Material;

@PerkInfo(name = "&aFishingRod", desc = "&aReceive a fishing rod on load out", cost = 550, icon = Material.FISHING_ROD)
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
