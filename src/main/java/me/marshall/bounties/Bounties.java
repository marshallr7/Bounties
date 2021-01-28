package me.marshall.bounties;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

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
        saveDefaultConfig();
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new PlayerEventHandler(), this);
        Objects.requireNonNull(getCommand("bounty")).setExecutor(new BountyCommands());
        if (!setupEconomy()) {
            System.out.println("No economy plugin found. Disabling Vault.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
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
        return true;
    }

    @Override
    public void onDisable() {
        saveBounties();

    }


    public void loadBounties() {

        for (String keys : Objects.requireNonNull(this.getConfig().getConfigurationSection("bounties")).getKeys(false)) {
            Utils.activeBounties.put(UUID.fromString(keys), (Double) this.getConfig().get("bounties." + keys));
        }
    }


    public void saveBounties() {
        for (UUID player : Utils.getBounties().keySet()) {
            this.getConfig().set("bounties." + player, Utils.activeBounties.get(player));
        }
        saveConfig();
    }
}
