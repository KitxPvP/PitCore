package com.kitx.shop;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class ShopItem {
    private final ItemStack itemStack;
    private final String name;
    private final int id;

    public ShopItem(ItemStack itemStack, String name, int id) {
        this.itemStack = itemStack;
        this.name = name;
        this.id = id;
    }
}
