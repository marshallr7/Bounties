package me.marshall.bounties;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PlayerEventHandler implements Listener {

    private final Bounties plugin;

    public PlayerEventHandler(Bounties plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBountyDeath(PlayerDeathEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getEntity().getKiller();
        if (!(entity instanceof Player)) {
            return;
        }
        if (!(damager instanceof Player)) {
            return;
        }
        Location location = entity.getLocation();
        World world = entity.getWorld();
        if (world.getName().equals("pvparena") || world.getName().equals("event")) {
            return;
        }
        Player target = (Player) entity;
        Player attacker = (Player) damager;
        String targetString = target.getPlayerListName();
        UUID targetUUID = target.getUniqueId();
        if (Utils.activeBounties.containsKey(targetUUID)) {
            if (plugin.getConfig().contains("bounties." + targetUUID)) {
                world.dropItemNaturally(location, Utils.deadPlayerSkull(target.getUniqueId(), targetString, attacker.getPlayerListName(), Utils.activeBounties.get(targetUUID)));
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2t&fBounties&7] &2" + attacker.getPlayerListName() + " &fhas completed the &2$" + Utils.activeBounties.get(targetUUID) + " &fbounty on &2" + targetString));
                Utils.removeActiveBounty(targetUUID);
                plugin.getConfig().set("bounties." + targetUUID, null);
                plugin.saveConfig();
            }
        }

    }

    @EventHandler
    public void onBountyRedeem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }
        if (!(item.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Completed Bounty:"))) {
            return;
        }
        NamespacedKey key = new NamespacedKey(Bounties.getInstance(), "Bounties-Key");
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (container.has(key, PersistentDataType.DOUBLE)) {
            double foundValue = container.get(key, PersistentDataType.DOUBLE);
            event.setCancelled(true);
            player.getInventory().setItemInMainHand(null);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2t&fBounties&7] &fYou have successfully redeemed the bounty on &2" + item.getItemMeta().getDisplayName().substring(26) + " &ffor &2$" + foundValue));
            Economy economy = Bounties.getEconomy();
            economy.depositPlayer(player, foundValue);
        }
    }
}
