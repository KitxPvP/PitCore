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

public class BountyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender.hasPermission("core.admin")) {
            if (args.length > 1) {
                int amount = Integer.parseInt(args[1]);
                if (amount == 0) {
                    sender.sendMessage(ChatColor.RED + "Amount not a number!");
                    return false;
                }

                final Player player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player isn't on!");
                    return false;
                }

                PlayerData targetData = DataManager.INSTANCE.get(player);

                targetData.bountyPlayer(amount);
                Bukkit.broadcastMessage(ColorUtil.translate("&6&lBOUNTY! &7bump &6" + amount + "g &7on " + targetData.getHeader() + " &7" + player.getName()));
            } else {
                sender.sendMessage(ChatColor.GREEN + "Usage: /bounty [player] [amount]");
            }
        }
        return false;
    }
}
