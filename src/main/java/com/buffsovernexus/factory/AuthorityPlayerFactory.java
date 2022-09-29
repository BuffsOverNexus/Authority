package com.buffsovernexus.factory;

import com.buffsovernexus.entity.AuthorityPlayer;
import jakarta.persistence.NoResultException;
import org.bukkit.entity.Player;
import org.hibernate.Session;

import java.util.UUID;

public class AuthorityPlayerFactory {

    /***
     * Retrieve an AuthorityPlayer from the database by Player
     * @param session - The database session
     * @param player - The player to retrieve
     * @return AuthorityPlayer -or- null if not found.
     */
    public static AuthorityPlayer getPlayerByPlayer(Session session, Player player) {
        return getPlayerByUUID(session, player.getUniqueId());
    }

    public static AuthorityPlayer getPlayerByUUID(Session session, UUID uuid) {
        try {
            return session.createQuery(String.format("FROM AuthorityPlayer WHERE uuid = '%s'", uuid.toString()), AuthorityPlayer.class).getSingleResult();
        } catch (NoResultException ex) {
            // THIS IS INTENDED
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static AuthorityPlayer getPlayerByName(Session session, Player player) {
        return getPlayerByName(session, player.getName());
    }

    public static AuthorityPlayer getPlayerByName(Session session, String name) {
        try {
            return session.createQuery(String.format("FROM AuthorityPlayer WHERE name = '%s'", name), AuthorityPlayer.class).getSingleResult();
        } catch (NoResultException ex) {
            // This is intended
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
