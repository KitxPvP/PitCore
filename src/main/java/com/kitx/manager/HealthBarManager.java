package com.kitx.manager;

import com.kitx.PitCorePlugin;
import com.kitx.utils.ColorUtil;
import com.kitx.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//This is taken from a plugin :/
public class HealthBarManager implements Listener {

    private final String message;
    private final boolean perm;
    private final boolean showMob;
    private final boolean showPlayer;
    private final boolean dealy;
    private final boolean checkPvP;
    private final boolean limitHealth;
    private final boolean stripName;
    private final String filledHeartIcon;
    private final String halfHeartIcon;
    private final String emptyHeartIcon;
    private final List<String> list = new ArrayList<>();
    private final List<String> worlds;
    private final HashMap<String, String> map = new HashMap<>();
    private String nmsver;
    private boolean useOldMethods = false;
    private final Plugin plugin;

    public HealthBarManager(PitCorePlugin plugin) {
        this.plugin = plugin;
        this.nmsver = Bukkit.getServer().getClass().getPackage().getName();
        this.nmsver = this.nmsver.substring(this.nmsver.lastIndexOf(".") + 1);
        if (this.nmsver.equalsIgnoreCase("v1_8_R1") || this.nmsver.equalsIgnoreCase("v1_7_")) {
            this.useOldMethods = true;
        }

        this.message = plugin.getConfig().getString("Health Message");
        this.perm = plugin.getConfig().getBoolean("Use Permissions");
        this.showMob = plugin.getConfig().getBoolean("Show Mob");
        this.showPlayer = plugin.getConfig().getBoolean("Show Player");
        this.dealy = plugin.getConfig().getBoolean("Delay Message");
        this.checkPvP = plugin.getConfig().getBoolean("Region PvP");
        this.limitHealth = plugin.getConfig().getBoolean("Limit Health");
        this.stripName = plugin.getConfig().getBoolean("Strip Name");
        this.filledHeartIcon = plugin.getConfig().getString("Full Health Icon");
        this.halfHeartIcon = plugin.getConfig().getString("Half Health Icon");
        this.emptyHeartIcon = plugin.getConfig().getString("Empty Health Icon");
        if (plugin.getConfig().getBoolean("Name Change")) {
            for (String s : plugin.getConfig().getStringList("Name")) {
                String[] split = s.split(" = ");
                this.map.put(split[0], split[1]);
            }
        }

        this.worlds = plugin.getConfig().getStringList("Disabled worlds");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }


    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onBow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile) {
            Projectile object = (Projectile)event.getDamager();
            if (object.getShooter() instanceof Player) {
                Player player = (Player)object.getShooter();
                if (!this.checkPvP || !event.isCancelled()) {
                    if (!this.worlds.contains(player.getWorld().getName())) {
                        if (!this.list.contains(player.getName())) {
                            if (!this.perm || player.hasPermission("ActionHealth.Health")) {
                                if (event.getEntity() instanceof Player) {
                                    Player entity = (Player)event.getEntity();
                                    if (this.showPlayer) {
                                        if (!this.dealy) {
                                            this.sendHealth1(player, entity, entity.getName(), entity.getHealth() - event.getFinalDamage());
                                        }

                                        if (this.dealy) {
                                            this.sendHealthD1(player, entity, entity.getName());
                                        }

                                    }
                                } else if (this.showMob) {
                                    if (event.getEntity() instanceof LivingEntity) {
                                        if (!(event.getEntity() instanceof ArmorStand)) {
                                            LivingEntity entity = (LivingEntity)event.getEntity();
                                            if (!this.dealy) {
                                                this.sendHealth2(player, entity, entity.getHealth() - event.getFinalDamage());
                                            }

                                            if (this.dealy) {
                                                this.sendHealthD2(player, entity);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (event.getEntity() instanceof LivingEntity) {
                Player player = (Player)event.getDamager();
                if (!this.checkPvP || !event.isCancelled()) {
                    if (!this.worlds.contains(player.getWorld().getName())) {
                        if (!this.list.contains(player.getName())) {
                            if (!this.perm || player.hasPermission("ActionHealth.Health")) {
                                if (event.getEntity() instanceof Player) {
                                    Player entity = (Player)event.getEntity();
                                    if (this.showPlayer) {
                                        if (!this.dealy) {
                                            this.sendHealth1(player, entity, entity.getName(), entity.getHealth() - event.getFinalDamage());
                                        }

                                        if (this.dealy) {
                                            this.sendHealthD1(player, entity, entity.getName());
                                        }

                                    }
                                } else if (this.showMob) {
                                    if (event.getEntity() instanceof LivingEntity) {
                                        if (!(event.getEntity() instanceof ArmorStand)) {
                                            LivingEntity entity = (LivingEntity)event.getEntity();
                                            if (!this.dealy) {
                                                this.sendHealth2(player, entity, entity.getHealth() - event.getFinalDamage());
                                            }

                                            if (this.dealy) {
                                                this.sendHealthD2(player, entity);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void sendActionBar(Player player, String message) {
        String version = Reflection.getVersion();
        try {
            Object chat = Reflection.getClass(version, "net.minecraft.server", "ChatComponentText").getConstructor(new Class<?>[]{String.class}).newInstance(ColorUtil.translate(message));
            Object packet = Reflection.getClass(version, "net.minecraft.server", "PacketPlayOutChat").getConstructor(Reflection.getClass(version, "net.minecraft.server", "IChatBaseComponent"), byte.class).newInstance(chat, (byte) 2);
            Reflection.sendPacket(version, player, packet, "net.minecraft.server");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHealth1(Player player, Player entity, String name, double health) {
        String messagesend = this.message;
        if (this.stripName) {
            name = ChatColor.stripColor(name);
        }

        if (this.map.containsKey(name)) {
            name = this.map.get(name);
        }

        if (Math.round(health) <= 0L) {
            messagesend = messagesend.replace("{name}", name).replace("{health}", String.valueOf(0)).replace("{maxhealth}", String.valueOf(Math.round(entity.getMaxHealth()))).replace("{damager_health}", String.valueOf(Math.round(player.getHealth()))).replace("{damager_max}", String.valueOf(Math.round(player.getMaxHealth())));
            this.sendActionBar(player, messagesend.replace("{usestyle}", this.getIconHealthString((int)Math.round(health), (int)Math.round(entity.getMaxHealth()))));
        } else {
            messagesend = messagesend.replace("{name}", name).replace("{health}", String.valueOf(Math.round(health))).replace("{maxhealth}", String.valueOf(Math.round(entity.getMaxHealth()))).replace("{damager_health}", String.valueOf(Math.round(player.getHealth()))).replace("{damager_max}", String.valueOf(Math.round(player.getMaxHealth())));
            this.sendActionBar(player, messagesend.replace("{usestyle}", this.getIconHealthString((int)Math.round(health), (int)Math.round(entity.getMaxHealth()))));
        }
    }

    public void sendHealth2(Player player, LivingEntity entity, double health) {
        String name = null;
        String messagesend = this.message;
        if (entity.getCustomName() == null) {
            if (this.map.containsKey(entity.getType().getName())) {
                name = this.map.get(entity.getType().getName());
            } else {
                name = entity.getType().getName();
            }
        }

        if (entity.getCustomName() != null) {
            if (this.map.containsKey(ChatColor.stripColor(entity.getCustomName()))) {
                name = this.map.get(ChatColor.stripColor(entity.getCustomName()));
            } else {
                name = ChatColor.stripColor(entity.getCustomName());
            }
        }

        if (Math.round(health) <= 0L) {
            messagesend = messagesend.replace("{name}", name).replace("{health}", String.valueOf(0)).replace("{maxhealth}", String.valueOf(Math.round(entity.getMaxHealth()))).replace("{damager_health}", String.valueOf(Math.round(player.getHealth()))).replace("{damager_max}", String.valueOf(Math.round(player.getMaxHealth())));
            this.sendActionBar(player, messagesend.replace("{usestyle}", this.getIconHealthString((int)Math.round(health), (int)Math.round(entity.getMaxHealth()))));
        } else {
            messagesend = messagesend.replace("{name}", name).replace("{health}", String.valueOf(Math.round(health))).replace("{maxhealth}", String.valueOf(Math.round(entity.getMaxHealth()))).replace("{damager_health}", String.valueOf(Math.round(player.getHealth()))).replace("{damager_max}", String.valueOf(Math.round(player.getMaxHealth())));
            this.sendActionBar(player, messagesend.replace("{usestyle}", this.getIconHealthString((int)Math.round(health), (int)Math.round(entity.getMaxHealth()))));
        }
    }

    public void sendHealthD1(final Player player, final Player entity, final String name) {
        (new BukkitRunnable() {
            public void run() {
                String messagesend = message;
                String tempName = name;
                if (stripName) {
                    tempName = ChatColor.stripColor(tempName);
                }

                if (map.containsKey(tempName)) {
                    tempName = map.get(tempName);
                }

                double health = entity.getHealth();
                if (Math.round(health) <= 0L) {
                    messagesend = messagesend.replace("{name}", tempName).replace("{health}", String.valueOf(0)).replace("{maxhealth}", String.valueOf(Math.round(entity.getMaxHealth()))).replace("{damager_health}", String.valueOf(Math.round(player.getHealth()))).replace("{damager_max}", String.valueOf(Math.round(player.getMaxHealth())));
                    sendActionBar(player, messagesend.replace("{usestyle}", getIconHealthString((int)Math.round(health), (int)Math.round(entity.getMaxHealth()))));
                } else {
                    messagesend = messagesend.replace("{name}", tempName).replace("{health}", String.valueOf(Math.round(health))).replace("{maxhealth}", String.valueOf(Math.round(entity.getMaxHealth()))).replace("{damager_health}", String.valueOf(Math.round(player.getHealth()))).replace("{damager_max}", String.valueOf(Math.round(player.getMaxHealth())));
                    sendActionBar(player, messagesend.replace("{usestyle}", getIconHealthString((int)Math.round(health), (int)Math.round(entity.getMaxHealth()))));
                }
            }
        }).runTaskLater(plugin, 1L);
    }

    public void sendHealthD2(final Player player, final LivingEntity entity) {
        (new BukkitRunnable() {
            public void run() {
                String name = null;
                String messagesend = message;
                double health = entity.getHealth();
                if (entity.getCustomName() == null) {
                    if (map.containsKey(entity.getType().getName())) {
                        name = map.get(entity.getType().getName());
                    } else {
                        name = entity.getType().getName();
                    }
                }

                if (entity.getCustomName() != null) {
                    if (map.containsKey(ChatColor.stripColor(entity.getCustomName()))) {
                        name = map.get(ChatColor.stripColor(entity.getCustomName()));
                    } else {
                        name = ChatColor.stripColor(entity.getCustomName());
                    }
                }

                if (Math.round(health) <= 0L) {
                    assert name != null;
                    messagesend = messagesend.replace("{name}", name).replace("{health}", String.valueOf(0)).replace("{maxhealth}", String.valueOf(Math.round(entity.getMaxHealth()))).replace("{damager_health}", String.valueOf(Math.round(player.getHealth()))).replace("{damager_max}", String.valueOf(Math.round(player.getMaxHealth())));
                    sendActionBar(player, messagesend.replace("{usestyle}", getIconHealthString((int)Math.round(health), (int)Math.round(entity.getMaxHealth()))));
                } else {
                    assert name != null;
                    messagesend = messagesend.replace("{name}", name).replace("{health}", String.valueOf(Math.round(health))).replace("{maxhealth}", String.valueOf(Math.round(entity.getMaxHealth()))).replace("{damager_health}", String.valueOf(Math.round(player.getHealth()))).replace("{damager_max}", String.valueOf(Math.round(player.getMaxHealth())));
                    sendActionBar(player, messagesend.replace("{usestyle}", getIconHealthString((int)Math.round(health), (int)Math.round(entity.getMaxHealth()))));
                }
            }
        }).runTaskLater(plugin, 1L);
    }

    private String getIconHealthString(int health, int maxHealth) {
        StringBuilder filled = new StringBuilder();
        int i;
        if (this.limitHealth) {
            if (maxHealth == health) {
                for(i = 0; i < 10; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                return filled.toString();
            } else if ((double)health >= 0.9D * (double)maxHealth && health <= maxHealth) {
                //System.out.println((0.9D * (double)maxHealth + (double)maxHealth) / 2.0D);

                for(i = 0; i < 9; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                filled.append(this.emptyHeartIcon);
                return filled.toString();
            } else if ((double)health >= 0.8D * (double)maxHealth && (double)health <= 0.9D * (double)maxHealth) {
                for(i = 0; i < 8; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                for(i = 0; i < 2; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else if ((double)health >= 0.7D * (double)maxHealth && (double)health <= 0.8D * (double)maxHealth) {
                for(i = 0; i < 7; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                for(i = 0; i < 3; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else if ((double)health >= 0.6D * (double)maxHealth && (double)health <= 0.7D * (double)maxHealth) {
                for(i = 0; i < 6; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                for(i = 0; i < 4; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else if ((double)health >= 0.5D * (double)maxHealth && (double)health <= 0.6D * (double)maxHealth) {
                for(i = 0; i < 5; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                for(i = 0; i < 5; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else if ((double)health >= 0.4D * (double)maxHealth && (double)health <= 0.5D * (double)maxHealth) {
                for(i = 0; i < 4; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                for(i = 0; i < 6; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else if ((double)health >= 0.3D * (double)maxHealth && (double)health <= 0.4D * (double)maxHealth) {
                for(i = 0; i < 3; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                for(i = 0; i < 7; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else if ((double)health >= 0.2D * (double)maxHealth && (double)health <= 0.3D * (double)maxHealth) {
                for(i = 0; i < 2; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                for(i = 0; i < 8; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else if ((double)health >= 0.1D * (double)maxHealth && (double)health <= 0.2D * (double)maxHealth) {
                filled.append(this.filledHeartIcon);

                for(i = 0; i < 9; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else if (health > 0 && (double)health <= 0.1D * (double)maxHealth) {
                filled.append(this.filledHeartIcon);

                for(i = 0; i < 9; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else if (health <= 0) {
                for(i = 0; i < 10; ++i) {
                    filled.append(this.emptyHeartIcon);
                }

                return filled.toString();
            } else {
                for(i = 0; i < 10; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                return filled.toString();
            }
        } else {
            if (health > maxHealth) {
                health = maxHealth;
            }

            if (health % 2 != 0) {
                for(i = 0; i < health / 2; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                filled.append(this.halfHeartIcon);
                if (health + 1 < maxHealth) {
                    for(i = 0; i < (maxHealth - (health + 1)) / 2; ++i) {
                        filled.append(this.emptyHeartIcon);
                    }
                }
            } else {
                for(i = 0; i < health / 2; ++i) {
                    filled.append(this.filledHeartIcon);
                }

                if (health < maxHealth) {
                    for(i = 0; i < (maxHealth - health) / 2; ++i) {
                        filled.append(this.emptyHeartIcon);
                    }
                }
            }

            return filled.toString();
        }
    }


}
