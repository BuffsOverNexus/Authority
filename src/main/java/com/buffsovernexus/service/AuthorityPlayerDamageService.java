package com.buffsovernexus.service;

import com.buffsovernexus.entity.AuthorityPlayer;
import com.buffsovernexus.factory.AuthorityPlayerFactory;
import org.bukkit.entity.Player;
import org.hibernate.Session;

public class AuthorityPlayerDamageService {

    public void displayPlayerDamageDefenderHistory(Session session, Player defender) {
        try {
            AuthorityPlayer authorityDefender = AuthorityPlayerFactory.getPlayerByPlayer(session, defender);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
