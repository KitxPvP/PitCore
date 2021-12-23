package com.kitx.perks.impl;

import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@PerkInfo(name = "&eSpeedster", desc = "&7Gain speed 2 for 5 seconds on kill", cost = 900, icon = Material.LEATHER_BOOTS)
public class SpeedsterPerk extends Perk {

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        PotionEffect regen = new PotionEffect(PotionEffectType.SPEED, 120, 1);
        killer.getPlayer().addPotionEffect(regen, true);
        super.onKill(killer, victim);
    }
}
