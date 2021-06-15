package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.GameState;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.util.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (Capture.getInstance().getPlayers().size() != 8) {
            Capture.getInstance().getPlayers().put(player.getUniqueId(), new CapturePlayer(player.getUniqueId()));
            if (Capture.getInstance().getPlayers().size() == 2 /* Change to 8 once done testing */) {
                Team.RED.setSpawn(Capture.getInstance().getLocation("redSpawn"));
                Team.BLUE.setSpawn(Capture.getInstance().getLocation("blueSpawn"));
                List<CapturePlayer> cps = new ArrayList<>();
                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                    cp.setScoreboard(new Scoreboard(cp, true));
                    cps.add(cp);
                }
                for (int i = 0; i != 1/* Change to 4 once done testing */; i++) {
                    cps.get(i).setTeam(Team.RED);
                }
                for (int i = 1/* Change to 4 once done testing */; i != 2/* Change to 8 once done testing */; i++) {
                    cps.get(i).setTeam(Team.BLUE);
                }
                for (CapturePlayer cp : cps) {
                    cp.getHandle().teleport(cp.getTeam().getSpawn());
                    cp.getHandle().sendMessage("§aGame starting..."); // Game start message
                }
            } else {
                for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                    cp.setScoreboard(new Scoreboard(cp, false));
                }
            }

        } else if (Capture.getInstance().getState() == GameState.INGAME) {
            player.sendMessage(ChatColor.YELLOW + "Game currently in progress, please wait for it to be over.");
        }
        return false;

    }
}
