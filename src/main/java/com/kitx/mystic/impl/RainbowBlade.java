package com.kitx.mystic.impl;

import com.kitx.data.PlayerData;
import com.kitx.mystic.MysticItem;
import com.kitx.utils.ColorUtil;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class RainbowBlade extends MysticItem {

    public RainbowBlade(int tier, int lives) {
        super("&2Rainbow Blade", tier, lives, "&7Turns the player into a rainbow. with motion: " + tier*0.85);
    }

    @Override
    public void onHit(PlayerData player, PlayerData victim, EntityDamageByEntityEvent event) {
        double yaw = player.getPlayer().getEyeLocation().getYaw();
        if (getHit() == (5 - getTier()) || getTier() >= 5) {
            player.getPlayer().sendMessage(ColorUtil.translate("&7You turned " + player.getPlayer().getName() + " into a rainbow!"));
            victim.getPlayer().setVelocity(new Vector((getTier()*(getTier()*0.45)) * -Math.sin(yaw),getTier()*0.6,(getTier()*(getTier()*0.45)) * Math.cos(yaw)));
        }
        if (getTier() >= 7) {
            player.getPlayer().setVelocity(new Vector((getTier()*-0.5) * -Math.sin(yaw),getTier()*0.2,(getTier()*-0.5) * Math.cos(yaw)));
        }
        super.onHit(player, victim, event);
    }
}
