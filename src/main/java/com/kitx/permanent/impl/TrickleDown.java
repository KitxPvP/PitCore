package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.events.GoldPickEvent;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@PerkInfo(name = "&eTrickleDown", desc = "&7Gold ingots reward &6+10g &7and heal &c2♥", cost = 1000, icon = Material.GOLD_INGOT)
public class TrickleDown extends Perk implements Listener {
    public TrickleDown() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    @EventHandler
    public void onHit(GoldPickEvent event) {
        if (event.getPlayer().getPerks().contains(this)) {
            Player player = event.getPlayer().getPlayer();
            player.setHealth(Math.min(20, player.getHealth() + 4));
            event.setGold(10);
        }
    }
}
