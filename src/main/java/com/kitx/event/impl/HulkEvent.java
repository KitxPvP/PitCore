package com.kitx.event.impl;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.event.Event;
import com.kitx.events.PitKillEvent;
import com.kitx.utils.ColorUtil;
import com.kitx.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class HulkEvent extends Event {
    private PlayerData hulk;

    public HulkEvent() {
        super(EventType.MAJOR, 8, 20, 500);
    }

    @EventHandler
    public void onKill(PitKillEvent event) {
        if(event.getPlayer() == hulk) {
            setHulk(event.getPlayer());
            event.setGold(event.getGold() * 4);
            event.setXp(event.getXp() * 4);
        }
    }

    @Override
    public void onTick() {
        while (hulk == null) {
            List<PlayerData> dataList = DataManager.INSTANCE.getPlayerDataMap().values().stream()
                    .filter(data -> data.getStatus() == PlayerData.Status.FIGHTING)
                    .toList();
            if(dataList.size() < 4) {
                PitCore.INSTANCE.getEventManager().stopCurrentEvent();
            }
            setHulk(dataList.get(new Random().nextInt(dataList.size())));
        }
        super.onTick();
    }

    public void setHulk(PlayerData hulk) {
        this.hulk = hulk;
        hulk.getPlayer().getInventory().clear();
        hulk.getPlayer().getInventory().setArmorContents(null);
        hulk.getPlayer().getInventory().setChestplate(ItemUtils.createItem(Material.DIAMOND_CHESTPLATE));
        hulk.getPlayer().getInventory().setHelmet(ItemUtils.createItem(Material.DIAMOND_HELMET));
        hulk.getPlayer().getInventory().setLeggings(ItemUtils.createItem(Material.DIAMOND_LEGGINGS));
        hulk.getPlayer().getInventory().setBoots(ItemUtils.createItem(Material.DIAMOND_BOOTS));
        hulk.getPlayer().getInventory().addItem(ItemUtils.createItem(Material.DIAMOND_SWORD));

        PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 19999980, 0);
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 19999980, 0);
        PotionEffect heal = new PotionEffect(PotionEffectType.HEAL, 1, 255);
        PotionEffect absorption = new PotionEffect(PotionEffectType.ABSORPTION, 19999980, 5);

        hulk.getPlayer().addPotionEffect(regen, true);
        hulk.getPlayer().addPotionEffect(speed, true);
        hulk.getPlayer().addPotionEffect(heal, true);
        hulk.getPlayer().addPotionEffect(absorption, true);
        hulk.getPlayer().updateInventory();

        hulk.getPlayer().sendMessage(ColorUtil.translate("&7[&aHulk Event&7] &4You are now the hulk!"));
        Bukkit.broadcastMessage(ColorUtil.translate("&7[aHulk Event&7] &4" + hulk.getPlayer().getName() + " is now the hulk!"));
    }
}
