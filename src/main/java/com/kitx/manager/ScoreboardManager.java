package com.kitx.manager;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.scoreboard.FastBoard;
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

            board.updateTitle(ColorUtil.translate("&e&lKITX"));
            board.updateLine(0, ColorUtil.translate(""));
            board.updateLine(1, ColorUtil.translate("&fLevel&7: " + pData.getHeader()));
            board.updateLine(2, ColorUtil.translate("&fNeeded XP&7: &b" + pData.getNeededXp()));
            board.updateLine(3, "");
            board.updateLine(4, ColorUtil.translate("&fGold&7: &6" + pData.getGold()));
            board.updateLine(5, "");
            board.updateLine(6, ColorUtil.translate("&fStatus&7: &aIdle"));
            board.updateLine(7, "");
            board.updateLine(8, ColorUtil.translate("&ekitx.minehut.gg"));
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
