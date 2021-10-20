package com.kitx;

import com.kitx.command.PerkCommand;
import com.kitx.command.ShopCommand;
import com.kitx.command.StatsCommand;
import com.kitx.config.Config;
import com.kitx.data.DataManager;
import com.kitx.manager.HealthBarManager;
import com.kitx.permanent.PerkLoader;
import com.kitx.manager.ScoreboardManager;
import lombok.Getter;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
public enum PitCore {
    INSTANCE;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private ScoreboardManager scoreboardManager;
    private HealthBarManager healthBarManager;

    private PitCorePlugin plugin;


    public void onLoad(PitCorePlugin plugin) {
        this.plugin = plugin;
        final File f = new File(plugin.getDataFolder(), "config.yml");
        if (!f.exists()) {
            plugin.saveResource("config.yml", true);
        }
    }

    public void onEnable() {
        scoreboardManager = new ScoreboardManager();
        healthBarManager = new HealthBarManager(plugin);

        DataManager.INSTANCE.init(plugin);
        PerkLoader.INSTANCE.init();
        handleBukkit();
        Config.loadConfig();
    }

    public void onDisable() {

        DataManager.INSTANCE.saveAll();
    }

    public void handleBukkit() {
        plugin.getCommand("stats").setExecutor(new StatsCommand());
        plugin.getCommand("perks").setExecutor(new PerkCommand());
        plugin.getCommand("shop").setExecutor(new ShopCommand());
    }
}
