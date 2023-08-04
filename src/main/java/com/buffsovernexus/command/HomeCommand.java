package com.buffsovernexus.command;

import com.buffsovernexus.entity.AuthorityHome;
import com.buffsovernexus.entity.AuthorityHomeInvite;
import com.buffsovernexus.entity.AuthorityPlayer;
import com.buffsovernexus.factory.AuthorityPlayerFactory;
import com.buffsovernexus.utility.BooleanUtil;
import com.buffsovernexus.utility.CommandUtil;
import com.buffsovernexus.utility.GeneralUtil;
import com.buffsovernexus.utility.HibernateUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        try {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (lbl.equalsIgnoreCase("ahome")) {
                    if (args.length == 0) {
                        player.sendMessage("-- Authority Homes --");
                        player.sendMessage(ChatColor.ITALIC + "/ahome <name>" + ChatColor.RESET + " - Teleport to your home with the <name>");
                        player.sendMessage(ChatColor.ITALIC + "/ahomes" + ChatColor.RESET + " - Retrieves all of your homes");
                        player.sendMessage(ChatColor.ITALIC + "/shome <name>" + ChatColor.RESET + " - Set a home.");
                        player.sendMessage(ChatColor.ITALIC + "/dhome <name>" + ChatColor.RESET + " - Delete a home.");
                    } else {
                        String name = CommandUtil.convertArgsToString(args).toLowerCase();
                        Session session = HibernateUtil.sessionFactory.openSession();
                        AuthorityPlayer authorityPlayer = AuthorityPlayerFactory.getPlayerByPlayer(session, player);
                        if (null != authorityPlayer.getHomes()) {
                            List<AuthorityHome> matchedHomes = authorityPlayer.getHomes().stream().filter(home -> home.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
                            if (matchedHomes.isEmpty()) {
                                player.sendMessage(ChatColor.RED + String.format("The home, %s, was not found.", name));
                            } else {
                                matchedHomes.forEach(home -> {
                                    player.teleport(home.toLocation());
                                    player.sendMessage(String.format("Teleported to home (" + ChatColor.BOLD + "%s" + ChatColor.RESET + ")", home.getName().toLowerCase()));
                                });
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have any homes.");
                        }
                    }
                }

                if (lbl.equalsIgnoreCase("ahomes")) {
                    Session session = HibernateUtil.sessionFactory.openSession();
                    AuthorityPlayer authorityPlayer = AuthorityPlayerFactory.getPlayerByPlayer(session, player);
                    if (null == authorityPlayer.getHomes()) {
                        player.sendMessage(ChatColor.RED + "You do not have any homes.");
                    } else {
                        StringBuilder result = new StringBuilder();
                        authorityPlayer.getHomes().forEach(home ->
                            result.append(String.format("[" + ChatColor.BOLD + "%s" + ChatColor.RESET + "] ", home.getName()))
                        );
                        player.sendMessage(result.toString());
                    }
                    session.close();
                }

                if (lbl.equalsIgnoreCase("dhome")) {
                    if (args.length == 0) {
                        player.sendMessage("Invalid command. Try: /rhome <name>");
                    } else {
                        String name = CommandUtil.convertArgsToString(args).toLowerCase();
                        Session session = HibernateUtil.sessionFactory.openSession();
                        Transaction transaction = session.beginTransaction();
                        AuthorityPlayer authorityPlayer = AuthorityPlayerFactory.getPlayerByPlayer(session, player);
                        if (null != authorityPlayer.getHomes()) {
                            List<AuthorityHome> homesToDelete = authorityPlayer.getHomes().stream()
                                    .filter(home -> home.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
                            for (AuthorityHome home : homesToDelete)
                                session.remove(home);
                            // Only persist if a home is deleted.
                            authorityPlayer.getHomes().removeAll(homesToDelete);
                            session.persist(authorityPlayer);
                            player.sendMessage(String.format("The home, %s, has been deleted.", name));
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have any homes.");
                        }
                        transaction.commit();
                        session.close();
                    }
                }

                if (lbl.equalsIgnoreCase("shome")) {
                    if (args.length == 0) {
                        player.sendMessage("Invalid command. Try: /shome <name>");
                    } else {
                        String name = CommandUtil.convertArgsToString(args).toLowerCase();
                        Location location = player.getLocation();
                        Session session = HibernateUtil.sessionFactory.openSession();
                        Transaction transaction = session.beginTransaction();
                        AuthorityPlayer authorityPlayer = AuthorityPlayerFactory.getPlayerByPlayer(session, player);
                        boolean doesHomeExist = BooleanUtil.
                                isGreaterThanZero(authorityPlayer.getHomes().stream()
                                        .filter(home -> home.getName().equalsIgnoreCase(name)).count());
                        if (doesHomeExist) {
                            authorityPlayer.getHomes().stream()
                                    .filter(home -> home.getName().equalsIgnoreCase(name))
                                    .forEach(home -> home.fromLocation(name, location));
                        } else {
                            AuthorityHome authorityHome = new AuthorityHome();
                            authorityHome.fromLocation(name, location);
                            authorityHome.setCreatedOn(new Date());
                            session.persist(authorityHome);
                            authorityPlayer.getHomes().add(authorityHome);
                        }
                        session.persist(authorityPlayer);
                        player.sendMessage(ChatColor.GREEN + String.format("Your home, " + ChatColor.BOLD + "%s" + ChatColor.RESET + "" + ChatColor.GREEN + ", has been saved/updated.", name.toLowerCase()));
                        transaction.commit();
                        session.close();
                    }
                }

                if (lbl.equalsIgnoreCase("invhome"))    {
                    if (args.length < 2) {
                        player.sendMessage("Invalid command. Try: /invhome <player> <home>");
                    } else {
                        String playerName = args[0].toLowerCase();
                        String homeName = CommandUtil.convertArgsToString(1, args).toLowerCase();
                        // Determine if the player is online
                        Player invited = Bukkit.getPlayer(playerName);
                        if (BooleanUtil.isFalse(Objects.isNull(invited))) {
                            Session session = HibernateUtil.sessionFactory.openSession();
                            Transaction transaction = session.beginTransaction();
                            AuthorityPlayer authorityPlayer = AuthorityPlayerFactory.getPlayerByPlayer(session, player);
                            AuthorityPlayer target = AuthorityPlayerFactory.getPlayerByPlayer(session, invited);
                            // Determine if the player has the home.
                            List<AuthorityHome> matchingHomes = authorityPlayer.getHomes().stream().filter(home -> home.getName().equalsIgnoreCase(homeName)).collect(Collectors.toList());
                            if ( BooleanUtil.isGreaterThanZero(matchingHomes.size()) ) {
                                // Just grab the first one.
                                AuthorityHome home = matchingHomes.get(GeneralUtil.ZERO);
                                AuthorityHomeInvite invite = new AuthorityHomeInvite();
                                invite.setHome(home);
                                invite.setCreatedOn(new Date());
                                session.persist(invite);
                                target.getHomeInvites().add(invite);
                                session.persist(target);
                            } else {
                                player.sendMessage(String.format("You do not have a home by the name of " + ChatColor.BOLD + "%s", homeName));
                            }
                            transaction.commit();
                            session.close();
                        } else {
                            player.sendMessage(String.format("The player, %s" + ChatColor.RESET + ". is not online.", ChatColor.BOLD + playerName));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }
}
