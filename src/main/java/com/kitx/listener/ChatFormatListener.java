package com.kitx.listener;


import com.kitx.data.DataManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();

        LuckPerms api = LuckPermsProvider.get();
        User user = api.getPlayerAdapter(Player.class).getUser(event.getPlayer());
        String prefix = user.getCachedData().getMetaData().getPrefix();
        String suffix = user.getCachedData().getMetaData().getSuffix();

        String header = DataManager.INSTANCE.get(p).getHeader();
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";
        if (p.hasPermission("kitx.staff")) {
            event.setFormat(ChatColor.translateAlternateColorCodes('&', header + " " + prefix + "" + p.getDisplayName() + suffix + ": &r" + event.getMessage()));
        } else {
            event.setFormat(ChatColor.translateAlternateColorCodes('&', header + " " + prefix + p.getDisplayName() + suffix + ": &7" + event.getMessage()));

        }
    }
}
