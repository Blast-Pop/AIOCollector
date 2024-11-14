package com.aiocollector.bot.BotMain;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.items.GroundItem;

import static org.dreambot.api.utilities.Logger.log;

public class CowhideCollector {

    private final BotMain script;

    // Constructeur de la classe avec la référence à la classe principale
    public CowhideCollector(BotMain script) {
        this.script = script;
    }

    // Méthode principale qui vérifie l'inventaire et collecte les Cowhides
    public void collectCowhides() {
        if (Inventory.isFull()) {
            // Si l'inventaire est plein, retourner à la banque
            log("Inventory is full, going to the bank.");
            goToBank();
        } else {
            // Sinon, collecter des Cowhides
            collect();
        }
    }

    // Méthode pour collecter les Cowhides
    private void collect() {
        Area cowArea = new Area(3244, 3296, 3264, 3257);  // Zone des vaches à Lumbridge
        if (!cowArea.contains(Players.getLocal())) {
            // Se déplacer à la zone de collecte si le joueur n'y est pas
            log("Not in cow area, walking there.");
            Walking.walk(cowArea.getRandomTile());
        } else {
            // Chercher les peaux de vache au sol
            GroundItem cowhide = GroundItems.closest("Cowhide");  // Chercher la peau de vache la plus proche
            if (cowhide != null && cowhide.interact("Take")) {
                log("Cowhide found and picked up.");
                // Attendre que l'action se termine
                script.sleep(1000, 1500); // Attendre un délai pour ramasser l'objet
            } else {
                log("No cowhide found, walking randomly in the area.");
                // Si aucune peau n'est trouvée, se déplacer aléatoirement dans la zone
                Tile randomTile = cowArea.getRandomTile();
                Walking.walk(randomTile);  // Se déplacer aléatoirement
                script.sleep(2000, 3000);   // Attendre un peu avant de vérifier à nouveau
            }
        }
    }

    // Méthode pour se rendre à la banque et déposer les Cowhides
    private void goToBank() {
        Area bankArea = new Area(3206, 3214, 3211, 3220, 2);  // Zone de la banque à Lumbridge
        if (!bankArea.contains(Players.getLocal())) {
            // Si le joueur n'est pas à la banque, s'y déplacer
            log("Not at the bank, walking there.");
            Walking.walk(bankArea.getRandomTile());
        } else {
            // Ouvrir la banque si elle n'est pas déjà ouverte
            if (!Bank.isOpen()) {
                log("Opening the bank.");
                Bank.open();
            } else {
                // Déposer tous les Cowhides dans la banque
                log("Depositing Cowhides into the bank.");
                Bank.depositAllItems();
                // Attendre que l'inventaire soit vide et recommencer à collecter
                script.sleep(1000, 1500);
            }
        }
    }
}
