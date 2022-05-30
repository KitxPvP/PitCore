package com.kitx.perks.impl;

import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import com.kitx.utils.ItemUtils;
import org.bukkit.Material;

@PerkInfo(name = "&aSafety First", desc = "&7Spawn with a helmet", icon = Material.CHAINMAIL_HELMET, cost = 3000)
public class SafetyFirstPerk extends Perk {
    @Override
    public void onLayout(PlayerData player) {
        player.getPlayer().getInventory().setHelmet(ItemUtils.createItem(Material.CHAINMAIL_HELMET));
        super.onLayout(player);
    }

    @Override
    public void onClick(PlayerData player) {
        player.getPlayer().getInventory().setHelmet(ItemUtils.createItem(Material.CHAINMAIL_HELMET));
        super.onClick(player);
    }
}
