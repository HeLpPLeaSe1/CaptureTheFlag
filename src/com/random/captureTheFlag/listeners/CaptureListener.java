package com.random.captureTheFlag.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CaptureListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent ev) {
        if (ev.getClickedBlock() == null) return;
        if (ev.getClickedBlock().getType() == Material.BANNER) {
            /* Determine if it's a blue or red banner
             * Determine the player's team
             * Determine if they're holding a flag already
             * Decide whether to capture it or not
             *      If capturing the flag, set the flag holder to the player
             */
            return;
        }

        if (ev.getClickedBlock().getType() == Material.STAINED_GLASS) {
            /* Check to see if the glass is red or blue stained glass
             * Check to see if the player is holding a banner in their hand
             * Check to see if it is the player's team's banner spot
             *      If so, return the flag
             */
        }
    }
}
