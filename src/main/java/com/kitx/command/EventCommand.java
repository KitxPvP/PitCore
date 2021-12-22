package com.kitx.command;

import com.kitx.PitCore;
import com.kitx.event.Event;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player player) {
            if(player.hasPermission("core.admin")) {
                if(args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Usage: /event [stop] [events]");
                    return false;
                }
                if(args[0].equalsIgnoreCase("stop")) {
                    PitCore.INSTANCE.getEventManager().stopCurrentEvent();
                }
                for(Event event : PitCore.INSTANCE.getEventManager().getEvents()) {
                    String rawEvent = ChatColor.stripColor(event.getName());
                    if(rawEvent.equalsIgnoreCase(args[0])) {
                        PitCore.INSTANCE.getEventManager().startEvent(event);
                    }
                }
            }
        }
        return false;
    }
}
