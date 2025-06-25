package com.monsite.mycolorplugin;

import org.bukkit.ChatColor;

import java.util.*;

/**
 * Gère les couleurs personnalisées visibles par chaque joueur.
 */
public class ColorManager {

    // Map<ViewerUUID, Map<TargetUUID, ChatColor>>
    private final Map<UUID, Map<UUID, ChatColor>> customColors = new HashMap<>();

    /**
     * Définit la couleur qu'un joueur voit pour un autre joueur.
     */
    public void setColor(UUID viewer, UUID target, ChatColor color) {
        customColors
                .computeIfAbsent(viewer, k -> new HashMap<>())
                .put(target, color);
    }

    /**
     * Récupère la couleur personnalisée d’un joueur cible, vue par un joueur.
     */
    public ChatColor getColor(UUID viewer, UUID target) {
        return customColors
                .getOrDefault(viewer, Collections.emptyMap())
                .get(target);
    }
}
