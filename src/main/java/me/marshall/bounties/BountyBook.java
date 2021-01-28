package me.marshall.bounties;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Map;

public class BountyBook {

    public static ItemStack bountyBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setTitle(ChatColor.DARK_GREEN + "Active Bounties");
        meta.setAuthor("Bounties");
        meta.addPage("\n\n\n\n" + ChatColor.translateAlternateColorCodes('&', "        &2&lBounties:\n\n" + " &8View active bounties"));
        for (Map.Entry element : Utils.activeBounties.entrySet()) {
            String key = (String) element.getKey();
            Double value = (Double) element.getValue();
            meta.addPage(ChatColor.translateAlternateColorCodes('&', "\n\n\n\n&2Name:&0 " + key + "\n&2Amount: &0$" + value));
        }
        book.setItemMeta(meta);
        return book;
    }

}
