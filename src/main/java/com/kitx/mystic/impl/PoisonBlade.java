package com.kitx.mystic.impl;

import com.kitx.data.PlayerData;
import com.kitx.mystic.MysticItem;
import com.kitx.utils.ColorUtil;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonBlade extends MysticItem {

    public PoisonBlade(int tier, int lives) {
        super("&2Poison Blade", tier, lives, "&7Inflicts &2poison &7" + tier + " for " + (tier * 2) + "seconds on the " + (5 - tier) + " hit");
    }

    @Override
    public void onHit(PlayerData player, PlayerData victim) {
        if (getHit() == (5 - getTier())) {
            player.getPlayer().sendMessage(ColorUtil.translate("&7You dealt &2poison &7 damage!"));
            PotionEffect poison = PotionEffectType.POISON.createEffect(getTier() * 2, getTier() - 1);
            victim.getPlayer().addPotionEffect(poison);
        }
        super.onHit(player, victim);
    }
}
