package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.GameState;
import com.random.captureTheFlag.player.Round;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.region.Flag;
import com.random.captureTheFlag.util.Scoreboard;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CaptureListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
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
                                for (Flag flag : Capture.getInstance().getFlags()) {
                                    flag.put();
                                }
                                // CAPTURED ~*~ <--

                                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                    cp.getHandle().sendMessage("§" + flags.getTeam().getColorCode() + flags.getTeam().name()
                                            + "§a Team Flag has been captured by §" + player.getTeam().getColorCode() + ev.getPlayer().getName() + "§a!");
                                    cp.getHandle().teleport(cp.getTeam().getSpawn());
                                    cp.getHandle().getInventory().clear();
                                    cp.setScoreboard(new Scoreboard(cp, true));
                                }
                                if (Capture.getInstance().getRound() != Round.THREE) {
                                    Capture.getInstance().setRound(Capture.getInstance().getRound() == Round.ONE ? Round.TWO : Round.THREE);
                                } else {
                                    for (Location loc : RejectionListeners.getBlocks()) {
                                        loc.getBlock().setType(Material.AIR);
                                    }
                                    Capture.getInstance().getPlayers().clear();
                                    return;
                                }
                                return;
                            }
                        }
                    } else {
                        ev.getPlayer().sendMessage("§cYou cannot take your own flag!");
                        return;
                    }
                }
                if (player.getTeam() != currentFlag.getTeam()) {
                    if (player.getCurrentFlags().length == 0) {
                        currentFlag.setHolder(ev.getPlayer());
                        currentFlag.take(currentFlag, ev.getPlayer());
                        // FLAG TAKEN ~*~ <--
                    }
                }
                return;
            }

            if (ev.getClickedBlock().getType() == Material.STAINED_GLASS) {
                final Flag currentFlag1 = Capture.getInstance().getFlag(ev.getClickedBlock().getLocation().add(0, 1, 0));
                if (currentFlag1 == null) return;
                if (player.getCurrentFlags().length == 0) return;
                if (currentFlag1.getTeam() == player.getTeam()) {
                    currentFlag1.put();
                    ev.getPlayer().getInventory().remove(currentFlag1.toItem());
                    // RETURNED ~*~ <--

                    for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                        cp.getHandle().sendMessage("§" + currentFlag1.getTeam().getColorCode() + currentFlag1.getTeam().name()
                                + "§a Team's Flag has been returned by §" + player.getTeam().getColorCode() + ev.getPlayer().getName() + "§a!");

                    }
                }
            }
        }
    }
}
