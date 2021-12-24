package com.kitx.event;

import com.kitx.PitCore;
import com.kitx.data.PlayerData;
import com.kitx.utils.CountDown;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Listener;

@AllArgsConstructor
@Getter
public class Event implements Listener {

    private final String name;
    private final EventType type;
    private final int minPlayers, chance;
    private CountDown duration;
    private final String desc;
    
    public void onTick() {
        duration.countDown();
        if (duration.isFinished()) {
            PitCore.INSTANCE.getEventManager().stopCurrentEvent();
        }
    }

    public String[] onScoreBoard(PlayerData data) {
        return new String[] {};
    }

    public void onStop() {
        duration.resetTime();
    }

    public void onStart() {}

    public enum EventType {
        MINI, MAJOR
    }
}
