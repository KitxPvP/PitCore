package com.kitx.data;

import com.kitx.PitCore;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkLoader;
import com.kitx.utils.ColorUtil;
import com.kitx.utils.ConcurrentEvictingList;
import com.kitx.utils.ItemUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    private Status status = Status.IDLE;
    private long lastJumpPad;

    private int level, kills, deaths, xp, neededXp, killStreak, prestige;
    private double gold, bounty;

    private final List<Location> pendingBlocks = new ArrayList<>();
    private final List<Perk> purchasedPerks = new ArrayList<>();
    private final ConcurrentEvictingList<Perk> perks = new ConcurrentEvictingList<>(3);

    public PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public void bountyPlayer(double bounty) {

    }

    public String getHeader() {
        return prestigeColor() + "[" + levelColor() + level + prestigeColor() + "]";
    }

    public String levelColor() {
        String color = "";
        if (level >= 1 && level <= 9) {
            color = "7";
        } else if (level >= 10 && level <= 19) {
            color = "9";
        } else if (level >= 20 && level <= 29) {
            color = "3";
        } else if (level >= 30 && level <= 39) {
            color = "2";
        } else if (level >= 40 && level <= 49) {
            color = "a";
        } else if (level >= 50 && level <= 59) {
            color = "e";
        } else if (level >= 60 && level <= 69) {
            color = "6";
        } else if (level >= 70 && level <= 79) {
            color = "c";
        } else if (level >= 80 && level <= 89) {
            color = "4";
        } else if (level >= 90 && level <= 99) {
            color = "5";
        } else if(level >= 100 && level <= 109) {
            color = "d";
        } else if(level >= 110 && level <= 119) {
            color = "&f";
        } else if(level >= 120) {
            color = "&b";
        }

        return ColorUtil.translate("&" + color);
    }

    public String prestigeColor() {
        String color = "";
        if (prestige == 0) {
            color = "7";
        } else if (prestige >= 1 && prestige <= 4) {
            color = "9";
        } else if (prestige >= 5 && prestige <= 9) {
            color = "e";
        } else if (prestige >= 10 && prestige <= 14) {
            color = "6";
        } else if (prestige >= 15 && prestige <= 19) {
            color = "c";
        } else if (prestige >= 20 && prestige <= 24) {
            color = "5";
        } else if (prestige >= 25 && prestige <= 29) {
            color = "d";
        } else if (prestige >= 30) {
            color = "f";
        }
        return ColorUtil.translate("&" + color);
    }

    public void updateStatus() {

    }

    public void saveData() {

        for (Location location : pendingBlocks) {
            location.getBlock().setType(Material.AIR);
        }

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
            for (Perk perk : getPerks()) {
                load.set("selectedPerks." + perk.getName(), perk.getName());
            }
            for (Perk perk : getPurchasedPerks()) {
                load.set("purchasedPerks." + perk.getName(), perk.getName());
            }

            try {
                load.save(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadLayout() {

        for (Location location : pendingBlocks) {
            location.getBlock().setType(Material.AIR);
        }

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().addItem(ItemUtils.createItem(Material.IRON_SWORD));
        player.getInventory().addItem(ItemUtils.createItem(Material.BOW));

        ItemStack itemStack = new ItemStack(Material.ARROW);
        itemStack.setAmount(30);
        player.getInventory().setItem(8, itemStack);

        player.getInventory().setChestplate(ItemUtils.createItem(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(ItemUtils.createItem(Material.CHAINMAIL_LEGGINGS));
        player.getInventory().setBoots(ItemUtils.createItem(Material.CHAINMAIL_BOOTS));

        player.updateInventory();

        for (Perk perk : perks) {
            if (getPerks().contains(perk)) {
                perk.onLayout(this);
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
                loadLayout();
                level = 1;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            final YamlConfiguration load = YamlConfiguration.loadConfiguration(player);
            setLevel(load.getInt("levels"));
            setKills(load.getInt("kills"));
            setDeaths(load.getInt("deaths"));
            setGold(load.getDouble("gold"));
            setXp(load.getInt("xp"));
            setNeededXp(load.getInt("neededXp"));
            setBounty(load.getInt("bounty"));
            setKillStreak(load.getInt("killStreak"));
            for (String key : load.getConfigurationSection("selectedPerks").getKeys(false)) {
                perks.add(PerkLoader.INSTANCE.findItem(load.getString("selectedPerks." + key)));
            }
            for (String key : load.getConfigurationSection("purchasedPerks").getKeys(false)) {
                purchasedPerks.add(PerkLoader.INSTANCE.findItem(load.getString("purchasedPerks." + key)));
            }
        }
    }

    public enum Status {
        IDLE("&aIdle"), FIGHTING("&cFighting"), BOUNTIED("&cBountied");

        private final String name;

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
