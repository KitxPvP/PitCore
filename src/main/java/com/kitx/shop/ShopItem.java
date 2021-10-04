package com.kitx.shop;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class ShopItem {
    private final ItemStack itemStack;
    private final String name;

    public ShopItem(ItemStack itemStack, String name) {
        this.itemStack = itemStack;
        this.name = name;
    }
}
