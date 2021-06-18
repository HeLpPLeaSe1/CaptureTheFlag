package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.GameState;
import com.random.captureTheFlag.region.Flag;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDamageDeath(PlayerDeathEvent ev) {
        if (ev.getEntity() == null) return;
        if (Capture.getInstance().getPlayers().containsKey(ev.getEntity().getUniqueId())) {
            Player p = ev.getEntity();
            ev.setDeathMessage(null);
            // Reset
            p.spigot().respawn();
            p.getInventory().clear();
            p.setHealth(20);
            p.setGameMode(GameMode.SPECTATOR);
            CapturePlayer player = Capture.getInstance().getPlayers().get(ev.getEntity().getUniqueId());
            p.teleport(Capture.getInstance().getWaitingLoc());
            new BukkitRunnable() {
                int i = 5;
                final int r = Capture.getInstance().getRound().getId();
                @Override
                public void run() {
                    if (player.getHandle() == null) {
                        cancel();
                        return;
                    }
                    if (Capture.getInstance().getRound().getId() != r) {
                        if (Capture.getInstance().getState() != GameState.END) {
                            p.teleport(player.getTeam().getSpawn());
                        }
                        cancel();
                        return;
                    }
                    if (i >= 1) {
                        p.playSound(p.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.YELLOW + "Respawning in " + ChatColor.RED + i + ChatColor.YELLOW + "...\"}");

                        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                        PacketPlayOutTitle length = new PacketPlayOutTitle(0, 20, 0);

                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
                    } else {
                        p.playSound(p.getLocation(), Sound.NOTE_SNARE_DRUM, 1, 1);
                        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.GREEN + "Respawning...\"}");

                        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                        PacketPlayOutTitle length = new PacketPlayOutTitle(0, 20, 10);

                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);

                        // RESPAWNING EVENT
                        player.getKit().apply(player);
                        p.teleport(player.getTeam().getSpawn());
                        p.setGameMode(GameMode.SURVIVAL);
                        p.setSaturation(20);


                        cancel();
                        return;
                    }
                    i--;

                }
            }.runTaskTimer(Capture.getInstance(), 20, 20);

            // Death message check
            if (player.getKiller() == null) {
                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                    cp.getHandle().sendMessage("§" + player.getTeam().getColorCode() + ev.getEntity().getName() + " §7died.");
                }
            } else {
                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                    cp.getHandle().sendMessage("§" + player.getTeam().getColorCode() + player.getHandle().getName()
                            + " §7was killed by §" + player.getKiller().getTeam().getColorCode() + player.getKiller().getHandle().getName() + "§7.");
                }
            }

            // Flag check
            if (player.getCurrentFlags().length != 0) {
                for (Flag flag : Capture.getInstance().getFlags()) {
                    if (flag.getHolder() == ev.getEntity()) {
                        if (player.getKiller() == null) {
                            flag.setDropped(ev.getEntity().getWorld().dropItem(Capture.getInstance().getLocation("flagDrop"), flag.toItem()));
                            // FLAG DROPPED
                            for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                cp.getHandle().sendMessage("§" + player.getTeam().getColorCode() + player.getHandle().getName()
                                        + " §adropped §" + flag.getTeam().getColorCode() + flag.getTeam().name().toUpperCase()
                                        + " §aTeam's flag!"
                                );
                            }
                        } else {
                            flag.setHolder(player.getKiller().getHandle());
                            player.getKiller().getHandle().getInventory().addItem(flag.toItem());
                            // FLAG GIVEN

                            for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                cp.getHandle().sendMessage("§" + player.getTeam().getColorCode() + player.getHandle().getName()
                                        + " §agave §" + flag.getTeam().getColorCode() + flag.getTeam().name().toUpperCase()
                                        + " §aTeam's flag to §" + player.getKiller().getTeam().getColorCode() + player.getKiller().getHandle().getName() + "§a!"
                                );
                            }

                        }

                    }

                }

            }

        }

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent ev) {
        if (!(ev.getEntity() instanceof Player || ev.getDamager() instanceof Player)) return;
        if (Capture.getInstance().getPlayers().containsKey(ev.getEntity().getUniqueId())) {
            Capture.getInstance().getPlayers().get(ev.getEntity().getUniqueId()).setKiller(Capture.getInstance().getPlayers().get(ev.getDamager().getUniqueId()));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Bukkit.getPlayer(ev.getEntity().getUniqueId()) != null && Capture.getInstance().getPlayers().get(ev.getEntity().getUniqueId()).getKiller() != Capture.getInstance().getPlayers().get(ev.getDamager().getUniqueId())) {
                        Capture.getInstance().getPlayers().get(ev.getEntity().getUniqueId()).setKiller(null);
                    }
                }
            }.runTaskLater(Capture.getInstance(), 100);

        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent ev) {
        if (Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) {
            // Kill them from void death
            if (ev.getPlayer().getLocation().getY() <= 5) {
                ev.getPlayer().teleport(Capture.getInstance().getWaitingLoc());
            }
        }

    }
}
