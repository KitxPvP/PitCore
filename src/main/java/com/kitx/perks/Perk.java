package com.kitx.perks;

import com.kitx.PitCore;
import com.kitx.data.PlayerData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;

@Getter
@RequiredArgsConstructor
public class Perk implements Listener {
    private final String name, desc;
    private final int cost, requiredPrestige;
    private final Material icon;

    public Perk() {
        this.name = getPerkInfo().name();
        this.desc = getPerkInfo().desc();
        this.cost = getPerkInfo().cost();
        this.icon = getPerkInfo().icon();
        this.requiredPrestige = getPerkInfo().requiredPrestige();
        Bukkit.getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }

    public void onClick(PlayerData player) {}

    public void onLayout(PlayerData player) {}

    public void onKill(PlayerData killer, PlayerData victim) {}

    public PerkInfo getPerkInfo() {
        if (this.getClass().isAnnotationPresent(PerkInfo.class)) {
            return this.getClass().getAnnotation(PerkInfo.class);
        } else {
            System.err.println("PerkInfo annotation hasn't been added to the class " + this.getClass().getSimpleName() + ".");
        }
        return null;
    }
}
