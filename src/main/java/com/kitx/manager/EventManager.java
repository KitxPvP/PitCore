package com.kitx.manager;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.event.Event;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EventManager {
    private Event activeEvent;
    private final List<Event> events = new ArrayList<>();

    public EventManager(JavaPlugin plugin) {
        // Add events here..
        new BukkitRunnable() {
            @Override
            public void run() {
                if(activeEvent == null) return;
                activeEvent.onTick();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public void startEvent(Event event) {
        this.activeEvent = event;
        Bukkit.getPluginManager().registerEvents(event, PitCore.INSTANCE.getPlugin());
    }

    public void stopCurrentEvent() {
        HandlerList.unregisterAll(activeEvent);
        activeEvent = null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        data: {
            final PlayerData data = DataManager.INSTANCE.get(player);
            if(data == null || activeEvent == null) break data;
            activeEvent.onJoin(data);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        data: {
            final PlayerData data = DataManager.INSTANCE.get(player);
            if(data == null || activeEvent == null) break data;
            activeEvent.onLeave(data);
        }
    }
}
