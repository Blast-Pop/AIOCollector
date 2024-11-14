package com.aiocollector.bot.BotMain;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.methods.interactive.Players;

import java.util.Random;

public class CowhideCollector {

    private final BotMain script;

    public CowhideCollector(BotMain script) {
        this.script = script;
    }

    public void collectCowhides() {
        if (Inventory.isFull()) {
            goToBank();
        } else {
            collect();
        }
    }

    private void collect() {
        Area cowArea = new Area(3244, 3296, 3264, 3257);  // Zone des vaches à Lumbridge
        if (!cowArea.contains(Players.getLocal())) {
            // Se déplacer à la zone de collecte si le joueur n'y est pas
            Tile randomTile = cowArea.getRandomTile();
            Walking.walk(randomTile);
            script.sleep(randomDelay(1500, 3000));  // Pause aléatoire
        } else {
            GroundItem cowhide = GroundItems.closest("Cowhide");  // Chercher la peau de vache la plus proche
            if (cowhide != null && cowhide.interact("Take")) {
                script.sleep(randomDelay(1000, 1500));  // Pause après interaction
            } else {
                // Se déplacer aléatoirement si aucune peau n'est trouvée
                Tile randomTile = cowArea.getRandomTile();
                Walking.walk(randomTile);
                script.sleep(randomDelay(2000, 3000));  // Attendre avant de vérifier à nouveau
            }
        }
    }

    private void goToBank() {
        // Zone de la banque à Lumbridge
        Area bankArea = new Area(3206, 3214, 3211, 3220, 2);
        if (!bankArea.contains(Players.getLocal())) {
            Walking.walk(bankArea.getRandomTile());
        } else {
            if (!Bank.isOpen()) {
                Bank.open();
            } else {
                Bank.depositAllItems();
                script.sleep(randomDelay(1000, 1500));  // Attente après avoir déposé
            }
        }
    }

    private int randomDelay(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;  // Retourne un nombre aléatoire
    }
}
