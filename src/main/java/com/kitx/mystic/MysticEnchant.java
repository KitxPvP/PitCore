package com.kitx.mystic;

import com.kitx.PitCore;
import com.kitx.data.PlayerData;
import com.kitx.utils.ColorUtil;
import com.samjakob.spigui.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class MysticEnchant implements Listener {
    private final String name;
    private final boolean combo;
    private final int tier;
    private final int lives;
    private final List<String> lore;
    private int hit;

    public MysticEnchant(String name, boolean combo, int tier, int lives, String... lore) {
        this.name = ColorUtil.translate(name);
        this.combo = combo;
        this.tier = tier;
        this.lives = lives;
        this.lore = ColorUtil.translate(lore);
    }

    public void onHit(PlayerData player, PlayerData victim, EntityDamageByEntityEvent event) {
        hit++;
        if(player.getLastPlayer() != victim)
            hit = 0;
    }
}
