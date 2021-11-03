package com.kitx.events;

import com.kitx.data.PlayerData;
import lombok.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class GoldPickEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerData player;
    @Setter private double gold;

    public static HandlerList getHandlerList() {
        return GoldPickEvent.HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
