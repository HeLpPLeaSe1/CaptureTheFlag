package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.GameState;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.region.Flag;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CaptureListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent ev) {
        if (ev.getClickedBlock() == null) return;
        CapturePlayer player = Capture.getInstance().getPlayers().get(ev.getPlayer().getUniqueId());
        if (ev.getClickedBlock().getType() == Material.STANDING_BANNER) {
            final Flag currentFlag = Capture.getInstance().getFlag(ev.getClickedBlock().getLocation());
            if (currentFlag == null || Capture.getInstance().getState() != GameState.INGAME) {
                ev.getPlayer().sendMessage("§cAn error occurred, please report it! (183x0)");
                return;
            }
            if (player.getTeam() == Team.SPECTATOR) return;
            if (player.getTeam() == currentFlag.getTeam()) {
                if (player.hasEnemyFlag()) {
                    for (Flag flags : Capture.getInstance().getFlags()) {
                        if (flags.getTeam() != player.getTeam() && flags.getHolder().getUniqueId() == ev.getPlayer().getUniqueId()) {
                            if (ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand() == flags.toItem()) {
                                flags.setHolder(null);
                                ev.getPlayer().setItemInHand(null);
                                // CAPTURED ~*~ <--

                                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                    cp.getHandle().sendMessage("§" + currentFlag.getTeam().getColorCode() + currentFlag.getTeam().name()
                                            + "§a Team Flag has been captured by §" + player.getTeam().getColorCode() + ev.getPlayer().getName() + "§a!");
                                }
                                return;
                            }
                        }
                    }
                } else {
                    ev.getPlayer().sendMessage("§cYou cannot take your own flag!");
                    return;
                }
            }
            if (player.getTeam() != currentFlag.getTeam()) {

            }
            return;
        }

        if (ev.getClickedBlock().getType() == Material.STAINED_GLASS) {
            final Flag currentFlag1 = Capture.getInstance().getFlag(ev.getClickedBlock().getLocation().add(0 , 1, 0));
            if (currentFlag1 == null) return;
            if (player.getCurrentFlags().length == 0) return;
            if (currentFlag1.getTeam() == player.getTeam() && ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getItemMeta() == currentFlag1.toItem().getItemMeta()) {
                ev.getPlayer().setItemInHand(null);
                currentFlag1.put();
                // RETURNED

                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                    cp.getHandle().sendMessage("§" + currentFlag1.getTeam().getColorCode() + currentFlag1.getTeam().name()
                            + "§a Team Flag has been returned by §" + player.getTeam().getColorCode() + ev.getPlayer().getName() + "§a!");

                }
            }
        }
    }
}
