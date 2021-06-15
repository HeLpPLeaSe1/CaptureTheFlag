package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Capture;
import com.random.captureTheFlag.player.CapturePlayer;
import com.random.captureTheFlag.player.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShout implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (Capture.getInstance().getPlayers().containsKey(player.getUniqueId()) && Capture.getInstance().getState() == GameState.INGAME) {
            if (args.length == 0) {
                player.sendMessage("§cIncorrect usage!  /shout {message}");
                return true;
            }
            CapturePlayer capturePlayer = Capture.getInstance().getPlayers().get(player.getUniqueId());
            for (CapturePlayer all : Capture.getInstance().getPlayers().values()) {
                all.getHandle().sendMessage("§6[§eSHOUT §8--> §" + capturePlayer.getTeam().getColorCode() + capturePlayer.getTeam().name().toUpperCase() + "§6] §f" + player.getName() + ": " + args);
            }
        }
        return false;
    }
}
