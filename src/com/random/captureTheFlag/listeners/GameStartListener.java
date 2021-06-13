package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.util.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class GameStartListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent ev) {
        if (ev.getCurrentItem() == null) return;
        if (ev.getCurrentItem().getItemMeta().getDisplayName().equals(/* With main plugin, there will be inv that holds this item, along with other mini-games' items to join */"")) {
            if (Capture.getInstance().getPlayers().size() != 8) {
                Capture.getInstance().getPlayers().put(ev.getWhoClicked().getUniqueId(), new CapturePlayer(ev.getWhoClicked().getUniqueId()));
                if (Capture.getInstance().getPlayers().size() == 8) {
                    Team.RED.setSpawn(Capture.getInstance().getLocation("redSpawn"));
                    Team.RED.setSpawn(Capture.getInstance().getLocation("blueSpawn"));
                    List<CapturePlayer> cps = new ArrayList<>();
                    for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                        cp.setScoreboard(new Scoreboard(cp, true));
                        cps.add(cp);
                    }
                    for (int i = 0; i != 3; i++) {
                        cps.get(i).setTeam(Team.RED);
                    }
                    for (int i = 4; i != 7; i++) {
                        cps.get(i).setTeam(Team.BLUE);
                    }
                } else {
                    for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                        cp.setScoreboard(new Scoreboard(cp, false));
                    }
                }

            } else {
                ev.getWhoClicked().sendMessage(ChatColor.YELLOW + "Game currently in progress, please wait for it to be over.");
            }

        }

    }

}
