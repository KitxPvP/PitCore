package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import com.kitx.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

@PerkInfo(name = "&eMineman", desc = "&7Spawn with &f24 cobblestone &7and a diamond pickaxe.", cost = 3000, icon = Material.COBBLESTONE)
public class Mineman extends Perk implements Listener {
    public Mineman() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            PlayerData data = DataManager.INSTANCE.get(event.getEntity().getKiller());
            if (data.getPerks().contains(this)) {
                ItemStack itemStack = new ItemStack(Material.COBBLESTONE);
                itemStack.setAmount(3);
                data.getPlayer().getInventory().addItem(itemStack);
            }
        }
    }

    @Override
    public void onLayout(PlayerData player) {
        player.getPlayer().getInventory().addItem(ItemUtils.createItem(Material.DIAMOND_PICKAXE));
        ItemStack itemStack = new ItemStack(Material.ARROW);
        itemStack.setAmount(30);
        player.getPlayer().getInventory().addItem(itemStack);
        super.onLayout(player);
    }
}
