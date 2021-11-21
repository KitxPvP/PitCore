package com.kitx.manager;

import com.kitx.PitCore;
import com.kitx.config.Config;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.events.GoldPickEvent;
import com.kitx.utils.ColorUtil;
import com.kitx.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;

public class GoldDropManager implements Listener {
    public GoldDropManager() {
        Bukkit.getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(PitCore.INSTANCE.getPlugin(), this::spawnItem, 0, 240);
    }

    public void spawnItem() {
        Location location = new Location(Config.getLocation().getWorld(), randomNumber(-44, 44), 71, randomNumber(41, -60));
        int count = 0;
        while (true) {
            if (count > 20) break;
            if (Config.getLocation() == null) System.out.println("Set spawn for this to work!");
            Block block = getBlockAsync(location);
            if (block != null) {
                if (block.getType().isSolid() && count == 0) {
                    spawnItem();
                    break;
                } else if (block.getType().isSolid() && count > 0) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            location.getWorld().dropItemNaturally(block.getLocation(), ItemUtils.createItem(Material.GOLD_INGOT));
                        }
                    }.runTask(PitCore.INSTANCE.getPlugin());
                    break;
                }
                location.setY(location.getBlockY() - 1);

            } else {
                spawnItem();
                break;
            }
            count++;
        }
    }

    private double randomNumber(float max, float min) {
        return (min + Math.random() * ((max - min)));
    }

    private Block getBlockAsync(final Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getWorld().getBlockAt(location);
        } else {
            return null;
        }
    }

    @EventHandler
    public void pickUp(PlayerPickupItemEvent event) {
        if (event.getItem().getItemStack() == null) return;
        if (event.getItem().getItemStack().getItemMeta() == null) return;
        if (event.getItem().getItemStack().isSimilar(ItemUtils.createItem(Material.GOLD_INGOT))) {
            PlayerData data = DataManager.INSTANCE.get(event.getPlayer());
            double addedGold = randomNumber(10, 1);
            addedGold = Math.round(addedGold * 100.0) / 100.0;
            GoldPickEvent goldPickEvent = new GoldPickEvent(data, addedGold);
            Bukkit.getServer().getPluginManager().callEvent(goldPickEvent);
            data.setGold(BigDecimal.valueOf(data.getGold()).add(BigDecimal.valueOf(goldPickEvent.getGold())).doubleValue());
            data.getPlayer().sendMessage(ColorUtil.translate("&6&lGOLD PICKUP! &7from the ground &6" + addedGold + "g"));
            event.getItem().remove();
            data.getPlayer().playSound(data.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
            event.setCancelled(true);
        }
    }
}
