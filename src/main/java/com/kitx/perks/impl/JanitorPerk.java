package com.kitx.perks.impl;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@PerkInfo(name = "&bJanitor", desc = "&c10% &7extra damage to players below half health", cost = 3000, icon = Material.LEASH)
public class JanitorPerk extends Perk {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            final Player entity = ((Player) event.getEntity()).getPlayer();
            final PlayerData data = DataManager.INSTANCE.get(((Player) event.getDamager()).getPlayer());
            if(data.getPerks().contains(this) && entity.getHealth() < entity.getMaxHealth() / 2) {
                final double percent = event.getDamage() * .10;
                event.setDamage(event.getDamage() + percent);
            }
        }
    }
}
