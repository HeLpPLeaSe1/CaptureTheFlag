package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.*;
import com.random.captureTheFlag.region.Flag;
import com.random.captureTheFlag.util.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

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
                                Capture.getInstance().setWinner(currentFlag.getTeam(), Capture.getInstance().getRound().getId());
                                // CAPTURED ~*~ <--

                                if (Capture.getInstance().getRound() == Round.TWO) {
                                    Bukkit.getPlayer("random_person159").sendMessage("Round = 2");
                                    if (Capture.getInstance().getWinner(1) == Team.RED && Capture.getInstance().getWinner(2) == Team.RED) {
                                        Capture.getInstance().setRound(Round.THREE);
                                        Capture.getInstance().setWinner(Team.RED, 3);
                                    } else if (Capture.getInstance().getWinner(1) == Team.BLUE && Capture.getInstance().getWinner(2) == Team.BLUE) {
                                        Capture.getInstance().setRound(Round.THREE);
                                        Capture.getInstance().setWinner(Team.RED, 3);
                                    }
                                }

                                if (Capture.getInstance().getRound() == Round.THREE) {
                                    for (Location loc : RejectionListeners.getBlocks()) {
                                        loc.getBlock().setType(Material.AIR);
                                    }
                                    for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                        if ((Capture.getInstance().getWinner(1) == Team.RED && Capture.getInstance().getWinner(3) == Team.RED) || (Capture.getInstance().getWinner(2) == Team.RED && Capture.getInstance().getWinner(3) == Team.RED)) {
                                            cp.getHandle().sendMessage("    §6§lWinner: §4>§4>§4> §c§lRed Team §4<§4<§4<");

                                        } else {
                                            cp.getHandle().sendMessage("    §6§lWinner: §1>§1>§1> §9§lBlue Team §1<§1<§1<");
                                        }
                                        cp.getHandle().teleport(Capture.getInstance().getLocation("spawn"));
                                        cp.getHandle().getInventory().clear();
                                        cp.getHandle().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                                        // GAME END EVENT
                                    }
                                    Capture.getInstance().setRound(Round.ONE);
                                    Capture.getInstance().setWinner(null, 1);
                                    Capture.getInstance().setWinner(null, 2);
                                    Capture.getInstance().setWinner(null, 3);
                                    Capture.getInstance().setState(GameState.LOBBY);
                                    Capture.getInstance().getPlayers().clear();
                                    return;
                                }
                                Capture.getInstance().setRound(Round.getById(Capture.getInstance().getRound().getId() + 1));
                                Capture.getInstance().setWinner(flags.getTeam(), Capture.getInstance().getRound().getId());
                                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                    cp.getHandle().sendMessage("    §" + flags.getTeam().getColorCode() + flags.getTeam().name()
                                            + "§a Team's flag has been captured by §" + player.getTeam().getColorCode() + ev.getPlayer().getName() + "§a!");
                                    cp.getHandle().teleport(cp.getTeam().getSpawn());
                                    cp.getHandle().getInventory().clear();
                                    cp.getHandle().setGameMode(GameMode.SURVIVAL);
                                    cp.getHandle().setHealth(20);
                                    cp.getKit().apply(cp);
                                    cp.setScoreboard(new Scoreboard(cp, true));
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
                final Flag currentFlag = Capture.getInstance().getFlag(ev.getClickedBlock().getLocation().add(0, 1, 0));
                if (currentFlag == null) return;
                if (player.getCurrentFlags().length == 0) return;
                for (Flag flag : Capture.getInstance().getFlags()) {
                    if (flag.getHolder().getUniqueId() == player.getHandle().getUniqueId()) {
                        if (currentFlag.getLocation() == flag.getLocation()) {
                            if (player.getTeam() == currentFlag.getTeam()) {
                                currentFlag.put();
                                ev.getPlayer().getInventory().remove(currentFlag.toItem());
                                // RETURNED ~*~ <--

                                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                    cp.getHandle().sendMessage("    §" + currentFlag.getTeam().getColorCode() + currentFlag.getTeam().name()
                                            + "§a Team's flag has been returned by §" + player.getTeam().getColorCode() + ev.getPlayer().getName() + "§a!");

                                }
                            } else {
                                ev.getPlayer().sendMessage("§cYou cannot return your enemies' flag!");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent ev) {
        if (Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) {
            for (Flag flag : Capture.getInstance().getFlags()) {
                if (flag.getDropped() != null && flag.getDropped().getLocation().distance(ev.getItem().getLocation()) < 5) {
                    flag.setHolder(ev.getPlayer());
                    CapturePlayer capturePlayer = Capture.getInstance().getPlayers().get(ev.getPlayer().getUniqueId());
                    for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                        cp.getHandle().sendMessage("§" + flag.getTeam().getColorCode() + flag.getTeam().name()
                                + "§a Team's Flag has been picked up by §" + capturePlayer.getTeam().getColorCode() + ev.getPlayer().getName() + "§a!");
                    }
                    // FLAG PICKED UP

                }
            }
        }
    }
}
