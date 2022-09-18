package com.buffsovernexus.service;

import com.buffsovernexus.entity.AuthorityPlayer;
import com.buffsovernexus.entity.AuthorityPlayerDamage;
import com.buffsovernexus.enumerator.ErrorMessage;
import com.buffsovernexus.factory.AuthorityPlayerDamageFactory;
import com.buffsovernexus.factory.AuthorityPlayerFactory;
import com.buffsovernexus.utility.DateUtil;
import com.buffsovernexus.utility.HibernateUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hibernate.Session;

import java.util.List;

public class AuthorityPlayerService {


    public static void getPlayerLookup(Player player, String name) {
        try {
            // Determine if the player exists on the server.
            Player onlinePlayer = Bukkit.getPlayer(name);
            if (null == onlinePlayer) {
                // Try to find the player in the database using the name.
                Session session = HibernateUtil.sessionFactory.openSession();
                AuthorityPlayer authorityPlayer = AuthorityPlayerFactory.getPlayerByName(session, name);
                if (null == authorityPlayer) {
                    // Failed to lookup player
                    player.sendMessage(ErrorMessage.INVALID_NAME_PLAYER_LOOKUP.toString());
                } else {
                    // Perform playerLookup
                    displayPlayerLookup(session, player, authorityPlayer);
                }
                session.close();
            } else {
                // Handle logic in overload.
                getPlayerLookup(player, onlinePlayer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getPlayerLookup(Player player, Player target) {
        try {
            Session session = HibernateUtil.sessionFactory.openSession();
            AuthorityPlayer authorityPlayer = AuthorityPlayerFactory.getPlayerByPlayer(session, target);
            displayPlayerLookup(session, player, authorityPlayer);
            session.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Display a player's statistics
     * @param session - Required for data lookup
     * @param player - The player to send the message to
     * @param authorityPlayer - The authorityPlayer to display stats of
     */
    private static void displayPlayerLookup(Session session, Player player, AuthorityPlayer authorityPlayer) {
        try {
            // First, display their name and UUID
            player.sendMessage(String.format(">> (#%s) %s <<", authorityPlayer.getId(), authorityPlayer.getName()));
            player.sendMessage();
            player.sendMessage(String.format("Joined: %s", DateUtil.convertDateToPretty(authorityPlayer.getJoined())));
            player.sendMessage(String.format("Last Seen: %s", DateUtil.convertDateToPretty(authorityPlayer.getLastSeen())));


            List<AuthorityPlayerDamage> playerDamageHistory = AuthorityPlayerDamageFactory.getPlayerDamageHistory(session, authorityPlayer);
            playerDamageHistory.forEach( history -> player.sendMessage(history.getAttacker().getName() + " attacked " + history.getDefender().getName() + " on " + DateUtil.convertDateToPretty(history.getOccurred())) );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Determine if a player exists.
     * @param session - The database session
     * @param player - The player to determine if exists
     * @return boolean
     */
    public static boolean doesPlayerExist(Session session, Player player) {
        try {
            return null != AuthorityPlayerFactory.getPlayerByPlayer(session, player);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
