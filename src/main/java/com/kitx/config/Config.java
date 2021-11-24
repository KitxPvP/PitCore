package com.kitx.config;

import com.kitx.PitCore;
import com.kitx.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    /*
     * # Basic messages
     * killMessage: "You killed %player%"
     * deathMessage: "You died to %player%"
     *
     * killStreak: "&a%player% is on a %streak%"
     */

    public static String KILL_MESSAGE, DEATH_MESSAGE, KILLSTREAK_MESSAGE;

    public static void loadConfig() {
        FileConfiguration config = PitCore.INSTANCE.getPlugin().getConfig();

        KILL_MESSAGE = config.getString("killMessage");
        DEATH_MESSAGE = config.getString("deathMessage");
        KILLSTREAK_MESSAGE = config.getString("killStreak");
    }

    public static void setSpawn(Location location) {
        final File spawn = new File(PitCore.INSTANCE.getPlugin().getDataFolder(), "spawnLocation.yml");
        if(!spawn.exists()) {
            try {
                spawn.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn);
        config.set("spawnLocation", LocationUtil.parseToString(location));
        try {
            config.save(spawn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getLocation() {
        final File spawn = new File(PitCore.INSTANCE.getPlugin().getDataFolder(), "spawnLocation.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn);
        return LocationUtil.parseToLocation(config.getString("spawnLocation"));
    }
}
