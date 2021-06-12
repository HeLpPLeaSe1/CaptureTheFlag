package com.random.captureTheFlag.region;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Banner;
import org.bukkit.material.MaterialData;

public class Flag {

    private final Location location;
    private final Team team;
    private final byte id;
    private Player holder;
    private Entity dropped;
    private boolean present;

    public Flag(Location location, Team team, byte id) {
        this.location = location;
        this.team = team;
        this.id = id;
    }

    public Player getHolder() {
        return holder;
    }

    public void setHolder(Player holder) {
        this.holder = holder;
    }

    public void setDropped(Entity dropped) {
        this.dropped = dropped;
    }

    public Entity getDropped() {
        return dropped;
    }

    public Location getLocation() {
        return location;
    }

    public Team getTeam() {
        return team;
    }

    public byte getId() {
        return id;
    }

    public boolean isPresent() {
        return present;
    }

    public void take(Flag flag, Player player) {
        CapturePlayer capturePlayer = Capture.getInstance().getPlayers().get(player.getUniqueId());
        this.holder = player;
        present = false;
        player.getInventory().addItem(toItem());
        location.getBlock().setType(Material.AIR);
        location.clone().subtract(0, 3, 0).getBlock().setType(Material.AIR);
        for(Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage("§7[---------------------§aFlag Taken!§7-------------------]");
            all.sendMessage("                        ");
            all.sendMessage("   §" + capturePlayer.getTeam().getColorCode() + player.getName() + "§7 has taken §"
                    + capturePlayer.getTeam().getOpposingColor() + capturePlayer.getTeam().getOpposingTeam().toUpperCase() + "§7 team's flag!");
            all.sendMessage("");
            all.sendMessage("§7[---------------------------------------------------]");
            all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 7, 1);
        }
    }

    public void put() {
        holder = null;
        present = true;
        location.clone().subtract(0, 3, 0).getBlock().setType(Material.DIAMOND_BLOCK);
        location.getBlock().setType(getMaterial());
    }

    public Material getMaterial() {
        return Material.STANDING_BANNER; // Return the correct colored banner for the team instead of a blank banner
    }

    public ItemStack toItem() {
        return new ItemBuilder().setDisplayName("§" + team.getColorCode()
                + team.name().toLowerCase().replace("r", "R").replace("b", "B") + ChatColor.GRAY + " Flag")
                .setMaterial(getMaterial()).getItem();
    }

}
