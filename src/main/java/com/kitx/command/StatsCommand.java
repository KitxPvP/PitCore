package com.kitx.command;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
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

                player.sendMessage(ChatColor.GREEN + "Stats of " + data.getPlayer().getName());
                player.sendMessage("");
                player.sendMessage(ChatColor.GREEN + "level: " + data.getLevel());
                player.sendMessage(ChatColor.GREEN + "gold " + data.getGold());
                player.sendMessage(ChatColor.GREEN + "kills: " + data.getKills());
                player.sendMessage(ChatColor.GREEN + "kill streak: " + data.getKillStreak());
                player.sendMessage(ChatColor.GREEN + "deaths: " + data.getDeaths());

            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Invalid player!");
            }
        }
        return false;
    }
}
