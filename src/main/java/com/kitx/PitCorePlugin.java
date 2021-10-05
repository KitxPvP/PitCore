package com.kitx;

import org.bukkit.plugin.java.JavaPlugin;

public class PitCorePlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        PitCore.INSTANCE.onLoad(this);
        super.onLoad();
    }

    @Override
    public void onEnable() {
        PitCore.INSTANCE.onEnable();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        PitCore.INSTANCE.onDisable();
        super.onDisable();
    }
}
