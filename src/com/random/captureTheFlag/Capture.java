package com.random.captureTheFlag;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Capture extends JavaPlugin {
    private static Capture instance;

    public Capture() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (!(new File("./plugins/CaptureTheFlag/config.yml").exists())) {
            try {
                new File("./plugins/CaptureTheFlag/config.yml").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDisable() {

    }

    public void setLocation(String path, Location loc) {
        FileConfiguration cfg = getConfig();
        cfg.set(path + ".World", loc.getWorld().getName());
        cfg.set(path + ".X", loc.getX());
        cfg.set(path + ".Y", loc.getY());
        cfg.set(path + ".Z", loc.getZ());
        cfg.set(path + ".Yaw", loc.getYaw());
        cfg.set(path + ".Pitch", loc.getPitch());
        try {
            cfg.save(new File("./plugins/CaptureTheFlag/config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation(String path) {
        FileConfiguration cfg = getConfig();
        String world = "world";
        double x = 0;
        double y = 0;
        double z = 0;
        float yaw = 0;
        float pitch = 0;
        if (cfg.contains(path + ".World")) {
            world = cfg.getString(path + ".World");
        }
        if (cfg.contains(path + ".X")) {
            x = cfg.getDouble(path + ".X");
        }
        if (cfg.contains(path + ".Y")) {
            y = cfg.getDouble(path + ".Y");
        }
        if (cfg.contains(path + ".Z")) {
            z = cfg.getDouble(path + ".Z");
        }
        if (cfg.contains(path + ".Yaw")) {
            yaw = cfg.getInt(path + ".Yaw");
        }
        if (cfg.contains(path + ".Pitch")) {
            pitch = cfg.getInt(path + ".Pitch");
        }
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public static Capture getInstance() {
        return instance;
    }
}
