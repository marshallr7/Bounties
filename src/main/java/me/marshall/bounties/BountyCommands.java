package me.marshall.bounties;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BountyCommands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // CMD bounty add <player> <amount>
        // ARGS null 0 1 2
        // LEN 0 1 2 3
        if (args[0].equalsIgnoreCase("add")) {
            if (sender.hasPermission("bounty.add")) {
                Economy economy = Bounties.getEconomy();
                if (args.length != 3) {
                    if (sender instanceof Player) {
                        sender.sendMessage("§7[§2t§fBounties§7] §cCorrect command usage: /bounty add <player> <amount>");
                    } else {
                        Bukkit.getConsoleSender().sendMessage("§7[§2t§fBounties§7] §cCorrect command usage: /bounty add <player> <amount>");
                    }
                } else {
                    Player target = Bukkit.getPlayer(args[1]);
                    assert target != null;
                    if (!target.isOnline()) {
                        sender.sendMessage("§7[§2t§fBounties§7] §cThe player " + target + " is not online.");
                    }
                    UUID targetUUID = target.getUniqueId();
                    String targetString = target.getPlayerListName();
                    double amount = Double.parseDouble(args[2]);
                    Player player = (Player) sender;
                    if (economy.getBalance(player) < amount) {
                        sender.sendMessage("§7[§2t§fBounties§7] §cYou do not have $" + amount + ".");
                    } else if (Utils.getBounties().containsKey(targetUUID)) {
                        economy.withdrawPlayer(player, amount);
                        String message = "&7[&2t&fBounties&7] &2" + player.getDisplayName() + " &fhas added &2$" + amount
                                + " &fto the bounty of &2" + targetString + "! &fThe new bounty total is now: &2$" + (Utils.getBounty(targetUUID) + amount);
                        Utils.updateActiveBounty(targetUUID, amount);
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));

                    } else {
                        economy.withdrawPlayer(player, amount);
                        Utils.setBounty(targetUUID, amount);
                        String message = "&7[&2t&fBounties&7] &2" + player.getDisplayName() + " &fhas place a &2$" + amount
                                + " &fbounty on &2" + targetString + "!";
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }
                }
            }
            // CMD bounty remove <player>
            // ARGS null    0      1
            // LEN    0    1      2
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (sender.hasPermission("bounty.remove")) {
                if (args.length != 2) {
                    if (sender instanceof Player) {
                        sender.sendMessage("§7[§2t§fBounties§7] §cCorrect command usage: /bounty remove <player>");
                    } else {
                        Bukkit.getConsoleSender().sendMessage("§7[§2t§fBounties§7] §cCorrect command usage: /bounty remove <player>");
                    }
                } else {
                    Player target = Bukkit.getPlayer(args[1]);
                    assert target != null;
                    if (!target.isOnline()) {
                        sender.sendMessage("§7[§2t§fBounties§7] §cThe player " + target + " is not online.");
                    }
                    String targetString = target.getPlayerListName();
                    UUID targetUUID = target.getUniqueId();
                    Player player = (Player) sender;
                    if (Utils.activeBounties.containsKey(targetUUID)) {
                        Utils.removeActiveBounty(targetUUID);
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2t&fBounties&7] &fThe bounty on &2" + targetString + " &fhas been removed by Staff."));
                    } else {
                        sender.sendMessage("§7[§2t§fBounties§7] §cPlease enter a player with an active bounty.");
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l&m----------&2&l Bounties &7&l&m---------- \n&2Commands: \n &2/bounty help: &7Displays this message. \n &2/bounty add <player> <amount>: &7Add a bounty to a player."));
        } else if (args[0].equalsIgnoreCase("list")) {
            Player player = (Player) sender;
            player.openBook(BountyBook.bountyBook());
        }
        return true;
    }
}