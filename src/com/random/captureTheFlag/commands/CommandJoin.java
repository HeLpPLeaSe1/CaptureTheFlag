package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.listeners.GameStartListener;
import com.random.captureTheFlag.player.*;
import com.random.captureTheFlag.util.ItemBuilder;
import com.random.captureTheFlag.util.Scoreboard;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CommandJoin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command!");
            return true;
        }
        Player player = (Player) sender;
        if (Capture.getInstance().getPlayers().containsKey(player.getUniqueId())) return true;
        if (Capture.getInstance().getPlayers().size() != 2 /* Change to 8 once done testing */ && Capture.getInstance().getState() != GameState.INGAME) {
            Capture.getInstance().getPlayers().put(player.getUniqueId(), new CapturePlayer(player.getUniqueId()));
            if (Capture.getInstance().getPlayers().size() == 2 /* Change to 8 once done testing */ && Capture.getInstance().getState() != GameState.INGAME) {
                Capture.getInstance().getPlayers().put(player.getUniqueId(), new CapturePlayer(player.getUniqueId()));
                if (Capture.getInstance().getPlayers().size() == 2 /* Change to 8 once done testing */) {
                    Team.RED.setSpawn(Capture.getInstance().getLocation("redSpawn"));
                    Team.BLUE.setSpawn(Capture.getInstance().getLocation("blueSpawn"));
                    List<CapturePlayer> cps = new ArrayList<>();
                    for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                        cp.setScoreboard(new Scoreboard(cp, true));
                        cps.add(cp);
                    }
                    for (int i = 0; i != 1/* Change to 3 once done testing */; i++) {
                        cps.get(i).setTeam(Team.RED);
                    }
                    for (int i = 1/* Change to 3 once done testing */; i != 2/* Change to 7 once done testing */; i++) {
                        cps.get(i).setTeam(Team.BLUE);
                    }
                    for (CapturePlayer cp : cps) {
                        cp.getHandle().teleport(cp.getTeam().getSpawn());
                        cp.getHandle().sendMessage("Hallo"); // Game start event
                        cp.getHandle().getInventory().addItem(new ItemBuilder().setMaterial(Material.CHEST).setDisplayName("§6Kit Selection").getItem());

                        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.GREEN + "Select Your Kit\"}");

                        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                        PacketPlayOutTitle length = new PacketPlayOutTitle(0, 20, 0);

                        ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(title);
                        ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(length);
                    }
                    new BukkitRunnable() {
                        int timer = 20;

                        @Override
                        public void run() {
                            for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                cp.getHandle().setLevel(timer);
                            }
                            if (timer <= 5 && timer != 0) {
                                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                    IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.YELLOW + "Starting in " + ChatColor.RED + timer + ChatColor.YELLOW + "...\"}");

                                    PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                                    PacketPlayOutTitle length = new PacketPlayOutTitle(0, 20, 0);

                                    ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(title);
                                    ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(length);
                                }
                            }
                            if (timer == 0) {
                                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                                    IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + ChatColor.GREEN + "Capture the Flag\"}");

                                    PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
                                    PacketPlayOutTitle length = new PacketPlayOutTitle(0, 60, 20);

                                    ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(title);
                                    ((CraftPlayer) cp.getHandle()).getHandle().playerConnection.sendPacket(length);
                                    Capture.getInstance().setState(GameState.INGAME);
                                    Capture.getInstance().setRound(Round.ONE);
                                    if (cp.getKit() == null) {
                                        cp.setKit(GameStartListener.getRandomKit(cp.getTeam() == Team.RED ? new ArrayList<>(GameStartListener.redKits.values()) : new ArrayList<>(GameStartListener.blueKits.values())));
                                    } else {
                                        cp.setKit(cp.getTeam() == Team.RED ? GameStartListener.redKits.get(cp.getHandle().getUniqueId()) : GameStartListener.blueKits.get(cp.getHandle().getUniqueId()));
                                    }
                                    cp.getKit().apply(cp);
                                    cp.setScoreboard(new Scoreboard(cp, true));
                                    cp.getHandle().teleport(cp.getTeam().getSpawn());
                                    cp.getHandle().closeInventory();
                                }
                                GameStartListener.redKits.clear();
                                GameStartListener.blueKits.clear();
                                cancel();
                                return;
                            }
                            timer--;

                        }
                    }.runTaskTimer(Capture.getInstance(), 20, 20);
                } else {
                    for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                        cp.setScoreboard(new Scoreboard(cp, false));
                    }
                }

            } else if (Capture.getInstance().getState() == GameState.INGAME) {
                player.sendMessage(ChatColor.YELLOW + "Game currently in progress, please wait for it to be over.");
            }

        }
        return false;
    }
}
