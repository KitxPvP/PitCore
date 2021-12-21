package com.kitx.manager;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.scoreboard.FastBoard;
import com.kitx.utils.ColorUtil;
import com.kitx.utils.RomanNumber;
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
            try {
                pData.getPlayer().setPlayerListName(ColorUtil.translate(pData.getHeader() + " " + pData.getPrefix() + pData.getPlayer().getName()));
            } catch (Exception e) {
                //ignored
            }
            board.updateTitle(ColorUtil.translate("&e&lKITX"));
            board.updateLine(pData.count(), ColorUtil.translate(""));
            if(pData.getPrestige() > 0) board.updateLine(pData.count(), ColorUtil.translate("&fPrestige&7: " + pData.prestigeColor() + RomanNumber.toRoman(pData.getPrestige())));
            board.updateLine(pData.count(), ColorUtil.translate("&fLevel&7: " + pData.getHeader()));
            final double percent = pData.getXp() * 100D / pData.getNeededXp();
            board.updateLine(pData.count(), ColorUtil.translate("&fProgress&7: &b" + "&7(&b" + Math.round(percent) + "%&7)"));
            board.updateLine(pData.count(), "");
            board.updateLine(pData.count(), ColorUtil.translate("&fGold&7: &6" + pData.getGold()) + "g");
            board.updateLine(pData.count(), "");
            board.updateLine(pData.count(), ColorUtil.translate("&fStatus&7: " + pData.getStatus().getName()));
            switch (pData.getStatus()) {
                case BOUNTIED: {
                    board.updateLine(pData.count(), "&fBounty&7: &6" + pData.getBounty());
                    pData.getRemoveLines().add(pData.getScoreboard());
                    break;
                }
                case FIGHTING: {
                    board.updateLine(pData.count(), ColorUtil.translate("&cTag&7: &c" + pData.getCountDown().convertTime()));
                    pData.getRemoveLines().add(pData.getScoreboard());
                    break;
                }
            }
            board.updateLine(pData.count(), "");
            board.updateLine(pData.count(), ColorUtil.translate("&ekitx.minehut.gg"));

            pData.setScoreboard(0);

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

    public FastBoard get(PlayerData data) {
        return this.boards.get(data);
    }
}
