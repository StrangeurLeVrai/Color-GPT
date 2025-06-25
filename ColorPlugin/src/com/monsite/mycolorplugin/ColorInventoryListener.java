package com.monsite.mycolorplugin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

/**
 * Gère les interactions dans les GUI de sélection de joueur et de couleur.
 */
public class ColorInventoryListener implements Listener {

    private static final Map<UUID, List<UUID>> pendingColorTargets = new HashMap<>();

    private final ColorGUIManager guiManager;
    private final ColorManager colorManager;

    public ColorInventoryListener(ColorGUIManager guiManager, ColorManager colorManager) {
        this.guiManager = guiManager;
        this.colorManager = colorManager;
    }

    /**
     * Associe des cibles au joueur en train de choisir une couleur.
     */
    public static void setPendingTargets(UUID viewer, List<UUID> targets) {
        pendingColorTargets.put(viewer, targets);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        String title = e.getView().getTitle();
        e.setCancelled(true); // Empêche le déplacement des items

        if (title.equals("Choisis un joueur")) {
            SkullMeta meta = (SkullMeta) clicked.getItemMeta();
            Player selected = Bukkit.getPlayerExact(meta.getOwner());
            if (selected != null) {
                guiManager.openColorSelection(player, Collections.singletonList(selected.getUniqueId()));
            }
        }

        else if (title.equals("Choisis une couleur")) {
            ItemMeta meta = clicked.getItemMeta();
            if (meta == null) return;

            String name = ChatColor.stripColor(meta.getDisplayName()).split(" ")[1];
            ChatColor chosen = ChatColor.valueOf(name);

            List<UUID> targets = pendingColorTargets.remove(player.getUniqueId());
            if (targets != null) {
                for (UUID t : targets) {
                    colorManager.setColor(player.getUniqueId(), t, chosen);
                }
                player.sendMessage(ChatColor.GREEN + "Couleur appliquée !");
                guiManager.openPlayerSelection(player);
            }
        }
    }
}
