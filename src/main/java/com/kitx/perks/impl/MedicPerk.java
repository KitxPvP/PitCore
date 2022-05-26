package com.kitx.perks.impl;

import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import org.bukkit.Material;

@PerkInfo(name = "&cMedic", desc = "&7Have a &920% &7chance to gain full health", cost = 500, icon = Material.APPLE)
public class MedicPerk extends Perk {

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        final double r = randomNumber(1, 25);
        if(r > 20) {
            killer.getPlayer().setHealth(killer.getPlayer().getMaxHealth());
        }
        super.onKill(killer, victim);
    }

    double randomNumber(float max, float min) {
        return (min + Math.random() * ((max - min)));
    }
}
