package com.kitx.perks.impl;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@PerkInfo(name = "&aVampire", desc = "&7Heal &c0.5 &7on hit and regen 1 on kill.", cost = 8000, icon = Material.FERMENTED_SPIDER_EYE)
public class VampirePerk extends Perk {

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        PotionEffect regen = PotionEffectType.REGENERATION.createEffect(160, 0);
        killer.getPlayer().addPotionEffect(regen);
        super.onKill(killer, victim);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            PlayerData data = DataManager.INSTANCE.get(((Player) event.getDamager()).getPlayer());
            if(data.getPerks().contains(this)) {
                if (data.getPlayer().getHealth() != 0) {
                    data.getPlayer().setHealth(Math.min(20, data.getPlayer().getHealth() + 1));
                }
            }
        }
    }

}
