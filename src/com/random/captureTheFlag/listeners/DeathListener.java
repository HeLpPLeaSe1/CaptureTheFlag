package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.region.Flag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDamageDeath(PlayerDeathEvent ev) {
        if (Capture.getInstance().getPlayers().containsKey(ev.getEntity().getUniqueId())) {
            ev.getEntity().getInventory().clear();
            CapturePlayer player = Capture.getInstance().getPlayers().get(ev.getEntity().getUniqueId());
            if (player.getKiller() == null) {
                ev.setDeathMessage("§" + player.getTeam().getColorCode() + ev.getEntity().getName() + " §7died.");
            } else {
                ev.setDeathMessage("§" + player.getTeam().getColorCode() + player.getHandle().getName()
                        + " §7was knocked into the void by §" + player.getKiller().getTeam().getColorCode() + player.getKiller().getHandle().getName() + "§7.");
            }

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
}
