package com.random.captureTheFlag.sql;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.UUID;

public class TopTag {

    private final int position;
    private final String stat;
    private final ArmorStand nameTag;

    public TopTag(Location location, int position, String stat) {
        this.position = position;
        this.stat = stat;
        nameTag = location.getWorld().spawn(location.clone().add(0, 0.375, 0), ArmorStand.class);
        nameTag.setGravity(false);
        nameTag.setVisible(false);
        nameTag.setCustomNameVisible(true);
        nameTag.setCustomName("");
    }

    public void update(String player, int points) {
        UUID uuid = Bukkit.getOfflinePlayer(player).getUniqueId();
        nameTag.setCustomName("§e#§e§l" + position + " "
                + "§7"
                + player + "§e: " + points + " " + stat);
    }

    public void despawn() {
        nameTag.remove();
    }

    public int getPosition() {
        return position;
    }

    public String getStat() {
        return stat;
    }

}
