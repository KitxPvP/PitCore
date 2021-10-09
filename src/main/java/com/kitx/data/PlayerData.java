package com.kitx.data;

import com.kitx.PitCore;
import com.kitx.permanent.Perk;
import com.kitx.utils.ConcurrentEvictingList;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private final Player player;
    private final UUID uuid;

    private int level, kills, deaths, gold, xp, neededXp, bounty, killStreak;
    private final List<Perk> purchasedPerks = new ArrayList<>();
    private final ConcurrentEvictingList<Perk> perks = new ConcurrentEvictingList<>(3);
    public PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public void saveData() {
        final File dir = new File(PitCore.INSTANCE.getPlugin().getDataFolder(), "data");

        if (!dir.exists()) //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();

        final File player = new File(dir, getPlayer().getUniqueId() + ".yml");

        if (!player.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                player.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final YamlConfiguration load = YamlConfiguration.loadConfiguration(player);
            load.set("levels", getLevel());
            load.set("kills", getKills());
            load.set("deaths", getDeaths());
            load.set("gold", getGold());
            load.set("xp", getXp());
            load.set("neededXp", getNeededXp());
            load.set("bounty", getBounty());
            load.set("killStreak", getKillStreak());

            try {
                load.save(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadData() {
        final File dir = new File(PitCore.INSTANCE.getPlugin().getDataFolder(), "data");

        final File player = new File(dir, getPlayer().getUniqueId() + ".yml");

        if (!player.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                player.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final YamlConfiguration load = YamlConfiguration.loadConfiguration(player);
            setLevel(load.getInt("levels"));
            setKills(load.getInt("kills"));
            setDeaths(load.getInt("deaths"));
            setGold(load.getInt("gold"));
            setXp(load.getInt("xp"));
            setNeededXp(load.getInt("neededXp"));
            setBounty(load.getInt("bounty"));
            setKillStreak(load.getInt("killStreak"));
        }
    }
}
