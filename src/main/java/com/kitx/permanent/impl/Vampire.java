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
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@PerkInfo(name = "&aVampire", desc = "&7Heal &c0.5 &7on hit and regen 1 on kill.", cost = 8000, icon = Material.FERMENTED_SPIDER_EYE)
public class Vampire extends Perk implements Listener {

    public Vampire() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }


    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if(event.getEntity().getKiller() != null) {
            PlayerData data = DataManager.INSTANCE.get(event.getEntity().getKiller());
            if(data.getPerks().contains(this)) {
                PotionEffect regen = PotionEffectType.REGENERATION.createEffect(160, 0);
                data.getPlayer().addPotionEffect(regen);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            PlayerData data = DataManager.INSTANCE.get(((Player) event.getDamager()).getPlayer());
            if(data.getPerks().contains(this)) {
                if (data.getPlayer().isDead()) {
                    data.getPlayer().setHealth(Math.min(20, data.getPlayer().getHealth() + 1));
                }
            }
        }
    }

}
