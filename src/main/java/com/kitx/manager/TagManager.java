package com.kitx.manager;

import com.kitx.PitCorePlugin;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import org.bukkit.scheduler.BukkitRunnable;

public class TagManager {
    public TagManager(PitCorePlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerData data : DataManager.INSTANCE.getPlayerDataMap().values()) {
                    //This counts as a tag?
                    if (!data.getCountDown().isFinished()) {
                        data.getCountDown().countDown();
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }
}
