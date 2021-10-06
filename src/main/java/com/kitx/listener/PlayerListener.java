package com.kitx.listener;

import com.kitx.PitCore;
import com.kitx.config.Config;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Locale;

public class PlayerListener implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        event.setDeathMessage("");
        Player killed = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if(killer != null) {
            PlayerData killedUser = DataManager.INSTANCE.get(killed);
            PlayerData killerUser = DataManager.INSTANCE.get(killer);

            killedUser.setDeaths(killedUser.getDeaths() + 1);
            killerUser.setKills(killerUser.getKills() + 1);

            killerUser.setKillStreak(killerUser.getKillStreak() + 1);
            killedUser.setKillStreak(0);

            double xpAdd = Math.ceil(Math.random() * 5) + 5;
            killerUser.setXp((int) (killerUser.getXp() + xpAdd));

            if(killerUser.getXp() >= killerUser.getNeededXp()) {
                killerUser.setXp(0);
                killerUser.setLevel(killerUser.getLevel() + 1);
                killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1, 1);
            } else {
                killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1, 1);
            }

            killer.sendMessage(ColorUtil.translate(Config.KILL_MESSAGE)
                    .replaceAll("%player%", killed.getName()));
            killed.sendMessage(ColorUtil.translate(Config.DEATH_MESSAGE)
                    .replaceAll("%player%", killer.getName()));
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(PitCore.INSTANCE.getPlugin(), () -> {
            killed.spigot().respawn();
        }, 5L);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            if (e.getClickedInventory() != null && e.getClickedInventory().getName() != null) {
                Inventory inventory = e.getInventory();
                PlayerData data = DataManager.INSTANCE.get(player);

                switch (inventory.getName().toLowerCase()) {
                    case "non-permanent items": {
                        e.setCancelled(true);
                        if (e.getCurrentItem() == null) return;
                        if (e.getCurrentItem().getItemMeta() == null) return;
                        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                        switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase())) {
                            case "diamond sword": {
                                ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
                                itemStack.getItemMeta().spigot().setUnbreakable(true);

                                itemStack.getItemMeta().addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                break;
                            }
                            case "obsidian": {

                                break;
                            }

                        }

                        break;
                    }
                    case "perks": {
                        //TODO: Add this soon
                    }
                }
            }
        }
    }
}
