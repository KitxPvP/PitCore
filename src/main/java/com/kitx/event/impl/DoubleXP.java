package com.kitx.event.impl;

import com.kitx.event.Event;
import com.kitx.events.PitKillEvent;
import com.kitx.utils.ColorUtil;
import com.kitx.utils.CountDown;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class DoubleXP extends Event {
    public DoubleXP() {
        super("&eDouble XP",EventType.MINI, 5, 1000, new CountDown(500), "&7All kill xp are doubled!");
    }

    @Override
    public void onStop() {
        Bukkit.broadcastMessage(ColorUtil.translate("&7[&eEvent&7] &aDouble XP has ended!"));
        super.onStop();
    }

    @EventHandler
    public void onDeath(PitKillEvent event) {
        event.setXp(event.getXp() * 2);
    }
}
