package com.kitx.perks.impl;

import com.kitx.events.GoldPickEvent;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

@PerkInfo(name = "&eTrickleDown", desc = "&7Gold ingots reward &6+10g &7and heal &c2♥", cost = 1000, icon = Material.GOLD_INGOT)
public class TrickleDownPerk extends Perk {

    @EventHandler
    public void onGold(GoldPickEvent event) {
        if (event.getPlayer().getPerks().contains(this)) {
            Player player = event.getPlayer().getPlayer();
            player.setHealth(Math.min(20, player.getHealth() + 4));
            event.setGold(10);
        }
    }
}
