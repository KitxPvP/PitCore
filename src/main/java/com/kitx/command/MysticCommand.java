package com.kitx.command;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.gui.impl.MysticGui;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MysticCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            PlayerData data = DataManager.INSTANCE.get(player);
            if(data.getPrestige() > 0) {
                new MysticGui(data).openGui();
            } else {
                player.sendMessage(ChatColor.RED + "You must be prestige to use this.");
            }
        }
        return false;
    }
}
