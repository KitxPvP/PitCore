package com.kitx;

import lombok.Getter;

@Getter
public enum PitCore {
    INSTANCE;

    private PitCorePlugin plugin;

    public void onLoad(PitCorePlugin plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }
}
