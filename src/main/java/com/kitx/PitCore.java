package com.kitx;

import com.kitx.command.*;
import com.kitx.config.Config;
import com.kitx.data.DataManager;
import com.kitx.manager.GoldDropManager;
import com.kitx.manager.HealthBarManager;
import com.kitx.manager.ScoreboardManager;
import com.kitx.manager.TagManager;
import com.kitx.mystic.MysticLoader;
import com.kitx.permanent.PerkLoader;
import lombok.Getter;
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

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final List<Block> pendingBlocks = new ArrayList<>();

    private ScoreboardManager scoreboardManager;
    private HealthBarManager healthBarManager;
    private GoldDropManager goldDropManager;
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
        scoreboardManager = new ScoreboardManager();
        tagManager = new TagManager(plugin);
        healthBarManager = new HealthBarManager(plugin);
        goldDropManager = new GoldDropManager();


        PerkLoader.INSTANCE.init();
        MysticLoader.INSTANCE.init();
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
    }

    public void handleBukkit() {
        plugin.getCommand("stats").setExecutor(new StatsCommand());
        plugin.getCommand("perks").setExecutor(new PerkCommand());
        plugin.getCommand("shop").setExecutor(new ShopCommand());
        plugin.getCommand("spawn").setExecutor(new SpawnCommand());
        plugin.getCommand("setspawn").setExecutor(new SetSpawn());
        plugin.getCommand("nick").setExecutor(new NickCommand());
        plugin.getCommand("mystic").setExecutor(new MysticCommand());
        plugin.getCommand("prestige").setExecutor(new PrestigeCommand());
    }
}
