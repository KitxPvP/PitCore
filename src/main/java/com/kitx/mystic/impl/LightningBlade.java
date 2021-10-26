package com.kitx.mystic.impl;

import com.kitx.data.PlayerData;
import com.kitx.mystic.MysticItem;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LightningBlade extends MysticItem {
    public LightningBlade(int tier, int lives) {
        super("&eLightning Blade", tier, lives, "&7Strike &elightning &7onto enemies on the " + (5 - tier) + " hit");
    }

    @Override
    public void onHit(PlayerData player, PlayerData victim, EntityDamageByEntityEvent event) {
        if (getHit() == (5 - getTier())) {
            Location location = victim.getPlayer().getLocation().add(0, 1, 0);
            location.getWorld().spigot().strikeLightningEffect(location, true);
            player.getPlayer().playSound(location, Sound.AMBIENCE_THUNDER, 1, 1);
            victim.getPlayer().playSound(location, Sound.AMBIENCE_THUNDER, 1, 1);
            event.setDamage(event.getDamage() + (3 * getTier()));
        }
        super.onHit(player, victim, event);
    }
}
