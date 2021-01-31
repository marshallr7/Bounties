package me.marshall.bounties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Map;
import java.util.UUID;

public class BountyBook {

    public static ItemStack bountyBook(UUID player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setTitle(ChatColor.DARK_GREEN + "Active Bounties");
        meta.setAuthor("Bounties");

        if (Utils.activeBounties.containsKey(player)) {
            meta.addPage("\n\n" + ChatColor.translateAlternateColorCodes('&', "        &2&lBounties:\n\n" + " &8View active bounties\n\n\n\n\n" + "  &8Your active bounty:\n\n" + "Name: " + Bukkit.getOfflinePlayer(player).getPlayer().getPlayerListName() + "\nAmount: $" + Utils.activeBounties.get(player)));
        } else {
            meta.addPage("\n\n" + ChatColor.translateAlternateColorCodes('&', "        &2&lBounties:\n\n" + " &8View active bounties"));
        }

        for (Map.Entry<UUID, Double> element : Utils.activeBounties.entrySet()) {
            UUID key = element.getKey();
            Double value = element.getValue();
            meta.addPage(ChatColor.translateAlternateColorCodes('&', "\n\n\n\n&2Name:&0 " + Bukkit.getOfflinePlayer(key).getName() + "\n&2Amount: &0$" + value));
        }
        book.setItemMeta(meta);
        return book;
    }

}
