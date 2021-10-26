package com.kitx.mystic.impl;

import com.kitx.data.PlayerData;
import com.kitx.mystic.MysticItem;
import com.kitx.utils.ColorUtil;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonBlade extends MysticItem {

    public PoisonBlade(int tier, int lives) {
        super("&2Poison Blade", tier, lives, "&7Inflicts &2poison &7" + tier + " for " + (tier * 2) + " seconds on the " + (5 - tier) + " hit");
    }

    @Override
    public void onHit(PlayerData player, PlayerData victim, EntityDamageByEntityEvent event) {
        if (getHit() == (5 - getTier())) {
            player.getPlayer().sendMessage(ColorUtil.translate("&7You dealt &2poison &7 damage!"));
            int duration = (getTier() * 2) * 20;
            PotionEffect poison = new PotionEffect(PotionEffectType.POISON, duration, getTier() - 1);
            victim.getPlayer().addPotionEffect(poison);
        }
        super.onHit(player, victim, event);
    }
}
