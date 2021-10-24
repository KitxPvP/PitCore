package com.kitx.manager;

import com.kitx.PitCore;
import com.kitx.PitCorePlugin;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.utils.ColorUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class TagManager {
    public TagManager(PitCorePlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerData data : DataManager.INSTANCE.getPlayerDataMap().values()) {
                    //This counts as a tag?
                    if (!data.getCountDown().isFinished()) {
                        data.getCountDown().countDown();
                    } else if (data.getStatus() != PlayerData.Status.BOUNTIED && data.getStatus() != PlayerData.Status.IDLE) {
                        data.updateStatus();
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }
}
