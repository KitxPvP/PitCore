package com.kitx.permanent.impl;

import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import com.kitx.utils.ItemUtils;
import org.bukkit.Material;

@PerkInfo(name = "&aSafety First", desc = "&7Spawn with a helmet", icon = Material.CHAINMAIL_HELMET, cost = 3000)
public class SafetyFirstPerk extends Perk {
    @Override
    public void onLayout(PlayerData player) {
        player.getPlayer().getInventory().setHelmet(ItemUtils.createItem(Material.CHAINMAIL_HELMET));
        super.onLayout(player);
    }
}
