package com.aiocollector.bot.BotMain;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.methods.interactive.Players;

import java.util.Random;

public class SalmonCollector {

    private final BotMain script;

    public SalmonCollector(BotMain script) {
        this.script = script;
    }

    public void collectSalmon() {
        if (Inventory.isFull()) {
            goToBank();
        } else {
            collect();
        }
    }

    private void collect() {
        Area salmonArea = new Area(3100, 3425, 3109, 3434); // Zone des Raw Salmon au Village Barbare
        if (!salmonArea.contains(Players.getLocal())) {
            Tile randomTile = salmonArea.getRandomTile();
            Walking.walk(randomTile);
            script.sleep(randomDelay(1500, 3000));  // Pause aléatoire
        } else {
            GroundItem rawSalmon = GroundItems.closest("Raw Salmon");  // Chercher la peau de vache la plus proche
            if (rawSalmon != null && rawSalmon.interact("Take")) {
                script.sleep(randomDelay(1000, 1500));  // Pause après interaction
            } else {
                Tile randomTile = salmonArea.getRandomTile();
                Walking.walk(randomTile);
                script.sleep(randomDelay(2000, 3000));  // Attendre avant de vérifier à nouveau
            }
        }
    }

    private void goToBank() {
        Area bankArea = new Area(3184, 3442, 3210, 3422); // Zone de la banque de Varrock
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
