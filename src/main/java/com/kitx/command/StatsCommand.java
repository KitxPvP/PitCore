package com.kitx.command;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            try {
                PlayerData data = DataManager.INSTANCE.get(args.length > 0 ? Bukkit.getPlayer(args[0]) : player);

                player.sendMessage(ChatColor.RED + "Stats of " + data.getPlayer().getName());
                player.sendMessage("");
                player.sendMessage(ColorUtil.translate("&7Level: &f" + data.getLevel()));
                player.sendMessage(ColorUtil.translate("&7Gold &f" + data.getGold()));
                player.sendMessage(ColorUtil.translate("&7Kills: &f" + data.getKills()));
                player.sendMessage(ColorUtil.translate("&7Kill Streak: &f" + data.getKillStreak()));
                player.sendMessage(ColorUtil.translate("&7Deaths: &f" + data.getDeaths()));

            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Player seems to be not online!");
            }
        }
        return false;
    }
}
