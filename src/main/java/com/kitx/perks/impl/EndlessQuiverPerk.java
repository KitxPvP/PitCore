package com.kitx.perks.impl;

import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@PerkInfo(name = "&eEndless Quiver", desc = "&7Get &f3 arrows &7on arrow hit.", cost = 2000, icon = Material.BOW)
public class EndlessQuiverPerk extends Perk implements Listener {

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        itemStack.setAmount(3);
        killer.getPlayer().getInventory().addItem(itemStack);
        super.onKill(killer, victim);
    }
}
