package com.random.captureTheFlag.player;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.region.Flag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CapturePlayer {

    private final UUID uuid;
    private Team team;

    public CapturePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Flag[] getCurrentFlags() {
        final List<Flag> ret = new ArrayList<>();
        for(Flag flag : Capture.getInstance().getFlags()) {
            if(flag.getHolder() != null && flag.getHolder().getUniqueId().equals(uuid)) {
                ret.add(flag);
            }
        }
        return ret.toArray(new Flag[0]);
    }

    public boolean hasEnemyFlag() {
        for(Flag flag : getCurrentFlags()) {
            if(flag.getTeam() == (team == Team.RED ? Team.BLUE : Team.RED)) {
                return true;
            }
        }
        return false;
    }

    public Player getHandle() {
        return Bukkit.getPlayer(uuid);
    }

}
