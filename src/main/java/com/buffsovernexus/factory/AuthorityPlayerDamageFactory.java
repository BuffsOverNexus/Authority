package com.buffsovernexus.factory;

import com.buffsovernexus.entity.AuthorityPlayer;
import com.buffsovernexus.entity.AuthorityPlayerDamage;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class AuthorityPlayerDamageFactory {

    /**
     * Acquire a player's entire damage history (being attacked or attacking)
     * @param session - The database session
     * @param authorityPlayer - The player to lookup
     * @return - The entire history
     */
    public static List<AuthorityPlayerDamage> getPlayerDamageHistory(Session session, AuthorityPlayer authorityPlayer) {
        try {
            String query = String.format("FROM authority_player_damage WHERE defender_id = '%s' OR attacker_id = '%s'", authorityPlayer.getId(), authorityPlayer.getId());
            return session.createQuery(query, AuthorityPlayerDamage.class).list();
        } catch (NoResultException ex) {
            // Return empty but is allowed.
            return new ArrayList<>();
        } catch (Exception ex) {
            // Something unknown happened.
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Acquire a player's history (as the attacker) against someone else (as defender)
     * @param session - The database session
     * @param attacker - The attacker
     * @param defender - The defender
     * @return - The entire history
     */
    public static List<AuthorityPlayerDamage> getPlayerDamageHistoryWithOtherPlayer(Session session, AuthorityPlayer attacker, AuthorityPlayer defender) {
        try {
            String query = String.format("FROM authority_player_damage WHERE defender_id = '%s' AND attacker_id = '%s'", defender.getId(), attacker.getId());
            return session.createQuery(query, AuthorityPlayerDamage.class).list();
        } catch (NoResultException ex) {
            // Expected in some cases.
            return new ArrayList<>();
        } catch (Exception ex) {
            // Unexpected outcome.
            return new ArrayList<>();
        }
    }
}
