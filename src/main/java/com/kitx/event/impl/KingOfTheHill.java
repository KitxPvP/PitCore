package com.kitx.event.impl;

import com.kitx.data.PlayerData;
import com.kitx.event.Event;
import com.kitx.utils.CountDown;

public class KingOfTheHill extends Event {

    public KingOfTheHill() {
        super("&b&lKOTH", EventType.MAJOR, 2, 1500, new CountDown(300), "&7Reach the top of the hill to receive bonus xp");
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
