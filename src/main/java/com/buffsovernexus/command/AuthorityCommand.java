package com.buffsovernexus.command;

import com.buffsovernexus.service.AuthorityPlayerService;
import com.buffsovernexus.utility.CommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AuthorityCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        // Ensure that only a person in the server can activate commands
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (lbl.equalsIgnoreCase("authority")) {
                if (args.length == 0) {
                    player.sendMessage("Authority v1");
                    player.sendMessage("/authority lookup <name> - Grabs the player's data");
                }
                else if (args.length == 1) {
                    player.sendMessage("Not implemented.");
                }
                else if (args.length == 2) {
                    String firstArg = args[0].toLowerCase();
                    if (firstArg.equalsIgnoreCase("lookup")) {
                        String name = CommandUtil.convertArgsToString(1, args);
                        AuthorityPlayerService.getPlayerLookup(player, name);
                    }
                }
            }

        }
        return false;
    }
}
