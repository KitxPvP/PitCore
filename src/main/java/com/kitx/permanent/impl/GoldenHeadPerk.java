package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import com.kitx.permanent.PerkLoader;
import com.kitx.utils.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        if(killer.getPerks().contains(PerkLoader.INSTANCE.findPerk("&aVampire")) || killer.isHulk()) return;
        found:
        {
            for (ItemStack itemStack : killer.getPlayer().getInventory()) {
                try {
                    if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(goldenHead.getItemMeta().getDisplayName())) {
                        itemStack.setAmount(Math.min(3, itemStack.getAmount() + 1));
                        break found;
                    }
                } catch (Exception ignored) {}
            }
            killer.getPlayer().getInventory().addItem(goldenHead);
        }
        super.onKill(killer, victim);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if (event.getItem().getItemMeta().getDisplayName() == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(goldenHead.getItemMeta().getDisplayName())) {
            Player player = event.getPlayer();
            PlayerData data = DataManager.INSTANCE.get(player);
            if (data.getGapCD().hasCooldown(1)) {
                player.playSound(player.getLocation(), Sound.EAT, 1, 1);

                ItemStack itemStack = new ItemStack(event.getItem());
                itemStack.setAmount(1);
                player.getInventory().removeItem(new ItemStack(itemStack));
                player.updateInventory();
                PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 120, 0);
                PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 180, 0);
                PotionEffect heal = new PotionEffect(PotionEffectType.HEAL, 1, 0);
                PotionEffect absorption = new PotionEffect(PotionEffectType.ABSORPTION, 120, 0);

                player.addPotionEffect(regen, true);
                player.addPotionEffect(speed, true);
                player.addPotionEffect(heal, true);
                player.addPotionEffect(absorption, true);
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
