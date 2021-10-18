package com.kitx.permanent;

import com.kitx.data.PlayerData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class Perk {
    private final String name, desc;
    private final int cost;
    private final Material icon;

    public Perk() {
        this.name = getCheckInfo().name();
        this.desc = getCheckInfo().desc();
        this.cost = getCheckInfo().cost();
        this.icon = getCheckInfo().icon();
    }

    public void onClick(PlayerData player) {}

    public void onLayout(PlayerData player) {}

    public PerkInfo getCheckInfo() {
        if (this.getClass().isAnnotationPresent(PerkInfo.class)) {
            return this.getClass().getAnnotation(PerkInfo.class);
        } else {
            System.err.println("PerkInfo annotation hasn't been added to the class " + this.getClass().getSimpleName() + ".");
        }
        return null;
    }
}
