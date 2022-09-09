package com.buffsovernexus.event;

import com.buffsovernexus.entity.AuthorityPlayer;
import com.buffsovernexus.service.AuthorityPlayerService;
import com.buffsovernexus.utility.HibernateUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;

public class AuthorityPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoinRecordNotExisting(PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();
            Session session = HibernateUtil.sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            if (!AuthorityPlayerService.doesPlayerExist(session, player)) {
                AuthorityPlayer authorityPlayer = new AuthorityPlayer();
                authorityPlayer.setName(player.getName());
                authorityPlayer.setUuid(player.getUniqueId());
                authorityPlayer.setLastSeen(new Date());
                session.persist(authorityPlayer);
            }
            transaction.commit();
            session.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
