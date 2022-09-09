package com.buffsovernexus.command;

import com.buffsovernexus.utility.CommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (lbl.equalsIgnoreCase("home")) {
                if (args.length == 0) {
                    player.sendMessage("/home [<name>]");
                }
            }

            if (lbl.equalsIgnoreCase("sethome")) {
                player.sendMessage("Not Implemented");
            }

            // Delete a home
            if (lbl.equalsIgnoreCase("delhome")) {
                if (args.length > 0) {
                    String name = CommandUtil.convertArgsToString(args);
                }
            }

            // Go to the player's last death location.
            if (lbl.equalsIgnoreCase("last")) {
                player.sendMessage("Not Implemented.");
            }
        }
        return false;
    }
}
