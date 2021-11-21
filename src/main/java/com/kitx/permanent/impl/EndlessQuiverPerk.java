package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

@PerkInfo(name = "&eEndless Quiver", desc = "&7Get &f3 arrows &7on arrow hit.", cost = 2000, icon = Material.BOW)
public class EndlessQuiverPerk extends Perk implements Listener {
    public EndlessQuiverPerk() {
        Bukkit.getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        itemStack.setAmount(3);
        killer.getPlayer().getInventory().addItem(itemStack);
        super.onKill(killer, victim);
    }
}
