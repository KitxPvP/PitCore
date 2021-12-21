package com.kitx.event.impl;

import com.kitx.event.Event;
import com.kitx.events.PitKillEvent;
import org.bukkit.event.EventHandler;

public class DoubleXP extends Event {
    public DoubleXP() {
        super(EventType.MINI, 5, 50, 500);
    }

    @EventHandler
    public void onDeath(PitKillEvent event) {
        event.setGold(event.getGold() * 2);
    }
}
