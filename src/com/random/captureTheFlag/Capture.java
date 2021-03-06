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
import com.random.captureTheFlag.sql.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Capture extends JavaPlugin {
    private static Capture instance;
    private Map<UUID, CapturePlayer> players = new HashMap<>();
    private final Set<Flag> flags = new HashSet<>();
    private GameState state = GameState.LOBBY;
    private Location waitingLoc;
    private Round round = Round.ONE;
    private final List<TopTag> nameTags = new ArrayList<>();
    private final Sql sql = new Sql(new SqlSetting("51.91.58.122", "db_338378", "382e3953d6", "db_338378", 3306));
    private Team winner1;
    private Team winner2;
    private Team winner3;

    public Capture() {
        instance = this;
    }

    @Override
    public void onEnable() {
        try {
            sql.connect();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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
        pm.registerEvents(new SqlListener(), this);

        getCommand("join").setExecutor(new CommandJoin());
        getCommand("ctfsetup").setExecutor(new CommandSetup());
        getCommand("shout").setExecutor(new CommandShout());

        waitingLoc = getLocation("waitingloc");

        for (int i = 1; i < 11; i++) {
            registerNameTag("kills", i);
            registerNameTag("wins", i);
            registerNameTag("captured", i);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for(TopTag tag : nameTags) {
                    updateTag(tag);
                }
            }
        }.runTaskTimer(this, 1, 20 * 15);


    }

    @Override
    public void onDisable() {
        sql.disconnect();

        for (Location loc : RejectionListeners.getBlocks()) {
            loc.getBlock().setType(Material.AIR);
        }
        for (CapturePlayer cp : players.values()) {
            if (cp.getHandle() != null) {
                cp.getHandle().getInventory().clear();
            }
        }

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

    public void setWinner(Team winner, int i) {
        if (i == 1) {
            winner1 = winner;
        } else if (i == 2) {
            winner2 = winner;
        } else {
            winner3 = winner;
        }
    }

    public Team getWinner(int i) {
        if (i == 1) {
            return winner1;
        } else if (i == 2) {
            return winner2;
        } else {
            return winner3;
        }

    }

    private void registerNameTag(String stat, int position) {
        final Location location = getLocation(stat + position);
        if(location != null) {
            final TopTag tag = new TopTag(location, position, stat);
            nameTags.add(tag);
            updateTag(tag);
        }
    }

    private void updateTag(TopTag tag) {
        final String rawPlayer = SqlUse.getPlace(tag.getPosition(), tag.getStat());
        if(rawPlayer != null) {
            final UUID topPlayer = UUID.fromString(rawPlayer);
            tag.update(SqlUse.getName(topPlayer), SqlUse.getStat(topPlayer, tag.getStat()));
        }
    }

    public Sql getSql() {
        return sql;
    }
}
