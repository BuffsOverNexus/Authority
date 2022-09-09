package com.buffsovernexus.event;

import com.buffsovernexus.entity.AuthorityPlayer;
import com.buffsovernexus.entity.AuthorityPlayerDamage;
import com.buffsovernexus.factory.AuthorityPlayerFactory;
import com.buffsovernexus.utility.HibernateUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;

public class AuthorityPlayerDamaging implements Listener {

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        try {
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                Player attacker = (Player) event.getDamager();
                Player defender = (Player) event.getEntity();
                String cause = event.getCause().toString();

                // Retrieve players from database
                Session session = HibernateUtil.sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                AuthorityPlayer authorityAttacker = AuthorityPlayerFactory.getPlayerByPlayer(session, attacker);
                AuthorityPlayer authorityDefender = AuthorityPlayerFactory.getPlayerByPlayer(session, defender);

                // Create the object to record the damage event
                AuthorityPlayerDamage authorityPlayerDamage =
                        com.buffsovernexus.entity.AuthorityPlayerDamage.builder()
                                .attacker(authorityAttacker)
                                .defender(authorityDefender)
                                .cause(cause)
                                .occurred(new Date())
                                .build();

                // Persist event and finish.
                session.persist(authorityPlayerDamage);
                transaction.commit();
                session.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
