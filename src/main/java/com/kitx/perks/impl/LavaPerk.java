package com.kitx.perks.impl;

import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@PerkInfo(name = "&6Lava Perk", desc = "&aReceive a lava bucket", cost = 350, icon = Material.LAVA_BUCKET)
public class LavaPerk extends Perk {

    @Override
    public void onClick(PlayerData player) {
        if (player.getPerks().contains(this)) {
            player.getPlayer().getInventory().remove(Material.LAVA_BUCKET);
        } else {
            player.getPlayer().getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
        }
        super.onClick(player);
    }

    @Override
    public void onLayout(PlayerData player) {
        player.getPlayer().getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
        super.onLayout(player);
    }
}
