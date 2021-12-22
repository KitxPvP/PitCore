package com.kitx.events;

import com.kitx.data.PlayerData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class RegisterPlayerEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerData player;

    public static HandlerList getHandlerList() {
        return RegisterPlayerEvent.HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
