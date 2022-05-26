package com.kitx.event.impl;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.event.Event;
import com.kitx.events.PitKillEvent;
import com.kitx.utils.ColorUtil;
import com.kitx.utils.CountDown;
import com.kitx.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class HulkEvent extends Event {
    private PlayerData hulk;

    public HulkEvent() {
        super("&2&lHULK",EventType.MAJOR, 2, 1500, new CountDown(300), "&7Kill &2Hulk &7to receive tons of gold and xp!");
    }

    @Override
    public void onStop() {
        if(hulk != null) {
            Bukkit.broadcastMessage(ColorUtil.translate("&e&lEvent hulk has stopped! Player " + hulk.getPlayer().getName() + " has won the event!"));
            hulk.getPlayer().sendTitle(ColorUtil.translate("&a&lYou have won the event!"), "");
            hulk.addGold(5000);
            hulk.loadLayout();
            hulk.setHulk(false);
            hulk = null;
        }
        super.onStop();
    }

    @EventHandler
    public void onKill(PitKillEvent event) {
        if(event.getPlayer() == hulk) {
            setHulk(event.getKiller());
            event.setGold(event.getGold() * 4);
            event.setXp(event.getXp() * 4);
            event.getPlayer().setHulk(false);
        }
    }

    @Override
    public String[] onScoreBoard(PlayerData data) {
        if(hulk != null) {
            return new String[]{
                    "&fHulk&7: &2" + hulk.getPlayer().getName()
            };
        }
        return super.onScoreBoard(data);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        PlayerData data = DataManager.INSTANCE.get(event.getPlayer());
        if(data.isHulk()) {
            data.loadLayout();
            hulk = null;
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerData data = DataManager.INSTANCE.get(event.getPlayer());
        if(data.isHulk()) {
            data.setHulk(false);
            hulk = null;
        }
    }

    @EventHandler
    public void onKill(PlayerQuitEvent event) {
        PlayerData data = DataManager.INSTANCE.get(event.getPlayer());
        if(data == hulk) hulk = null;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(DataManager.INSTANCE.get(player).isHulk()) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onTick() {
        while (hulk == null) {
            List<PlayerData> dataList = DataManager.INSTANCE.getPlayerDataMap().values().stream()
                    .filter(data -> data.getStatus() == PlayerData.Status.FIGHTING)
                    .toList();
            if(dataList.isEmpty()) {
                PitCore.INSTANCE.getEventManager().stopCurrentEvent();
                break;
            }
            setHulk(dataList.get(new Random().nextInt(dataList.size())));
        }
        if(hulk != null) {
            if(!hulk.getPlayer().isOnline()) hulk = null;
        }
        super.onTick();
    }

    private void setHulk(PlayerData hulk) {
        this.hulk = hulk;
        hulk.getPlayer().getInventory().clear();
        hulk.getPlayer().getInventory().setArmorContents(null);
        hulk.getPlayer().getInventory().setChestplate(ItemUtils.createItem(Material.DIAMOND_CHESTPLATE));
        hulk.getPlayer().getInventory().setHelmet(ItemUtils.createItem(Material.DIAMOND_HELMET));
        hulk.getPlayer().getInventory().setLeggings(ItemUtils.createItem(Material.DIAMOND_LEGGINGS));
        hulk.getPlayer().getInventory().setBoots(ItemUtils.createItem(Material.DIAMOND_BOOTS));
        hulk.getPlayer().getInventory().addItem(ItemUtils.createItem(Material.DIAMOND_SWORD));

        PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 19999980, 0);
        PotionEffect speed = new PotionEffect(PotionEffectType.SLOW, 19999980, 0);
        PotionEffect heal = new PotionEffect(PotionEffectType.HEAL, 1, 255);
        PotionEffect absorption = new PotionEffect(PotionEffectType.ABSORPTION, 19999980, 5);

        hulk.getPlayer().addPotionEffect(regen, true);
        hulk.getPlayer().addPotionEffect(speed, true);
        hulk.getPlayer().addPotionEffect(heal, true);
        hulk.getPlayer().addPotionEffect(absorption, true);
        hulk.getPlayer().updateInventory();

        hulk.getPlayer().sendMessage(ColorUtil.translate("&7[&aHulk Event&7] &4You are now the hulk!"));
        Bukkit.broadcastMessage(ColorUtil.translate("&7[&eHulk Event&7] &4" + hulk.getPlayer().getName() + " is now the hulk!"));
        hulk.getPlayer().getWorld().playSound(hulk.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
        hulk.setHulk(true);
    }
}
