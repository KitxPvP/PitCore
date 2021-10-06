package com.kitx.scoreboard;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.utils.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {

    private final DataManager data = DataManager.INSTANCE;
    private final Map<PlayerData, FastBoard> boards = new HashMap<>();

    public ScoreboardManager() {

        new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimerAsynchronously(PitCore.INSTANCE.getPlugin(), 0, 2);

    }

    private void update() {
        for (Map.Entry<PlayerData, FastBoard> entry : boards.entrySet()) {
            PlayerData pData = entry.getKey();
            FastBoard board = entry.getValue();

            entry.getKey().getPlayer().setPlayerListName(entry.getKey().getPlayer().getDisplayName());

            board.updateTitle(ColorUtil.translate("&6&lKitX &7┃ &fKits"));
            board.updateLine(0, ColorUtil.translate("&7&m------------------"));
            board.updateLine(1, ColorUtil.translate("&cLevel: &f" + pData.getLevel()));
            board.updateLine(2, ColorUtil.translate("&cKills: &f" + pData.getKills()));
            board.updateLine(3, ColorUtil.translate("&cDeaths: &f" + pData.getDeaths()));
            board.updateLine(4, ColorUtil.translate("&cKillstreak: &f" + pData.getKillStreak()));
            board.updateLine(5, ColorUtil.translate("&cProgress: &f" + pData.getXp() + "/" + pData.getNeededXp()));
            board.updateLine(6, "");
            board.updateLine(7, "strafekits.cf");
            board.updateLine(8, ColorUtil.translate("&7&m------------------"));
        }
    }

    public void create(Player player) {
        FastBoard board = new FastBoard(player);

        this.boards.put(data.get(player), board);
    }

    public void remove(Player player) {
        FastBoard board = this.boards.get(data.get(player));

        this.boards.remove(data.get(player));

        if (board != null) {
            board.delete();
        }

    }

    public FastBoard get(Player player) {
        return this.boards.get(data.get(player));
    }
}
