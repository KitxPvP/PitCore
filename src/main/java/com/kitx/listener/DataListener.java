package com.kitx.listener;

import com.kitx.PitCore;
import com.kitx.config.Config;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DataListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        new BukkitRunnable() {

            @Override
            public void run() {
                event.getPlayer().teleport(Config.getLocation());
            }
        }.runTaskLater(PitCore.INSTANCE.getPlugin(), 5);
        DataManager.INSTANCE.inject(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
        PlayerData player = DataManager.INSTANCE.get(event.getPlayer());
        if(!player.getCountDown().isFinished()) {
            for(Player players : Bukkit.getOnlinePlayers()) {
                if(players.hasPermission("core.staff")) {
                    players.sendMessage(ColorUtil.translate(String.format("&c%s has logged out the server in combat!", event.getPlayer().getName())));
                }
            }
        }
        player.unregisterNameTag();
        DataManager.INSTANCE.deject(event.getPlayer());
    }
}
