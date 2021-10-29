package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import com.kitx.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@PerkInfo(name = "&eSpeedster", desc = "&7Gain speed 2 for 5 seconds on kill", cost = 900, icon = Material.LEATHER_BOOTS)
public class Speedster extends Perk implements Listener {

    public Speedster() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }



    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            PlayerData data = DataManager.INSTANCE.get(event.getEntity().getKiller());
            if (data.getPerks().contains(this)) {
                PotionEffect regen = new PotionEffect(PotionEffectType.SPEED, 120, 1);
                data.getPlayer().addPotionEffect(regen, true);
            }
        }
    }
}
