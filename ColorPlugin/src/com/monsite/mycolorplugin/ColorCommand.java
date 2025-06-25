package com.monsite.mycolorplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Gère la commande /color ainsi que la complétion automatique.
 */
public class ColorCommand implements CommandExecutor, TabCompleter {

    private final ColorGUIManager guiManager;
    private final JavaPlugin plugin;

    public ColorCommand(ColorGUIManager guiManager, JavaPlugin plugin) {
        this.guiManager = guiManager;
        this.plugin = plugin;
    }

    /**
     * Exécutée quand un joueur utilise /color ou /color <pseudo...>
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return true;
        }

        Player player = (Player) sender;

        // Si aucun argument, ouvrir la sélection de joueurs
        if (args.length == 0) {
            guiManager.openPlayerSelection(player);
        } else {
            // Chercher les joueurs spécifiés
            List<UUID> targets = Arrays.stream(args)
                    .map(Bukkit::getPlayerExact)
                    .filter(Objects::nonNull)
                    .map(Player::getUniqueId)
                    .collect(Collectors.toList());

            if (targets.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Aucun joueur valide.");
                return true;
            }

            // Ouvrir directement le menu de couleurs
            guiManager.openColorSelection(player, targets);
        }

        return true;
    }

    /**
     * Suggère automatiquement les pseudos des joueurs connectés.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
                                      String alias, String[] args) {
        if (args.length >= 1) {
            String last = args[args.length - 1].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(last))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
