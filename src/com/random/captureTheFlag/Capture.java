package com.random.captureTheFlag;

import com.random.captureTheFlag.commands.CommandJoin;
import com.random.captureTheFlag.commands.CommandSetup;
import com.random.captureTheFlag.commands.CommandShout;
import com.random.captureTheFlag.listeners.*;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.GameState;
import com.random.captureTheFlag.player.Round;
import com.random.captureTheFlag.player.Team;
import com.random.captureTheFlag.region.Flag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Capture extends JavaPlugin {
    private static Capture instance;
    private Map<UUID, CapturePlayer> players = new HashMap<>();
    private final Set<Flag> flags = new HashSet<>();
    private GameState state = GameState.LOBBY;
    private Location waitingLoc;
    private Round round = Round.ONE;

    public Capture() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        if (!(new File("./plugins/CaptureTheFlag/config.yml").exists())) {
            try {
                new File("./plugins/CaptureTheFlag/config.yml").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        registerFlag(Team.RED);
        registerFlag(Team.BLUE);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CaptureListener(), this);
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new DeathListener(), this);
        pm.registerEvents(new GameStartListener(), this);
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new RejectionListeners(), this);

        getCommand("join").setExecutor(new CommandJoin());
        getCommand("ctfsetup").setExecutor(new CommandSetup());
        getCommand("shout").setExecutor(new CommandShout());


    }

    @Override
    public void onDisable() {

    }

    public Flag getFlag(Location loc) {
        for (Flag flag : flags) {
            if(flag.getLocation().distance(loc) < 3) {
                return flag;
            }
        }
        return null;
    }

    private void registerFlag(Team team) {
        final Location flagLoc = getLocation("flag" + team.name().toLowerCase());
        if (flagLoc != null) {
            final Flag flag = new Flag(flagLoc, team);
            flags.add(flag);
            flag.put();
        }
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
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("./plugins/CaptureTheFlag/config.yml"));
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

    public Map<UUID, CapturePlayer> getPlayers() {
        return players;
    }

    public Set<Flag> getFlags() {
        return flags;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Round getRound() {
        return round;
    }

    public Location getWaitingLoc() {
        return waitingLoc;
    }

    public static Capture getInstance() {
        return instance;
    }
}
