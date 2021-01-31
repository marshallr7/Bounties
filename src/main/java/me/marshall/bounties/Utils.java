package me.marshall.bounties;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Utils {

    public static HashMap<UUID, Double> activeBounties = new HashMap<>();

    public static HashMap<UUID, Double> getBounties() {
        return activeBounties;
    }

    public static Double getBounty(UUID player) {
        return activeBounties.get(player);
    }

    public static void setBounty(UUID player, Double amount) {
        activeBounties.put(player, amount);
    }

    public static void removeActiveBounty(UUID player) {
        activeBounties.remove(player);
    }

    public static void updateActiveBounty(UUID player, Double amount) {
        activeBounties.replace(player, activeBounties.get(player), amount + activeBounties.get(player));
    }


    public static ItemStack toHeadUuid(ItemStack item, UUID uuid) {
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        ItemMeta itemMeta = item.getItemMeta();
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        if (itemMeta.hasDisplayName()) skull.setDisplayName(itemMeta.getDisplayName());
        if (itemMeta.hasLore()) skull.setLore(itemMeta.getLore());
        item.setItemMeta(skull);
        return item;
    }


    public static ItemStack deadPlayerSkull(UUID playerUUID, String player, String killer, Double amount) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemStack playerHead = toHeadUuid(head, playerUUID);
        ItemMeta playerHeadLabel = playerHead.getItemMeta();
        playerHeadLabel.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Completed Bounty: " + ChatColor.GRAY + "" + ChatColor.BOLD + player);
        List<String> Lore = new ArrayList<>();
        Lore.add(ChatColor.GREEN + "Killer: " + ChatColor.GRAY + killer);
        Lore.add(ChatColor.GREEN + "Amount: " + ChatColor.GRAY + "$" + amount);
        Lore.add("");
        Lore.add(ChatColor.GRAY + "Click to redeem this bounty.");
        playerHeadLabel.setLore(Lore);
        playerHeadLabel.addEnchant(Enchantment.QUICK_CHARGE, 1, true);
        playerHeadLabel.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        NamespacedKey key = new NamespacedKey(Bounties.getInstance(), "Bounties-Key");
        playerHeadLabel.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, amount);
        playerHead.setItemMeta(playerHeadLabel);
        return playerHead;
    }
}
