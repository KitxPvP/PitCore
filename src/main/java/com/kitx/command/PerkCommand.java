package com.kitx.command;

import com.kitx.data.DataManager;
import com.kitx.gui.impl.PermGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            new PermGui(DataManager.INSTANCE.get(((Player) sender).getPlayer())).openGui();
        }
        return false;
    }
}
