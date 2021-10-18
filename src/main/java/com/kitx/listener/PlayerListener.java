package com.kitx.listener;

import com.kitx.PitCore;
import com.kitx.config.Config;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkLoader;
import com.kitx.utils.ColorUtil;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        event.setDeathMessage("");
        Player killed = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if(killer != null) {
            PlayerData killedUser = DataManager.INSTANCE.get(killed);
            PlayerData killerUser = DataManager.INSTANCE.get(killer);

            killedUser.setDeaths(killedUser.getDeaths() + 1);
            killerUser.setKills(killerUser.getKills() + 1);

            killerUser.setKillStreak(killerUser.getKillStreak() + 1);

            killedUser.setKillStreak(0);

            double xpAdd = Math.ceil(Math.random() * 5) + 5;
            killerUser.setXp((int) (killerUser.getXp() + xpAdd));

            if(killerUser.getXp() >= killerUser.getNeededXp()) {
                killerUser.setXp(0);
                killerUser.setLevel(killerUser.getLevel() + 1);
                killerUser.setNeededXp(killerUser.getLevel() * 25);
                killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1, 1);
            } else {
                killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1, 1);
            }

            double addedGold;

            if (killedUser.getKillStreak() >= 5) {
                addedGold = killedUser.getKillStreak() / 2.0 + 10;
            } else {
                addedGold = randomNumber(10, 5);
            }
            addedGold = Math.round(addedGold * 100.0) / 100.0;
            killerUser.setGold(BigDecimal.valueOf(killerUser.getGold()).add(BigDecimal.valueOf(addedGold)).doubleValue());

            killer.sendMessage(ColorUtil.translate(Config.KILL_MESSAGE)
                    .replaceAll("%player%", killedUser.getHeader() + " \2477" + killed.getName())
                    .replaceAll("%xp%", String.valueOf(xpAdd))
                    .replaceAll("%gold%", String.valueOf(addedGold)));
            killed.sendMessage(ColorUtil.translate(Config.DEATH_MESSAGE)
                    .replaceAll("%player%", killerUser.getHeader() + " \2477" + killer.getName()));

            if(killerUser.getKillStreak() % 5 == 0 && killerUser.getKillStreak() > 0) {
                Bukkit.broadcastMessage(ColorUtil.translate(Config.KILLSTREAK_MESSAGE)
                        .replaceAll("%player%", killerUser.getHeader() + " \2477" + killer.getName())
                        .replaceAll("%streak%", String.format("%s", killerUser.getKillStreak())));
            }


        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(PitCore.INSTANCE.getPlugin(), () -> {
            killed.spigot().respawn();
        }, 5L);
    }

    @EventHandler
    public void onHungerDeplete(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getEntity();
        player.setFoodLevel(20);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().hasPermission("pit.admin")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player p = event.getPlayer();
        Location l = p.getLocation();
        Vector vector = p.getEyeLocation().getDirection().multiply(2);
        vector.setY(0.5);
        Location d = new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ());
        if(d.getBlock().getType() == Material.SLIME_BLOCK) {
            p.setVelocity(vector);

            p.playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerData data = DataManager.INSTANCE.get(player);
        data.loadLayout();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            if (e.getClickedInventory() != null && e.getClickedInventory().getName() != null) {
                Inventory inventory = e.getInventory();
                PlayerData data = DataManager.INSTANCE.get(player);

                switch (inventory.getName().toLowerCase()) {
                    case "non-permanent items": {
                        e.setCancelled(true);
                        if (e.getCurrentItem() == null) return;
                        if (e.getCurrentItem().getItemMeta() == null) return;
                        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                        switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase())) {
                            case "diamond sword": {
                                if(data.getGold() > 150) {
                                    data.setGold(data.getGold() - 150);
                                    ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    itemMeta.spigot().setUnbreakable(true);
                                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                    List<String> lore = new ArrayList<>();
                                    lore.add(ColorUtil.translate("&cLost on death."));
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);

                                    player.getInventory().addItem(itemStack);
                                    player.updateInventory();
                                } else {
                                    player.sendMessage(ChatColor.RED + "Not enough gold");
                                }
                                break;
                            }
                            case "obsidian": {
                                if(data.getGold() > 50) {
                                    data.setGold(data.getGold() - 50);
                                    ItemStack itemStack = new ItemStack(Material.OBSIDIAN);
                                    itemStack.setAmount(8);
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    itemMeta.spigot().setUnbreakable(true);
                                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                    List<String> lore = new ArrayList<>();
                                    lore.add(ColorUtil.translate("&cLost on death."));
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);

                                    player.getInventory().addItem(itemStack);
                                    player.updateInventory();
                                } else {
                                    player.sendMessage(ChatColor.RED + "Not enough gold");
                                }
                                break;
                            }
                            case "diamond chestplate": {
                                if(data.getGold() > 500) {
                                    data.setGold(data.getGold() - 500);
                                    ItemStack itemStack = new ItemStack(Material.DIAMOND_CHESTPLATE);
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    itemMeta.spigot().setUnbreakable(true);
                                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                    List<String> lore = new ArrayList<>();
                                    lore.add(ColorUtil.translate("&cLost on death."));
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);

                                    player.getInventory().addItem(itemStack);
                                    player.updateInventory();
                                } else {
                                    player.sendMessage(ChatColor.RED + "Not enough gold");
                                }
                                break;
                            }
                            case "diamond boots": {
                                if(data.getGold() > 300) {
                                    data.setGold(data.getGold() - 300);
                                    ItemStack itemStack = new ItemStack(Material.DIAMOND_BOOTS);
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    itemMeta.spigot().setUnbreakable(true);
                                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                    List<String> lore = new ArrayList<>();
                                    lore.add(ColorUtil.translate("&cLost on death."));
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);

                                    ItemStack clone = player.getInventory().getBoots();
                                    if (clone != null)
                                        player.getInventory().addItem(clone);

                                    player.getInventory().setChestplate(itemStack);
                                    player.updateInventory();
                                } else {
                                    player.sendMessage(ChatColor.RED + "Not enough gold");
                                }
                                break;
                            }
                        }

                        break;
                    }
                    case "perks": {
                        e.setCancelled(true);
                        if (e.getCurrentItem() == null) return;
                        if (e.getCurrentItem().getItemMeta() == null) return;
                        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
                        for (Perk perk : PerkLoader.INSTANCE.getPerkList()) {
                            String name = perk.getName().replaceAll("&", "");
                            String clickedName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                            if (name.contains(clickedName)) {
                                if(!data.getPurchasedPerks().contains(perk)) {
                                    if(data.getGold() > perk.getCost()) {
                                        player.sendMessage(ChatColor.GREEN + "You purchased " + clickedName);
                                        data.setGold(data.getGold() - perk.getCost());
                                        data.getPurchasedPerks().add(perk);
                                    }  {
                                        player.sendMessage(ChatColor.RED + "You do not have enough gold for that!");
                                    }
                                } else {
                                    if(data.getPerks().contains(perk)) {
                                        player.sendMessage(ChatColor.RED + "You unselected " + clickedName);
                                        data.getPerks().remove(perk);
                                    } else {
                                        player.sendMessage(ChatColor.GREEN + "You selected " + clickedName);
                                        data.getPerks().add(perk);
                                        perk.onClick(data);
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public double randomNumber(float max, float min) {
        return (min + Math.random() * ((max - min)));
    }


    @EventHandler
    public void onLavaFlow(BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        PlayerData data = DataManager.INSTANCE.get(event.getPlayer());
        Location clicked = event.getBlockClicked().getLocation();
        BlockFace f = event.getBlockFace();
        Location lava = new Location(clicked.getWorld(), clicked.getX() + f.getModX(), clicked.getY() + f.getModY(), clicked.getZ() + f.getModZ());
        data.getPendingBlocks().add(lava);
    }

    @EventHandler
    public void onPlayerFill(PlayerBucketFillEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.canBuild()) return;
        if (event.isCancelled()) return;
        ItemStack itemStack = event.getItemInHand();
        if (itemStack == null) return;
        if (itemStack.getType() == Material.OBSIDIAN) {
            PlayerData data = DataManager.INSTANCE.get(event.getPlayer());
            data.getPendingBlocks().add(event.getBlock().getLocation());
            Bukkit.getScheduler().runTaskLater(PitCore.INSTANCE.getPlugin(), () -> {
                event.getBlock().setType(Material.AIR);
            }, 2400);
        }
    }
}
