package com.buffsovernexus.utility;

import com.buffsovernexus.Authority;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    public static SessionFactory sessionFactory;
    public static void setup() throws Exception {
        try {
            Configuration config = new Configuration();

            if (!Authority.isProduction()) {
                // Initialized development configuration
                sessionFactory = config.configure("hibernate_dev.cfg.xml").buildSessionFactory();
            } else {
                //initialize session factory
                sessionFactory = new Configuration().configure().buildSessionFactory();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new Exception("Could not setup database");
        }
    }
}
