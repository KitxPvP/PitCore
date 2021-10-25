package com.kitx.command;

import com.kitx.data.DataManager;
import com.kitx.gui.impl.PrestigeGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

public class PrestigeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            new PrestigeGui(DataManager.INSTANCE.get(player)).openGui();
        }
        return false;
    }
}
