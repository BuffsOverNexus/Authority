package com.buffsovernexus.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        try {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (lbl.equalsIgnoreCase("cclear")) {
                    for (int i = 0; i < 200; i++) {
                        player.sendMessage(" ");
                    }
                    player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Your chat has been cleared.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
