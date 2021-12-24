package com.kitx.perks.impl;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.perks.Perk;
import com.kitx.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

@PerkInfo(name = "&eEndless Quiver", desc = "&7Get &f3 arrows &7on arrow hit.", cost = 2000, icon = Material.BOW)
public class EndlessQuiverPerk extends Perk  {

    @Override
    public void onKill(PlayerData killer, PlayerData victim) {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        itemStack.setAmount(3);
        killer.getPlayer().getInventory().addItem(itemStack);
        super.onKill(killer, victim);
    }

    @EventHandler
    public void onArrow(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Arrow arrow && !event.isCancelled()) {
            if(arrow.getShooter() instanceof Player player && player != event.getEntity()) {
                PlayerData data = DataManager.INSTANCE.get(player);
                if(data.getPerks().contains(this)) {
                    ItemStack itemStack = new ItemStack(Material.ARROW);
                    itemStack.setAmount(3);
                    player.getInventory().addItem(itemStack);
                }
            }
        }
    }
}
