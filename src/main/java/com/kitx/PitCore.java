package com.kitx;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.listener.DataListener;
import lombok.Getter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
public enum PitCore {
    INSTANCE;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private PitCorePlugin plugin;

    public void onLoad(PitCorePlugin plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
        DataManager.INSTANCE.init(plugin);
    }

    public void onDisable() {
        DataManager.INSTANCE.saveAll();
    }
}
