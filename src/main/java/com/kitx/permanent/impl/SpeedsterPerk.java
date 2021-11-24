package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@PerkInfo(name = "&eSpeedster", desc = "&7Gain speed 2 for 5 seconds on kill", cost = 900, icon = Material.LEATHER_BOOTS)
public class SpeedsterPerk extends Perk implements Listener {

    public SpeedsterPerk() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        PotionEffect regen = new PotionEffect(PotionEffectType.SPEED, 120, 1);
        killer.getPlayer().addPotionEffect(regen, true);
        super.onKill(killer, victim);
    }
}
