package com.buffsovernexus.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        try {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (lbl.equalsIgnoreCase("spawn")) {
                    Location spawn = Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation();
                    player.teleport(spawn);
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
