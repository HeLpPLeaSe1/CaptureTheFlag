package com.random.captureTheFlag.region;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class Flag {

    private final Location location;
    private final Team team;
    private Player holder;
    private Entity dropped;
    private boolean present;

    public Flag(Location location, Team team) {
        this.location = location;
        this.team = team;
    }

    public Player getHolder() {
        return holder;
    }

    public void setHolder(Player holder) {
        this.holder = holder;
    }

    public void setDropped(Entity dropped) {
        this.dropped = dropped;
        this.holder = null;
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
        for (CapturePlayer all : Capture.getInstance().getPlayers().values()) {
            all.getHandle().sendMessage("§7[---------------------§aFlag Taken!§7-------------------]");
            all.getHandle().sendMessage("                        ");
            all.getHandle().sendMessage("   §" + capturePlayer.getTeam().getColorCode() + player.getName() + "§7 has taken §"
                    + flag.getTeam().getColorCode() + flag.getTeam().name().toUpperCase() + "§7 team's flag!");
            all.getHandle().sendMessage("");
            all.getHandle().sendMessage("§7[---------------------------------------------------]");
            all.getHandle().playSound(all.getHandle().getLocation(), Sound.ENDERDRAGON_GROWL, 7, 1);
        }
    }

    public void put() {
        holder = null;
        present = true;
        location.clone().subtract(0, 3, 0).getBlock().setType(Material.DIAMOND_BLOCK);
        location.getBlock().setType(Material.STANDING_BANNER);
        Banner banner = (Banner) location.getBlock().getState();
        banner.setBaseColor(getColor());
        banner.update();
    }

    public DyeColor getColor() {
        return DyeColor.valueOf(team.name().toUpperCase());
    }

    public ItemStack toItem() {
        ItemStack banner = new ItemStack(Material.BANNER, 1, getColor().getDyeData());
        ItemMeta meta = banner.getItemMeta();
        meta.setDisplayName("§" + team.getColorCode() + team.name() + "§7 Team's Flag");
        banner.setItemMeta(meta);
        return banner;
    }

}
