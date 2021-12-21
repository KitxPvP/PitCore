package com.kitx.event;

import com.kitx.data.PlayerData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Event implements Listener {

    private final List<PlayerData> players = new ArrayList<>();

    private final EventType type;
    private final int minPlayers, chance, duration;

    public void onJoin(PlayerData data) {
        players.add(data);
    }

    public void onLeave(PlayerData data) {
        players.remove(data);
    }

    public void onTick() {
    }

    public enum EventType {
        MINI, MAJOR
    }
}
