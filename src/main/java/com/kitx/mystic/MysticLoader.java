package com.kitx.mystic;

import com.kitx.PitCore;
import com.kitx.data.PlayerData;
import com.kitx.mystic.impl.PoisonBlade;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public enum MysticLoader implements Listener {
    INSTANCE;

    /**
     * Used for getting the item
     */
    private final Class<?>[] MYSTICS = new Class[] {
            PoisonBlade.class,
    };

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PitCore.INSTANCE.getPlugin());
    }
}
