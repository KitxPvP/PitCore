package com.kitx.mystic;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.events.PitKillEvent;
import com.kitx.mystic.impl.LightningBlade;
import com.kitx.mystic.impl.PoisonBlade;
import com.kitx.mystic.impl.RainbowBlade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum MysticLoader implements Listener {
    INSTANCE;

    /**
     * Used for getting the item
     */
    public final Class<?>[] MYSTICS = new Class[] {
            PoisonBlade.class, LightningBlade.class, RainbowBlade.class
    };

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) return;
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if(((Player) event.getDamager()).getItemInHand() == null) return;
            if(((Player) event.getDamager()).getItemInHand().getItemMeta() == null) return;
            if(((Player) event.getDamager()).getItemInHand().getItemMeta().getDisplayName() == null) return;

            PlayerData player = DataManager.INSTANCE.get((Player) event.getDamager());
            PlayerData victim = DataManager.INSTANCE.get((Player) event.getEntity());

            String heldName = player.getPlayer().getItemInHand().getItemMeta().getDisplayName();
            for(MysticItem item : player.getMysticItems()) {
                String name = item.getName().replaceAll("&", "\247");
                if(heldName.equalsIgnoreCase(name)) {
                    item.onHit(player, victim, event);
                    break;
                }
            }

            player.setLastPlayer(victim);
        }
    }

    @EventHandler
    public void onDeath(PitKillEvent event) {
        List<MysticItem> found = new ArrayList<>();
        for(ItemStack itemStack : event.getPlayer().getPlayer().getInventory().getContents()) {
            if(itemStack.getItemMeta() == null) return;
            if(itemStack.getItemMeta().getDisplayName() == null) return;

            String heldName = itemStack.getItemMeta().getDisplayName();
            for(MysticItem mysticItem : event.getPlayer().getMysticItems()) {
                String name = mysticItem.getName().replaceAll("&", "\247");
                if(heldName.equalsIgnoreCase(name)) {
                    mysticItem.setLives(mysticItem.getLives() - 1);
                    if (mysticItem.getLives() == 0) {
                        found.add(mysticItem);
                    } else {
                        event.getPlayer().getMysticLoadNow().add(mysticItem);
                    }
                }
            }
        }
        event.getPlayer().getMysticItems().removeAll(found);
    }
}
