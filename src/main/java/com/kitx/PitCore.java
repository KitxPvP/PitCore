package com.kitx;

import com.kitx.config.Config;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.listener.DataListener;
import com.kitx.scoreboard.ScoreboardManager;
import jdk.nashorn.internal.ir.Block;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
public enum PitCore {
    INSTANCE;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private ScoreboardManager scoreboardManager;
    private final List<Block> pendingBlocks = new ArrayList<>();

    private PitCorePlugin plugin;

    public void onLoad(PitCorePlugin plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
        scoreboardManager = new ScoreboardManager();
        DataManager.INSTANCE.init(plugin);
        Config.loadConfig();
    }

    public void onDisable() {
        DataManager.INSTANCE.saveAll();
    }
}
