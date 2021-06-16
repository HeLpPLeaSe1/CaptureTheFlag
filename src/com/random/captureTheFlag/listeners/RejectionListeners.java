package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.GameState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.ArrayList;
import java.util.List;

public class RejectionListeners implements Listener {
    public List<Location> blocks = new ArrayList<>();

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent ev) {
        if ((Capture.getInstance().getState() != GameState.INGAME || ev.getItemDrop().getItemStack().getType() == Material.STANDING_BANNER) && Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev) {
        if (ev.getPlayer() == null) return;
        if ((ev.getBlock().getType() == Material.STANDING_BANNER || Capture.getInstance().getState() != GameState.INGAME) && Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) {
            ev.setCancelled(true);
            return;
        }

        if (Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId()) && Capture.getInstance().getState() == GameState.INGAME) {
            blocks.add(ev.getBlock().getLocation());
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent ev) {
        if (ev.getPlayer() == null) return;
        if (Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId()) && ev.getBlock().getType() == Material.WOOL) {
            blocks.remove(ev.getBlock().getLocation());
        } else if (Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) {
            ev.setCancelled(true);
        }

    }
}
