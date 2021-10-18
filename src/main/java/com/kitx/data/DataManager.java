package com.kitx.data;

import com.kitx.PitCore;
import com.kitx.PitCorePlugin;
import com.kitx.listener.DataListener;
import com.kitx.listener.PlayerListener;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public enum DataManager {
    INSTANCE;

    private final ConcurrentHashMap<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();

    /*
    TODO: Add listeners here
     */
    public void init(PitCorePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new DataListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
    }

    public void inject(Player player) {
        PitCore.INSTANCE.getExecutorService().execute(() -> {
            PlayerData data = new PlayerData(player);
            playerDataMap.put(player.getUniqueId(), data);
            PitCore.INSTANCE.getScoreboardManager().create(player);
            data.loadData();
        });
    }

    public void deject(Player player) {
        PitCore.INSTANCE.getExecutorService().execute(() -> {
            get(player).saveData();
            PitCore.INSTANCE.getScoreboardManager().remove(player);
            playerDataMap.remove(player.getUniqueId());
        });
    }

    public void saveAll() {
        for(PlayerData data : playerDataMap.values()) {
            data.saveData();
        }
    }

    public PlayerData get(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }
}
