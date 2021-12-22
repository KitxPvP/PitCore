package com.kitx.manager;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.event.Event;
import com.kitx.event.impl.DoubleXP;
import com.kitx.event.impl.HulkEvent;
import com.kitx.event.impl.Teams;
import com.kitx.utils.ColorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class EventManager {
    private Event activeEvent;
    private final List<Event> events = new ArrayList<>();

    public EventManager(JavaPlugin plugin) {
        events.add(new DoubleXP());
        events.add(new HulkEvent());
        events.add(new Teams());
        new BukkitRunnable() {
            @Override
            public void run() {
                if(activeEvent == null) {
                    for (Event event : events) {
                        if (1 == new Random().nextInt(event.getChance())) {
                            startEvent(event);
                        }
                    }
                } else {
                    activeEvent.onTick();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    public void startEvent(Event event) {
        if(DataManager.INSTANCE.getPlayerDataMap().values().size() >= event.getMinPlayers()) {
            this.activeEvent = event;
            Bukkit.getPluginManager().registerEvents(event, PitCore.INSTANCE.getPlugin());
            switch (event.getType()) {
                case MINI -> Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("", ColorUtil.translate("&a&lMini Event!")));
                case MAJOR -> Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ColorUtil.translate("&4&lMAJOR EVENT!"), ""));
            }
            Bukkit.broadcastMessage(ColorUtil.translate("&7[&eEvent&7] " + event.getDesc()));

            event.onStart();
        }
    }

    public void stopCurrentEvent() {
        activeEvent.onStop();
        DataManager.INSTANCE.getPlayerDataMap().values().forEach(PlayerData::removeLines);
        HandlerList.unregisterAll(activeEvent);
        activeEvent = null;
    }
}
