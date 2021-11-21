package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@PerkInfo(
        name = "&2Dirty",
        desc = "&7Killing a player grants &c40% &7damage reduction for &94 &7seconds. Requires prestige 2",
        cost = 700, requiredPrestige = 2,
        icon = Material.DIRT
)
public class DirtyPerk extends Perk {

    public DirtyPerk() {
        Bukkit.getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            PlayerData data = DataManager.INSTANCE.get(((Player) event.getEntity()).getPlayer());
            if (System.currentTimeMillis() - data.getLastKill() < 4000) {
                double dmgAdd = event.getDamage() * data.getDamageMultiplier();
                if (data.getPerks().contains(this)) {
                    event.setDamage(Math.max(0, event.getDamage() - dmgAdd));
                }
            } else {
                data.setDamageMultiplier(0);
            }
        }
    }
}
