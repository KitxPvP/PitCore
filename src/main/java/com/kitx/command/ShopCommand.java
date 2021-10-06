package com.kitx.command;

import com.kitx.data.DataManager;
import com.kitx.gui.impl.TempGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            new TempGui(DataManager.INSTANCE.get(((Player) commandSender).getPlayer())).openGui();
        }
        return false;
    }
}
