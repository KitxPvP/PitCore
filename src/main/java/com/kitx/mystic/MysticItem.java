package com.kitx.mystic;


import com.kitx.data.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@Getter
public class MysticItem {
    private final String name;
    private final List<String> lore;
    private final int tier;
    @Setter private int lives;
    private int hit;

    /**
     * Calls whenever a player hits with the item
     */
    public void onHit(PlayerData player, PlayerData victim) {
        if(player.getLastPlayer() != victim)
            hit = 0;
    }

    public MysticItem(String name, int tier, int lives, String... lore) {
        this.name = name;
        this.tier = tier;
        this.lives = lives;
        this.lore = Arrays.asList(lore);
    }
}
