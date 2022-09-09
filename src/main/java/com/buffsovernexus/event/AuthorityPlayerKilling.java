package com.buffsovernexus.event;


import com.buffsovernexus.entity.AuthorityPlayer;
import com.buffsovernexus.entity.AuthorityPlayerKill;
import com.buffsovernexus.factory.AuthorityPlayerFactory;
import com.buffsovernexus.utility.HibernateUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;

public class AuthorityPlayerKilling implements Listener {

    @EventHandler
    public void onPlayerKillPlayer(PlayerDeathEvent event) {
        try {
            Player player = event.getEntity();
            if (player.getKiller() != null) {
                Player killer = player.getKiller();

                // Acquire database of each user
                Session session = HibernateUtil.sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                AuthorityPlayer authorityPlayer = AuthorityPlayerFactory.getPlayerByPlayer(session, player);
                AuthorityPlayer authorityKiller = AuthorityPlayerFactory.getPlayerByPlayer(session, killer);
                String message = event.getDeathMessage();

                AuthorityPlayerKill authorityPlayerKill = com.buffsovernexus.entity.AuthorityPlayerKill.builder()
                        .killer(authorityKiller)
                        .victim(authorityPlayer)
                        .message(message)
                        .occurred(new Date())
                        .build();

                session.persist(authorityPlayerKill);
                transaction.commit();
                session.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
