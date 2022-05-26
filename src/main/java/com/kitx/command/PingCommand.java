package com.kitx.command;

import com.kitx.utils.ColorUtil;
import com.kitx.utils.PingUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            sender.sendMessage(ColorUtil.translate("&c" + player.getPlayer().getName() + " &7has a ping of &c" + PingUtil.getPlayerPing(player) + "ms"));
        }
        return false;
    }
}
