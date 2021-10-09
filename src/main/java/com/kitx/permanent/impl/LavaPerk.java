package com.kitx.permanent.impl;

import com.kitx.PitCore;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkInfo;
import org.bukkit.Material;
import org.bukkit.event.Listener;

@PerkInfo(name = "&6Lava Perk", desc = "Receive a lava bucket", cost = 1000, icon = Material.LAVA_BUCKET)
public class LavaPerk extends Perk implements Listener {
    public LavaPerk() {
        PitCore.INSTANCE.getPlugin().getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }
}
