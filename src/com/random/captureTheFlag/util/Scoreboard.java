package com.random.captureTheFlag.util;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard {
    private Team team;

    public Scoreboard(CapturePlayer player, boolean ingame) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("dummy", "test");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        if (ingame) {
            obj.getScore(ChatColor.YELLOW + "Capture your enemy's flags!").setScore(10);
            obj.getScore(ChatColor.DARK_GRAY + "[------------------------]").setScore(9);
            obj.getScore(ChatColor.GOLD + "Round " + Capture.getInstance().getRound().getId() + "/3");
            obj.getScore(" ").setScore(8);
            if (player.getTeam() == Team.RED) {
                obj.getScore(ChatColor.RED + "Red Flag: " + ChatColor.GREEN + "✓" + ChatColor.GRAY + " YOU").setScore(7);
                obj.getScore(ChatColor.BLUE + "Blue Flag: " + ChatColor.GREEN + "✓").setScore(6);
            } else {
                obj.getScore(ChatColor.RED + "Red Flag: " + ChatColor.GREEN + "✓").setScore(7);
                obj.getScore(ChatColor.BLUE + "Blue Flag: " + ChatColor.GREEN + "✓" + ChatColor.GRAY + " YOU").setScore(6);
            }
            obj.getScore("  ").setScore(5);
            obj.getScore(ChatColor.WHITE + "Kills: " + 0).setScore(4);
            obj.getScore(ChatColor.WHITE + "Flags Taken: " + 0).setScore(3);
            obj.getScore(ChatColor.WHITE + "Flags Captured: " + 0).setScore(2);
            obj.getScore("   ").setScore(1);
            obj.getScore(ChatColor.DARK_GRAY + "[------------------------]").setScore(0);

            if (player.getTeam() == Team.RED) {
                org.bukkit.scoreboard.Team red = board.registerNewTeam("red");
                red.setAllowFriendlyFire(false);
                red.setCanSeeFriendlyInvisibles(true);
                red.setPrefix("§c");
                red.addEntry(player.getHandle().getName());
            } else {
                org.bukkit.scoreboard.Team blue = board.registerNewTeam("blue");
                blue.setAllowFriendlyFire(false);
                blue.setCanSeeFriendlyInvisibles(true);
                blue.setPrefix("§9");
                blue.addEntry(player.getHandle().getName());
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!(Capture.getInstance().getPlayers().containsKey(players.getUniqueId()))) {
                    players.hidePlayer(player.getHandle());
                }
            }
        } else {
            obj.getScore(ChatColor.DARK_GRAY + "[------------------------]").setScore(5);
            obj.getScore(" ").setScore(4);
            obj.getScore(ChatColor.DARK_GRAY + "Waiting for more players...").setScore(3);
            obj.getScore(ChatColor.YELLOW + "" + Capture.getInstance().getPlayers().size() + ChatColor.GRAY + "/" + ChatColor.YELLOW + "8" + ChatColor.GRAY + " Players...").setScore(2);
            obj.getScore("  ").setScore(1);
            obj.getScore(ChatColor.DARK_GRAY + "[------------------------]").setScore(0);

        }

    }


}
