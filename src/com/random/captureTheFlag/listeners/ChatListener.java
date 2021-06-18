package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        if (Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId()) && Capture.getInstance().getState() == GameState.INGAME) {
            ev.setCancelled(true);
            CapturePlayer player = Capture.getInstance().getPlayers().get(ev.getPlayer().getUniqueId());
            for (CapturePlayer cp : Capture.getInstance().getPlayers().values()) {
                if (cp.getTeam() == player.getTeam()) {
                    cp.getHandle().sendMessage("§7[§" + player.getTeam().getColorCode() + player.getTeam().name().toUpperCase()
                            + "§7]§f " + ev.getPlayer().getName() + "§7: " + ev.getMessage());

                }

            }

        }
    }
}
