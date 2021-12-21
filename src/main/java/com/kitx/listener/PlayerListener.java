package com.kitx.listener;

import com.kitx.PitCore;
import com.kitx.config.Config;
import com.kitx.data.DataManager;
import com.kitx.data.PlayerData;
import com.kitx.events.PitKillEvent;
import com.kitx.gui.impl.SelectGui;
import com.kitx.gui.impl.SlotGui;
import com.kitx.mystic.MysticItem;
import com.kitx.mystic.MysticLoader;
import com.kitx.permanent.Perk;
import com.kitx.permanent.PerkLoader;
import com.kitx.permanent.impl.GoldenHeadPerk;
import com.kitx.utils.ColorUtil;
import com.kitx.utils.ItemUtils;
import com.kitx.utils.RomanNumber;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {


    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if(event.getEntity().getKiller() != null) {
            Player killed = event.getEntity();
            Player killer = event.getEntity().getKiller();

            PlayerData killedUser = DataManager.INSTANCE.get(killed);
            PlayerData killerUser = DataManager.INSTANCE.get(killer);

            for(Perk perk : killerUser.getPerks()) {
                perk.onKill(killerUser, killedUser);
            }

        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(PitCore.INSTANCE.getPlugin(), () -> {
            event.getEntity().spigot().respawn();
        }, 5L);
    }
    
    @EventHandler
    public void onKill(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player killed = (Player) event.getEntity();
            Player killer = null;
            if(event.getDamager() instanceof Player) {
                killer = (Player) event.getDamager();
            } else if(event.getDamager() instanceof Arrow) {
                ProjectileSource source = ((Arrow) event.getDamager()).getShooter();
                if(source instanceof Player) {
                    killer = (Player) source;
                }
            }
            if(killed.getHealth() - event.getDamage() < 1) {
                event.setCancelled(true);
                if (killer != null) {
                    killed.teleport(Config.getLocation());
                    killed.setHealth(20);
                    event.setDamage(0);
                    for(PotionEffect effect : killed.getActivePotionEffects()){
                        killed.removePotionEffect(effect.getType());
                    }

                    PlayerData killedUser = DataManager.INSTANCE.get(killed);
                    PlayerData killerUser = DataManager.INSTANCE.get(killer);
                    
                    boolean hasGoldenHead = false;
                    
                    for(Perk perk : killerUser.getPerks()) {
                        if(perk instanceof GoldenHeadPerk) hasGoldenHead = true;
                        perk.onKill(killerUser, killedUser);
                    }
                    
                    if(!hasGoldenHead) killer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                    
                    killedUser.respawn();

                    killedUser.setDeaths(killedUser.getDeaths() + 1);
                    killerUser.setKills(killerUser.getKills() + 1);

                    killerUser.setKillStreak(killerUser.getKillStreak() + 1);

                    killedUser.setKillStreak(0);

                    double xpAdd = (int) Math.abs(Math.ceil(Math.random() * killerUser.getLevel() - Math.ceil(Math.random() * killerUser.getLevel()))) + 50;
                    double addedGold;

                    if (killedUser.getKillStreak() >= 5) {
                        addedGold = killedUser.getKillStreak() / 2.0 + 10;
                    } else {
                        addedGold = randomNumber(10, 5);
                    }
                    addedGold = Math.round(addedGold * 100.0) / 100.0;

                    PitKillEvent pitKillEvent = new PitKillEvent(killedUser, killerUser, addedGold, xpAdd);
                    Bukkit.getPluginManager().callEvent(pitKillEvent);
                    addedGold = pitKillEvent.getGold();
                    xpAdd = pitKillEvent.getXp();

                    killerUser.setXp((int) (killerUser.getXp() + xpAdd));

                    if (killerUser.getXp() >= killerUser.getNeededXp()) {
                        killerUser.setXp(0);
                        String lastHeader = killerUser.getHeader();
                        killerUser.setLevel(killerUser.getLevel() + 1);
                        String header = killerUser.getHeader();
                        killerUser.setNeededXp(killerUser.getLevel() * 25);
                        killerUser.updateNameTag();
                        killer.sendTitle(ColorUtil.translate("&b&lLEVEL UP!"), ColorUtil.translate(lastHeader + " &7→ " + header));
                        killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 3, 1);
                    } else {
                        killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1, 1);
                    }

                    killerUser.setGold(BigDecimal.valueOf(killerUser.getGold()).add(BigDecimal.valueOf(addedGold)).doubleValue());

                    killer.sendMessage(ColorUtil.translate(Config.KILL_MESSAGE)
                            .replaceAll("%player%", killedUser.getHeader() + " \2477" + killed.getName())
                            .replaceAll("%xp%", String.valueOf(xpAdd))
                            .replaceAll("%gold%", String.valueOf(addedGold)));
                    killed.sendMessage(ColorUtil.translate(Config.DEATH_MESSAGE)
                            .replaceAll("%player%", killerUser.getHeader() + " \2477" + killer.getName()));

                    if (killerUser.getKillStreak() % 5 == 0 && killerUser.getKillStreak() > 0) {
                        Bukkit.broadcastMessage(ColorUtil.translate(Config.KILLSTREAK_MESSAGE)
                                .replaceAll("%player%", killerUser.getHeader() + " \2477" + killer.getName())
                                .replaceAll("%streak%", String.format("%s", killerUser.getKillStreak())));
                    }

                    if (killedUser.getStatus() == PlayerData.Status.BOUNTIED) {
                        PitCore.INSTANCE.getScoreboardManager().get(killedUser).removeLine(9);
                    }

                    if (killerUser.getPrestige() > 0) {
                        int percent = killerUser.getPrestige() * 30;

                        int chance = (int) ((((percent / 100) + 1) * 0.266) * 100);

                        if (randomInt(0, 500) < chance) {
                            ItemStack stack = ItemUtils.createItem(Material.GOLD_SWORD);
                            ItemMeta itemMeta = stack.getItemMeta();
                            itemMeta.setDisplayName(ColorUtil.translate("&dUncovered Mystic Item"));
                            List<String> lore = new ArrayList<>();
                            lore.add(ColorUtil.translate("&7Return to the mystic well."));
                            lore.add("");
                            lore.add(ColorUtil.translate("&7Used in the mystic well"));
                            itemMeta.setLore(lore);
                            stack.setItemMeta(itemMeta);
                            killer.getInventory().addItem(stack);
                        }
                    }
                }
            }
        }
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

    /*
    Jump pad logic
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player p = event.getPlayer();
        PlayerData data = DataManager.INSTANCE.get(p);
        Location l = p.getLocation();
        Vector vector = p.getEyeLocation().getDirection().multiply(3);
        vector.setY(0.7);
        Location d = new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ());
        if (d.getBlock().getType() == Material.SLIME_BLOCK && System.currentTimeMillis() - data.getLastJumpPad() > 1000) {
            p.setVelocity(vector);
            data.setLastJumpPad(System.currentTimeMillis());
            p.playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        PlayerData data = DataManager.INSTANCE.get(event.getPlayer());
        String[] args = event.getMessage().split(" ");
        if (!data.getCountDown().isFinished() && args[0].toLowerCase().contains("/spawn")) {
            event.getPlayer().sendMessage(ChatColor.RED + "You are in combat!");
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerData data = DataManager.INSTANCE.get(player);
        data.respawn();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
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
                                if (data.getGold() > 150) {
                                    data.setGold(BigDecimal.valueOf(data.getGold()).subtract(BigDecimal.valueOf(150)).doubleValue());
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
                                if (data.getGold() > 50) {
                                    data.setGold(BigDecimal.valueOf(data.getGold()).subtract(BigDecimal.valueOf(50)).doubleValue());
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
                                if (data.getGold() > 500) {
                                    data.setGold(BigDecimal.valueOf(data.getGold()).subtract(BigDecimal.valueOf(500)).doubleValue());
                                    ItemStack itemStack = new ItemStack(Material.DIAMOND_CHESTPLATE);
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    itemMeta.spigot().setUnbreakable(true);
                                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                    List<String> lore = new ArrayList<>();
                                    lore.add(ColorUtil.translate("&cLost on death."));
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    ItemStack clone = player.getInventory().getChestplate();
                                    if (clone != null)
                                        player.getInventory().addItem(clone);
                                    player.getInventory().setChestplate(itemStack);
                                    player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 1, 1);
                                    player.updateInventory();
                                } else {
                                    player.sendMessage(ChatColor.RED + "Not enough gold");
                                }
                                break;
                            }
                            case "diamond boots": {
                                if (data.getGold() > 300) {
                                    data.setGold(BigDecimal.valueOf(data.getGold()).subtract(BigDecimal.valueOf(300)).doubleValue());
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

                                    player.getInventory().setBoots(itemStack);
                                    player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 1, 1);
                                    player.updateInventory();
                                } else {
                                    player.sendMessage(ChatColor.RED + "Not enough gold");
                                }
                                break;
                            }
                        }

                        break;
                    }
                    case "prestige": {
                        e.setCancelled(true);
                        if (e.getCurrentItem() == null) return;
                        if (e.getCurrentItem().getItemMeta() == null) return;
                        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                        if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("\247bPrestige")) {
                            if(data.getLevel() >= 120) {
                                data.setLevel(1);
                                data.getPurchasedPerks().clear();
                                data.getPerks().clear();
                                data.getMysticItems().clear();
                                data.setGold(0);
                                data.setPrestige(data.getPrestige() + 1);
                                data.loadLayout();
                                Bukkit.broadcastMessage(ColorUtil.translate("&e&lPRESTIGE! &6" + player.getName() + " &7 unlocked prestige &e" + RomanNumber.toRoman(data.getPrestige()) + "&7, gg!"));
                            } else {
                                player.sendMessage(ChatColor.RED + "You need level 120 to prestige!");
                            }
                        }

                        break;
                    }
                    case "mystic well": {
                        e.setCancelled(true);
                        if (e.getCurrentItem() == null) return;
                        if (e.getCurrentItem().getItemMeta() == null) return;
                        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Uncovered Mystic Item")) {
                            switch (e.getCurrentItem().getType()) {
                                case GOLD_SWORD: {
                                    ItemStack sword = new ItemStack(e.getCurrentItem());
                                    ItemMeta swordMeta = sword.getItemMeta();
                                    swordMeta.getLore().remove("\2477Used in the mystic well");
                                    sword.setItemMeta(swordMeta);

                                    e.getInventory().setItem(10, sword);
                                    ItemStack stack = new ItemStack(Material.GOLD_BLOCK);
                                    ItemMeta meta = stack.getItemMeta();
                                    meta.setDisplayName(ColorUtil.translate("&dDiscover Item"));
                                    List<String> lore = new ArrayList<>();
                                    lore.add(ColorUtil.translate("&7Cost: &65000 gold"));
                                    meta.setLore(lore);
                                    stack.setItemMeta(meta);
                                    e.getInventory().setItem(16, stack);
                                    break;
                                }
                                case BOW: {

                                    break;
                                }
                            }
                        } else if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Discover Item")) {
                            if(data.getGold() >= 5000) {
                                data.setGold(BigDecimal.valueOf(data.getGold()).subtract(BigDecimal.valueOf(5000)).doubleValue());
                                Class<?> mysticClass = MysticLoader.INSTANCE.MYSTICS[randomInt(0, (MysticLoader.INSTANCE.MYSTICS.length - 1))];
                                try {


                                    MysticItem item = (MysticItem) mysticClass
                                            .getConstructor(int.class, int.class)
                                            .newInstance(randomInt(4, 0), randomInt(20, 4));
                                    data.getMysticItems().add(item);

                                    List<ItemStack> found = new ArrayList<>();
                                    for(ItemStack itemStack : player.getInventory().getContents()) {
                                        if(itemStack == null) continue;
                                        if(itemStack.getItemMeta() == null) continue;
                                        if(itemStack.getItemMeta().getDisplayName() == null) continue;

                                        for(MysticItem mysticItem : data.getMysticItems()) {
                                            String name = mysticItem.getName().replaceAll("&", "\247");
                                            if(name.equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())) {
                                                found.add(itemStack);
                                            }
                                        }
                                    }

                                    for(ItemStack itemStack : found) {
                                        player.getInventory().remove(itemStack);
                                    }
                                    data.updateMystics();
                                    player.closeInventory();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    player.sendMessage(ChatColor.RED + "Something went wrong!");
                                    player.closeInventory();
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "Not enough gold!");
                            }
                        }
                        break;
                    }
                    case "perks 3":
                    case "perks 2":
                    case "perks 1": {
                        e.setCancelled(true);
                        if (e.getCurrentItem() == null) return;
                        if (e.getCurrentItem().getItemMeta() == null) return;
                        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
                        int slot = Integer.parseInt(inventory.getName().split(" ")[1]) - 1;
                        for (Perk perk : PerkLoader.INSTANCE.getPerkList()) {
                            String name = perk.getName().replaceAll("&", "");
                            String clickedName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                            if (name.contains(clickedName)) {

                                if (!data.getPurchasedPerks().contains(perk)) {
                                    if(data.getPrestige() >= perk.getRequiredPrestige()) {
                                        if (data.getGold() > perk.getCost()) {
                                            player.sendMessage(ChatColor.GREEN + "You purchased " + clickedName);
                                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                                            data.setGold(BigDecimal.valueOf(data.getGold()).subtract(BigDecimal.valueOf(perk.getCost())).doubleValue());
                                            data.getPurchasedPerks().add(perk);
                                        } else {
                                            player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                                            player.sendMessage(ChatColor.RED + "You do not have enough gold for that!");
                                        }
                                    } else {
                                        player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                                        player.sendMessage(ChatColor.RED + "You are not the right prestige level for that perk!");
                                    }
                                } else {
                                    if (data.getPerks().contains(perk)) {
                                        player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                                        player.sendMessage(ChatColor.RED + "You already selected " + clickedName);
                                    } else {
                                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                                        player.sendMessage(ChatColor.GREEN + "You selected " + clickedName);
                                        try {
                                            if (data.getPerks().get(slot) != null) {
                                                data.getPerks().set(slot, perk);
                                            }
                                        } catch (Exception ignored) {
                                            data.getPerks().add(perk);
                                        }
                                        perk.onClick(data);
                                    }
                                }
                                new SelectGui(data, slot + 1).openGui();
                                player.updateInventory();
                            }
                        }
                        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Back")) {
                            new SlotGui(data).openGui();
                        }
                        break;
                    }
                    case "perk slots": {
                        e.setCancelled(true);
                        if (e.getCurrentItem() == null) return;
                        if (e.getCurrentItem().getItemMeta() == null) return;
                        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
                        int slot = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().split("#")[1]) - 1;
                        if ((slot == 1 && data.getLevel() >= 35) || (slot == 2 && data.getLevel() >= 75) || slot == 0) {
                            new SelectGui(data, slot + 1).openGui();
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have the required level!");
                        }
                        break;
                    }
                    case "chest":
                    case "ender chest": {
                        if (e.getCurrentItem() == null) return;
                        if (e.getCurrentItem().getItemMeta() == null) return;
                        try {
                            if(e.getCurrentItem().getItemMeta().getLore().get(0).contains("Lost on death.")) {
                                e.setCancelled(true);
                            }
                        } catch(Exception ignored) {}
                        break;
                    }
                }
            }
        }
    }

    public int randomInt(int min, int max) {
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }

    public double randomNumber(float max, float min) {
        return (min + Math.random() * ((max - min)));
    }

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        event.getEntity().remove();
    }

    @EventHandler
    public void onLavaFlow(BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            PlayerData damage = DataManager.INSTANCE.get((Player) event.getDamager());
            PlayerData entity = DataManager.INSTANCE.get((Player) event.getEntity());
            damage.getCountDown().resetTime();
            damage.setStatus(PlayerData.Status.FIGHTING);
            entity.getCountDown().resetTime();
            entity.setStatus(PlayerData.Status.FIGHTING);
        }
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
            PitCore.INSTANCE.getPendingBlocks().add(event.getBlock());
            Bukkit.getScheduler().runTaskLater(PitCore.INSTANCE.getPlugin(), () -> {
                event.getBlock().setType(Material.AIR);
            }, 2400);
        } else if (itemStack.getType() == Material.COBBLESTONE) {
            PitCore.INSTANCE.getPendingBlocks().add(event.getBlock());
            Bukkit.getScheduler().runTaskLater(PitCore.INSTANCE.getPlugin(), () -> {
                event.getBlock().setType(Material.AIR);
            }, 1000);
        }
    }
}
