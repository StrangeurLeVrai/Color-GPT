package com.monsite.mycolorplugin;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Point d'entrée principal du plugin.
 * Initialise les composants et enregistre les commandes et événements.
 */
public class Main extends JavaPlugin {
    private ColorManager colorManager;
    private ColorGUIManager guiManager;

    @Override
    public void onEnable() {
        // Initialise les gestionnaires
        this.colorManager = new ColorManager();
        this.guiManager = new ColorGUIManager(colorManager);

        // Commande avec exécution et tab-completion
        ColorCommand cmd = new ColorCommand(guiManager, this);
        getCommand("color").setExecutor(cmd);
        getCommand("color").setTabCompleter(cmd);

        // Événements GUI
        getServer().getPluginManager().registerEvents(
                new ColorInventoryListener(guiManager, colorManager), this
        );
    }
}