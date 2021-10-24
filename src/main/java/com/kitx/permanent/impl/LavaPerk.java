package com.kitx.permanent.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.kitx.PitCore;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@PerkInfo(name = "&6Lava Perk", desc = "&aReceive a lava bucket", cost = 450, icon = Material.LAVA_BUCKET)
public class LavaPerk extends Perk implements Listener {
    public LavaPerk() {
        PitCore.INSTANCE.getPlugin().getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    @Override
    public void onClick(PlayerData player) {
        if (player.getPerks().contains(this)) {
            player.getPlayer().getInventory().remove(Material.LAVA_BUCKET);
        } else {
            player.getPlayer().getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
        }
        super.onClick(player);
    }

    @Override
    public void onLayout(PlayerData player) {
        player.getPlayer().getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
        super.onLayout(player);
    }
}
