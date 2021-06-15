package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        if (Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) {
            ev.getPlayer().teleport(Capture.getInstance().getLocation("waitloc"));
        }
    }
}
