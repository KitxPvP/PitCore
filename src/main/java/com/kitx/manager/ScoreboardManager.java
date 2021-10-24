package com.kitx.manager;

import com.kitx.PitCore;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.scoreboard.FastBoard;
import com.kitx.utils.ColorUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
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
            board.updateLine(0, ColorUtil.translate(""));
            board.updateLine(1, ColorUtil.translate("&fLevel&7: " + pData.getHeader()));
            final double percent = pData.getXp() * 100D / pData.getNeededXp();
            board.updateLine(2, ColorUtil.translate("&fProgress&7: &b" + "&7(&b" + Math.round(percent) + "%&7)"));
            board.updateLine(3, "");
            board.updateLine(4, ColorUtil.translate("&fGold&7: &6" + pData.getGold()));
            board.updateLine(5, "");
            board.updateLine(6, ColorUtil.translate("&fStatus&7: " + pData.getStatus().getName()));
            if(pData.getStatus() == PlayerData.Status.BOUNTIED) {
                board.updateLine(7, "&fBounty&7: &6" + pData.getBounty());
                board.updateLine(8, "");
                board.updateLine(9, ColorUtil.translate("&ekitx.minehut.gg"));
            } else if(pData.getStatus() == PlayerData.Status.FIGHTING) {
                board.updateLine(7, ColorUtil.translate("&cTag&7: &c" + pData.getCountDown().convertTime()));
                board.updateLine(8, "");
                board.updateLine(9, ColorUtil.translate("&ekitx.minehut.gg"));
            } else {
                board.updateLine(7, "");
                board.updateLine(8, ColorUtil.translate("&ekitx.minehut.gg"));
            }

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
