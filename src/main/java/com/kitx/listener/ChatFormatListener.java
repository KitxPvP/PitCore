package com.kitx.listener;


import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.utils.ColorUtil;
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
        PlayerData data = DataManager.INSTANCE.get(p);
        String header = data.getHeader();
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";
        if (!event.getPlayer().hasPermission("core.vip")) {
            if (!data.getChatCD().hasCooldown(2)) {
                p.sendMessage(ColorUtil.translate(String.format("&cThere is a &4%s &csecond chat cooldown!", data.getChatCD().getSeconds())));
                event.setCancelled(true);
            }
        }
        if (p.hasPermission("core.white")) {
            event.setMessage(parse(event.getMessage()));
            event.setFormat(ChatColor.translateAlternateColorCodes('&', header + " " + prefix + "" + p.getName() + suffix + ": &r") + "%2$s");
        } else {
            event.setFormat(ChatColor.translateAlternateColorCodes('&', header + " " + prefix + p.getName() + suffix + ": &7") + "%2$s");

        }
    }

    private String parse(String message) {
        boolean parseStar = false;
        StringBuilder parsedStr = new StringBuilder();
        for(char character : message.toCharArray()) {
            if(character == '*') {
                if(parseStar) {
                    message = message.replaceFirst("\\*", "");
                    message = message.replaceFirst("\\*", "");
                    return message.replace(parsedStr, "\247o"+ parsedStr + "\247r");
                }
                parseStar = true;
            } else {
                if(parseStar) {

                    parsedStr.append(character);
                }

            }

        }
        return message;
    }
}

