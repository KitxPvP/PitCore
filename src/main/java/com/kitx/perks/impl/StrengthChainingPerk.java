package com.kitx.perks.impl;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@PerkInfo(name = "&aStrength-Chaining", desc = "&c+8% damage &7for 7s stacking on kill", icon = Material.REDSTONE, cost = 2600)
public class StrengthChainingPerk extends Perk{


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

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        if(killer.getDamageMultiplier() == 0) {
            killer.setDamageMultiplier(0.08);
        } else {
            killer.setDamageMultiplier(Math.min(1, killer.getDamageMultiplier() * 2));
        }
        super.onKill(killer, victim);
    }
}
