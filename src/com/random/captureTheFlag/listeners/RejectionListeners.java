package com.random.captureTheFlag.listeners;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.GameState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class RejectionListeners implements Listener {
    public static List<Location> blocks = new ArrayList<>();

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent ev) {
        if ((Capture.getInstance().getState() != GameState.INGAME || ev.getItemDrop().getItemStack().getType() == Material.BANNER) && Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev) {
        if (ev.getPlayer() == null) return;
        if (!(Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId()))) return;
        if ((ev.getBlock().getType() == Material.STANDING_BANNER || ev.getBlock().getType() == Material.WALL_BANNER) && Capture.getInstance().getPlayers().containsKey(ev.getPlayer().getUniqueId())) {
            ev.setCancelled(true);
            return;
        }
        if (ev.getBlock().getLocation().getX() <= -24 || ev.getBlock().getLocation().getY() < 45 || ev.getBlock().getLocation().getY() > 66 || ev.getBlock().getLocation().getX() >= 76) {
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

    @EventHandler
    public void onClick(InventoryClickEvent ev) {
        if (Capture.getInstance().getPlayers().containsKey(ev.getWhoClicked().getUniqueId())) {
            if (ev.getCurrentItem() == null) return;
            if (ev.getCurrentItem().getType().toString().contains("LEATHER") || ev.getCurrentItem().getType().toString().contains("IRON")) {
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent ev) {
        if (ev.getLocation().getWorld().getName().equals("ctf")) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent ev) {
        if (Capture.getInstance().getPlayers().containsKey(ev.getEntity().getUniqueId())) {
            if (Capture.getInstance().getState() != GameState.INGAME) {
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent ev) {
        ev.getEntity().spigot().respawn();

    }

    public static List<Location> getBlocks() {
        return blocks;
    }
}
