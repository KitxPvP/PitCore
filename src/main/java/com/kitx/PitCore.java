package com.kitx;

import com.kitx.command.*;
import com.kitx.config.Config;
import com.kitx.data.DataManager;
import com.kitx.manager.*;
import com.kitx.perks.PerkLoader;
import com.samjakob.spigui.SpiGUI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
public enum PitCore {
    INSTANCE;
    // What u doin man
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final List<Block> pendingBlocks = new ArrayList<>();

    private SpiGUI spiGUI;
    private ScoreboardManager scoreboardManager;
    private HealthBarManager healthBarManager;
    private GoldDropManager goldDropManager;
    private EventManager eventManager;
    private TagManager tagManager;

    private PitCorePlugin plugin;

    public void onLoad(PitCorePlugin plugin) {
        this.plugin = plugin;
        final File f = new File(plugin.getDataFolder(), "config.yml");
        final File spawn = new File(plugin.getDataFolder(), "spawnLocation.yml");
        if (!f.exists()) {
            plugin.saveResource("config.yml", true);
        }
        if(!spawn.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                spawn.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onEnable() {
        spiGUI = new SpiGUI(plugin);
        scoreboardManager = new ScoreboardManager();
        tagManager = new TagManager(plugin);
        healthBarManager = new HealthBarManager(plugin);
        goldDropManager = new GoldDropManager();
        eventManager = new EventManager(plugin);


        PerkLoader.INSTANCE.init();
        DataManager.INSTANCE.init(plugin);
        handleBukkit();
        Config.loadConfig();
    }

    public void onDisable() {
        for(Block block : pendingBlocks) {
            block.setType(Material.AIR);
        }
        pendingBlocks.clear();

        DataManager.INSTANCE.save();
        Bukkit.getScheduler().cancelAllTasks();
    }

    public void handleBukkit() {
        plugin.getCommand("stats").setExecutor(new StatsCommand());
        plugin.getCommand("perks").setExecutor(new PerkCommand());
        plugin.getCommand("shop").setExecutor(new ShopCommand());
        plugin.getCommand("spawn").setExecutor(new SpawnCommand());
        plugin.getCommand("setspawn").setExecutor(new SetSpawn());
        plugin.getCommand("nick").setExecutor(new NickCommand());
        plugin.getCommand("prestige").setExecutor(new PrestigeCommand());
        plugin.getCommand("event").setExecutor(new EventCommand());
        plugin.getCommand("bounty").setExecutor(new BountyCommand());
        plugin.getCommand("ping").setExecutor(new PingCommand());

    }
}
