package com.kitx.data;

import com.kitx.PitCore;
import com.kitx.PitCorePlugin;
import com.kitx.listener.ChatFormatListener;
import com.kitx.listener.DataListener;
import com.kitx.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public enum DataManager {
    INSTANCE;

    private final ConcurrentHashMap<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();

    /*
    TODO: Add listeners here
     */
    public void init(PitCorePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new DataListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChatFormatListener(), plugin);
        for (Player player : Bukkit.getOnlinePlayers()) {
            inject(player);
        }
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(PitCore.INSTANCE.getPlugin(), this::saveAll, 0, 6000);
    }

    public void inject(Player player) {
        PlayerData data = new PlayerData(player);
        playerDataMap.put(player.getUniqueId(), data);
        PitCore.INSTANCE.getScoreboardManager().create(player);
        data.loadData();
    }

    public void deject(Player player) {
        get(player).saveData();
        PitCore.INSTANCE.getScoreboardManager().remove(player);
        playerDataMap.remove(player.getUniqueId());
    }

    public void saveAll() {
        for (PlayerData data : playerDataMap.values()) {
            data.saveData();
            if (data.getPlayer().hasPermission("core.dev")) {
                data.getPlayer().sendMessage(ChatColor.RED + "Saving data this may lag!");
            }
        }
    }

    public void save() {
        for (PlayerData data : playerDataMap.values()) {
            data.saveData();
            data.unregisterNameTag();
        }
    }


    public PlayerData get(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }
}
