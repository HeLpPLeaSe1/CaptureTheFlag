package com.random.captureTheFlag.commands;

import com.random.captureTheFlag.Capture;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetup implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.isOp()) return true;
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Incorrect usage!  /setcaptureloc <loc>");
            return true;
        }
        Capture.getInstance().setLocation(args[0], player.getLocation());
        return false;
    }
}
