package com.monsite.mycolorplugin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

/**
 * Gère la création des inventaires interactifs.
 */
public class ColorGUIManager {

    private final ColorManager colorManager;

    public ColorGUIManager(ColorManager colorManager) {
        this.colorManager = colorManager;
    }

    /**
     * Affiche un inventaire contenant toutes les têtes des joueurs en ligne.
     */
    public void openPlayerSelection(Player viewer) {
        Inventory inv = Bukkit.createInventory(null, 54, "Choisis un joueur");

        for (Player target : Bukkit.getOnlinePlayers()) {
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner(target.getName());

            ChatColor color = colorManager.getColor(viewer.getUniqueId(), target.getUniqueId());
            meta.setDisplayName((color != null ? color : ChatColor.WHITE) + target.getName());

            skull.setItemMeta(meta);
            inv.addItem(skull);
        }

        viewer.openInventory(inv);
    }

    /**
     * Ouvre un inventaire avec les choix de couleurs.
     */
    public void openColorSelection(Player viewer, List<UUID> targets) {
        Inventory inv = Bukkit.createInventory(null, 27, "Choisis une couleur");

        for (ChatColor color : ChatColor.values()) {
            if (!color.isColor()) continue;

            ItemStack wool = new ItemStack(Material.WOOL, 1, (short) getWoolData(color));
            ItemMeta meta = wool.getItemMeta();
            meta.setDisplayName(color + "■ " + color.name());
            wool.setItemMeta(meta);
            inv.addItem(wool);
        }

        viewer.openInventory(inv);
        ColorInventoryListener.setPendingTargets(viewer.getUniqueId(), targets);
    }

    /**
     * Associe chaque couleur à une couleur de laine.
     */
    private int getWoolData(ChatColor color) {
        switch (color) {
            case BLACK: return 15;
            case DARK_BLUE: return 11;
            case DARK_GREEN: return 13;
            case DARK_AQUA: return 9;
            case DARK_RED: return 14;
            case DARK_PURPLE: return 10;
            case GOLD: return 1;
            case GRAY: return 7;
            case DARK_GRAY: return 8;
            case BLUE: return 3;
            case GREEN: return 5;
            case AQUA: return 9;
            case RED: return 14;
            case LIGHT_PURPLE: return 2;
            case YELLOW: return 4;
            case WHITE: return 0;
            default: return 0;
        }
    }
}
