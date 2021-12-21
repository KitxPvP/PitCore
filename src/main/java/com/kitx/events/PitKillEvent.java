package com.kitx.events;

import com.kitx.data.PlayerData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class PitKillEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerData player;
    private final PlayerData killer;
    @Setter private double gold, xp;

    public static HandlerList getHandlerList() {
        return PitKillEvent.HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
