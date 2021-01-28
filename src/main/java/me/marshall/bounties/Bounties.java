package me.marshall.bounties;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bounties extends JavaPlugin {

    public static Bounties plugin;
    private static Economy econ = null;

    public static Economy getEconomy() {
        return econ;
    }

    public static Bounties getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new PlayerEventHandler(), this);
        getCommand("bounty").setExecutor(new BountyCommands());
        if (!setupEconomy()) {
            System.out.println("No economy plugin found. Disabling Vault.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        loadBounties();

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        saveBounties();

    }


    public void loadBounties() {

        for (String keys : this.getConfig().getConfigurationSection("bounties").getKeys(false)) {
            Utils.activeBounties.put(keys, (Double) this.getConfig().get("bounties." + keys));
        }
    }


    public void saveBounties() {
        for (String player : Utils.getBounties().keySet()) {
            this.getConfig().set("bounties." + player, Utils.activeBounties.get(player));
        }
        saveConfig();
    }
}
