package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import com.kitx.utils.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@PerkInfo(name = "&6GoldenHeads", desc = "&7On kill you get a &6golden head!", cost = 450, icon = Material.GOLDEN_APPLE)
public class GoldenHeadPerk extends Perk implements Listener {

    private final ItemStack goldenHead;

    public GoldenHeadPerk() {
        PitCore.INSTANCE.getPlugin().getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
        this.goldenHead = skullItem();
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (killer != null) {
            PlayerData data = DataManager.INSTANCE.get(killer);
            if (data.getPerks().contains(this)) {
                killer.getInventory().addItem(goldenHead);
            } else {
                killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            }

        }

    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if(event.getItem().getItemMeta().getDisplayName() == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(goldenHead.getItemMeta().getDisplayName())) {
            Player player = event.getPlayer();
            PlayerData data = DataManager.INSTANCE.get(player);
            if(data.getGapCD().hasCooldown(3)) {
                player.playSound(player.getLocation(), Sound.EAT, 1, 1);

                ItemStack itemStack = new ItemStack(event.getItem());
                itemStack.setAmount(1);
                player.getInventory().removeItem(new ItemStack(itemStack));
                player.updateInventory();
                PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 120, 0);
                PotionEffect instant = new PotionEffect(PotionEffectType.HEAL, 1, 0);
                PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 120, 0);
                PotionEffect absortion = new PotionEffect(PotionEffectType.ABSORPTION, 120, 0);

                player.addPotionEffect(regen);
                player.addPotionEffect(instant);
                player.addPotionEffect(speed);
                player.addPotionEffect(absortion);
            } else {
                player.sendMessage(ChatColor.RED + "Heads are on cooldown for " + data.getGapCD().getSeconds());
            }
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
