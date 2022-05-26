package com.kitx.event.impl;

import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.event.Event;
import com.kitx.events.PitKillEvent;
import com.kitx.events.RegisterPlayerEvent;
import com.kitx.utils.ColorUtil;
import com.kitx.utils.CountDown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Teams extends Event {
    public Teams() {
        super("&e&lTeams", EventType.MAJOR, 4, 1500, new CountDown(300), "&aKill other team members, whichever team wins get's the most xp!");
    }

    private final List<PlayerData> blueTeam = new ArrayList<>();
    private final List<PlayerData> redTeam = new ArrayList<>();
    private int blueTeamKills, redTeamKills;

    @Override
    public void onStop() {
        if (blueTeamKills > redTeamKills) {
            Bukkit.broadcastMessage(ColorUtil.translate("&7[&eEvent&7] &bBlue &7team wins!"));
            blueTeam.forEach(data -> data.addGold(1000));
        } else {
            Bukkit.broadcastMessage(ColorUtil.translate("&7[&eEvent&7] &cRed &7team wins!"));
            redTeam.forEach(data -> data.addGold(1000));
        }
        blueTeam.clear();
        redTeam.clear();
        blueTeamKills = redTeamKills = 0;
        DataManager.INSTANCE.getPlayerDataMap().values().forEach(data -> data.setPrefix(data.getHeader() + " "));
        super.onStop();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager && event.getEntity() != null) {
            if (event.getEntity() instanceof Player victim) {
                PlayerData attacker = DataManager.INSTANCE.get(damager);
                PlayerData victimUser = DataManager.INSTANCE.get(victim);

                if ((redTeam.contains(attacker) && redTeam.contains(victimUser)) || (blueTeam.contains(attacker) && blueTeam.contains(victimUser))) {
                    event.setCancelled(true);
                    damager.sendMessage(ChatColor.RED + "You cannot attack your own teammate!");
                }
            }
        } else if(event.getDamager() instanceof Arrow arrow && event.getEntity() != null) {
            if(event.getEntity() instanceof Player victim && arrow.getShooter() instanceof Player damager) {
                PlayerData attacker = DataManager.INSTANCE.get(damager);
                PlayerData victimUser = DataManager.INSTANCE.get(victim);

                if ((redTeam.contains(attacker) && redTeam.contains(victimUser)) || (blueTeam.contains(attacker) && blueTeam.contains(victimUser))) {
                    event.setCancelled(true);
                    damager.sendMessage(ChatColor.RED + "You cannot attack your own teammate!");
                }
            }
        }
    }

    @Override
    public void onTick() {
        for(PlayerData data : DataManager.INSTANCE.getPlayerDataMap().values()) {
            data.setPrefix(blueTeam.contains(data) ? "\247b" : "\247c");
        }

        if(Math.abs(blueTeamKills - redTeamKills) > 10) {
            final boolean team = blueTeamKills > redTeamKills;
            if(team) {
                for(PlayerData data : redTeam) {
                    data.getPlayer().addPotionEffects(Arrays.asList(PotionEffectType.SPEED.createEffect(5, 0), PotionEffectType.INCREASE_DAMAGE.createEffect(5, 0)));
                }
            } else {
                for(PlayerData data : blueTeam) {
                    data.getPlayer().addPotionEffects(Arrays.asList(PotionEffectType.SPEED.createEffect(5, 0), PotionEffectType.INCREASE_DAMAGE.createEffect(5, 0)));
                }
            }
        }

        super.onTick();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        PlayerData data = DataManager.INSTANCE.get(event.getPlayer());
        blueTeam.remove(data);
        redTeam.remove(data);
    }

    @EventHandler
    public void onKill(PitKillEvent event) {
        if (blueTeam.contains(event.getKiller())) {
            blueTeamKills++;
        } else if (redTeam.contains(event.getKiller())) {
            redTeamKills++;
        }
    }

    @EventHandler
    public void onJoin(RegisterPlayerEvent event) {
        if (blueTeam.size() > redTeam.size()) {
            redTeam.add(event.getPlayer());
        } else {
            blueTeam.add(event.getPlayer());
        }
    }

    @Override
    public void onStart() {
        int count = 0;
        for(PlayerData data : DataManager.INSTANCE.getPlayerDataMap().values()) {
            count++;
            if(count % 2 == 0) {
                blueTeam.add(data);
            } else {
                redTeam.add(data);
            }
            data.getPlayer().sendMessage(ColorUtil.translate("&e&lKitX TEAM EVENT:"));
            data.getPlayer().sendMessage(ColorUtil.translate(blueTeam.contains(data) ? "&b&lYou are on the blue team!" : "&c&lYou are on the red team!"));
        }

        super.onStart();
    }

    @Override
    public String[] onScoreBoard(PlayerData data) {
        return new String[]{
                "&fBlue Team&7: &b" + blueTeamKills,
                "&fRed Team&7: &c" + redTeamKills
        };
    }

}
