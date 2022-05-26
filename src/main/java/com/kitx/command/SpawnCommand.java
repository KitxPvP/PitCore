package com.kitx.command;


import com.kitx.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Location location = Config.getLocation();
            if(location == null) {
                player.sendMessage(ChatColor.RED + "Spawn is currently not set. Please notify a Admin.");
            } else {
                player.teleport(location);
            }
        }
        return false;
    }
}
