package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.PitCorePlugin;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

@PerkInfo(name = "&aStrength-Chaining", desc = "&c+8% damage &7for 7s stacking on kill", icon = Material.REDSTONE, cost = 2600)
public class StrengthChaining extends Perk implements Listener {
    public StrengthChaining() {
        Bukkit.getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            PlayerData data = DataManager.INSTANCE.get(((Player) event.getDamager()).getPlayer());
            if (System.currentTimeMillis() - data.getLastKill() < 7000) {
                double dmgAdd = event.getDamage() * data.getDamageMultiplier();
                if (data.getPerks().contains(this)) {
                    event.setDamage(event.getDamage() + dmgAdd);
                }
            } else {
                data.setDamageMultiplier(0);
            }
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            PlayerData killerData = DataManager.INSTANCE.get(killer);
            killerData.setLastKill(System.currentTimeMillis());
            if(killerData.getDamageMultiplier() == 0) {
                killerData.setDamageMultiplier(0.08);
            } else {
                killerData.setDamageMultiplier(Math.min(1, killerData.getDamageMultiplier() * 2));
            }
        }
    }

}
