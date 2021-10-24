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
public class EndlessQuiver extends Perk implements Listener {
    public EndlessQuiver() {
        Bukkit.getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            PlayerData data = DataManager.INSTANCE.get(event.getEntity().getKiller());
            if (data.getPerks().contains(this)) {
                ItemStack itemStack = new ItemStack(Material.ARROW);
                itemStack.setAmount(3);
                data.getPlayer().getInventory().addItem(itemStack);
            }
        }
    }
}
