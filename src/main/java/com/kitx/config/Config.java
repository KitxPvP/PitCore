package com.kitx.config;

import com.kitx.PitCore;
import org.bukkit.configuration.file.FileConfiguration;

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
}
