package com.kitx.perks.impl;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import com.kitx.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

@PerkInfo(name = "&4Necromancer", cost = 8000, desc = "&7Every kill you receive a &4bomb&7. You can throw the &4bomb&7.", requiredPrestige = 3, icon = Material.TNT)
public class BomberPerk extends Perk {

    private final ItemStack bomb = skullItem();

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        if (killer.isHulk()) return;
        found:
        {
            for (ItemStack itemStack : killer.getPlayer().getInventory()) {
                try {
                    if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(bomb.getItemMeta().getDisplayName())) {
                        itemStack.setAmount(Math.min(3, itemStack.getAmount() + 1));
                        break found;
                    }
                } catch (Exception ignored) {
                }
            }
            killer.getPlayer().getInventory().addItem(bomb);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if (event.getItem().getItemMeta().getDisplayName() == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(bomb.getItemMeta().getDisplayName())) {
            Player player = event.getPlayer();
            PlayerData data = DataManager.INSTANCE.get(player);
            if (data.getBomberCD().hasCooldown(1)) {
                TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREBALL);
                tnt.setCustomName("TNT:" + player.getName());
                tnt.setCustomNameVisible(false);
            } else
                player.sendMessage(ChatColor.RED + "Bombs are on cooldown for " + data.getBomberCD().getSeconds());
        }
        event.setCancelled(true);
    }

    public ItemStack skullItem() {
        ItemStack skull = new ItemStack(Material.TNT, 1);
        ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(ColorUtil.translate("&cTNT &7(Right Click)"));
        skull.setItemMeta(meta);

        return skull;
    }


}
