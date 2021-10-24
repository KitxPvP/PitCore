package com.kitx.command;

import com.kitx.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return false;
        if(sender.hasPermission("core.admin")) {
            Player player = (Player) sender;
            Config.setSpawn(player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Set spawn at " + player.getLocation());
        }
        return false;
    }
}
