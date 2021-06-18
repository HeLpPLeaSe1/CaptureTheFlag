package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandLeave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (Capture.getInstance().getPlayers().containsKey(player.getUniqueId())) {
            if (Capture.getInstance().getState() == GameState.INGAME) {
                Capture.getInstance().getPlayers().remove(player.getUniqueId());
                player.sendMessage("§eYou have been removed from the Capture the Flag game!");
                player.teleport(Capture.getInstance().getLocation("spawn"));
            }
        } else {
            player.sendMessage("§cYou are not currently in a game!");
        }
        return false;
    }
}
