package com.kitx.mystic;

import com.kitx.PitCore;
import com.samjakob.spigui.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class MysticEnchant {
    private final String name;
    private final boolean combo;
    private final int tier;
    private final int lives;
    private final String[] lore;

    public MysticEnchant(String name, boolean combo, int tier, int lives, String... lore) {
        this.name = name;
        this.combo = combo;
        this.tier = tier;
        this.lives = lives;
        this.lore = lore;
    }

    public void createItem(Player player) {

    }
}
