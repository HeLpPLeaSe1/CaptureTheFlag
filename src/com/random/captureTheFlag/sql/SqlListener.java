package com.random.captureTheFlag.sql;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SqlListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        if (SqlUse.isUserExists(ev.getPlayer().getUniqueId())) {
            SqlUse.updateName(ev.getPlayer().getUniqueId(), ev.getPlayer().getName());
        } else {
            SqlUse.insertUser(ev.getPlayer().getUniqueId(), ev.getPlayer().getName());
        }
    }

}
