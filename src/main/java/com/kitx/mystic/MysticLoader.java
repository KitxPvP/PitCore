package com.kitx.mystic;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.mystic.impl.PoisonBlade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public enum MysticLoader implements Listener {
    INSTANCE;

    /**
     * Used for getting the item
     */
    public final Class<?>[] MYSTICS = new Class[] {
            PoisonBlade.class,
    };

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) return;
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            PlayerData player = DataManager.INSTANCE.get((Player) event.getDamager());
            PlayerData victim = DataManager.INSTANCE.get((Player) event.getEntity());

            for(MysticItem item : player.getMysticItems()) {
                item.onHit(player, victim);
            }

            player.setLastPlayer(victim);
        }
    }
}
