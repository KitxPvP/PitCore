package com.kitx.permanent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class Perk {
    private final String name;
    private final int cost;
    private final ItemStack icon;
}
