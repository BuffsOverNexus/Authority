package com.buffsovernexus.command;

import com.buffsovernexus.entity.AuthorityHome;
import com.buffsovernexus.entity.AuthorityPlayer;
import com.buffsovernexus.factory.AuthorityPlayerFactory;
import com.buffsovernexus.utility.BooleanUtil;
import com.buffsovernexus.utility.CommandUtil;
import com.buffsovernexus.utility.HibernateUtil;
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
import java.util.stream.Collectors;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        try {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (lbl.equalsIgnoreCase("ahome")) {
                    if (args.length == 0) {
                        player.sendMessage("Invalid command. Try: /ahome <name>");
                    } else {
                        String name = CommandUtil.convertArgsToString(args);
                        Session session = HibernateUtil.sessionFactory.openSession();
                        AuthorityPlayer authorityPlayer = AuthorityPlayerFactory.getPlayerByPlayer(session, player);
                        if (null != authorityPlayer.getHomes()) {
                            authorityPlayer.getHomes().stream().filter(home -> home.getName().equalsIgnoreCase(name))
                                    .forEach(home -> {
                                        player.teleport(home.toLocation());
                                        player.sendMessage(String.format("Teleported to home (%s)", home.getName().toLowerCase()));
                                    });
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
                            result.append(String.format("[%s] ", home.getName()))
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
                        player.sendMessage(ChatColor.GREEN + String.format("Your home, %s, has been saved/updated.", name.toLowerCase()));
                        transaction.commit();
                        session.close();
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
