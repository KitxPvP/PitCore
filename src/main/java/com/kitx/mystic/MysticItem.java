package com.kitx.mystic;


import com.kitx.data.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.List;

@Getter
public  abstract class MysticItem {
    private final String name;
    private final List<String> lore;
    private final int tier;
    @Setter private int lives;
    private int hit;

    public MysticItem(String name, int tier, int lives, String... lore) {
        this.name = name;
        this.tier = tier;
        this.lives = lives;
        this.lore = Arrays.asList(lore);
    }

    /**
     * Calls whenever a player hits with the item
     */
    public void onHit(PlayerData player, PlayerData victim, EntityDamageByEntityEvent event) {
        hit++;
        if(player.getLastPlayer() != victim)
            hit = 0;
    }


}
