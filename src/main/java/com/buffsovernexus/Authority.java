package com.buffsovernexus;

import com.buffsovernexus.command.AuthorityCommand;
import com.buffsovernexus.command.ChatCommand;
import com.buffsovernexus.command.HomeCommand;
import com.buffsovernexus.enumerator.Config;
import com.buffsovernexus.event.AuthorityPlayerDamaging;
import com.buffsovernexus.event.AuthorityPlayerJoin;
import com.buffsovernexus.event.AuthorityPlayerKilling;
import com.buffsovernexus.utility.HibernateUtil;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public class Authority extends JavaPlugin {
    private static Plugin plugin;
    private static boolean isProduction = false;

    public void onEnable() {
        try {
            // Save a default configuration
            this.saveDefaultConfig();

            // Step 1: Define the environment of the plugin
            String environment = this.getConfig().getString(Config.ENVIRONMENT.toString());
            // Only check if it is production. Otherwise,
            assert environment != null;
            if (environment.toLowerCase().contains("production")) {
                isProduction = true;
            }

            // Attempt to set up Hibernate. This is *required*.
            HibernateUtil.setup();

            plugin = this;

            // Step 3: Register events
            this.getServer().getPluginManager().registerEvents(new AuthorityPlayerJoin(), this);
            this.getServer().getPluginManager().registerEvents(new AuthorityPlayerKilling(), this);
            this.getServer().getPluginManager().registerEvents(new AuthorityPlayerDamaging(), this);

            // Step 4: Register commands
            Objects.requireNonNull(this.getCommand("authority")).setExecutor(new AuthorityCommand());
            Objects.requireNonNull(this.getCommand("ahome")).setExecutor(new HomeCommand());
            Objects.requireNonNull(this.getCommand("dhome")).setExecutor(new HomeCommand());
            Objects.requireNonNull(this.getCommand("shome")).setExecutor(new HomeCommand());
            Objects.requireNonNull(this.getCommand("ahomes")).setExecutor(new HomeCommand());
            Objects.requireNonNull(this.getCommand("cclear")).setExecutor(new ChatCommand());

        } catch (Exception ex) {
            // Disable the plugin on exception in startup.
            this.setEnabled(false);
            this.getLogger().log(Level.SEVERE, ex.getMessage());
        }
    }

    public void onDisable() {
        this.getLogger().log(Level.INFO, "Authority is now being disabled.");

        // Close database connection on disable.
        if (null != HibernateUtil.sessionFactory && HibernateUtil.sessionFactory.isOpen()) {
            HibernateUtil.sessionFactory.close();
            this.getLogger().log(Level.INFO, "Database connection severed.");
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static boolean isProduction() { return isProduction; }
}
