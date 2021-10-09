package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import com.kitx.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@PerkInfo(name = "&6GoldenHeads", desc = "&7On kill you get a &6golden head!", cost = 800, icon = Material.GOLDEN_APPLE)
public class GoldenHead extends Perk implements Listener {

    private final ItemStack goldenHead;

    public GoldenHead() {
        PitCore.INSTANCE.getPlugin().getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
        this.goldenHead = skullItem();
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer != null) {
            PlayerData data = DataManager.INSTANCE.get(killer);
            if(!data.getPerks().contains(this)) return;

            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner("PhantomTupac");
            meta.setDisplayName(ColorUtil.translate("&6Golden Head &7(Right Click)"));
            skull.setItemMeta(meta);

            killer.getInventory().addItem(skull);
        }

    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem().getItemMeta().equals(goldenHead.getItemMeta())) {
            Player player = event.getPlayer();

            player.getInventory().remove(goldenHead);
            player.updateInventory();
            PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 60, 1);
            PotionEffect instant = new PotionEffect(PotionEffectType.HEAL, 20, 1);
            PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 60, 1);

            player.addPotionEffect(regen);
            player.addPotionEffect(instant);
            player.addPotionEffect(speed);

            event.setCancelled(true);
        }
    }

    public ItemStack skullItem() {

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner("PhantomTupac");
        meta.setDisplayName(ColorUtil.translate("&6Golden Head &7(Right Click)"));
        skull.setItemMeta(meta);
        return skull;
    }
}
