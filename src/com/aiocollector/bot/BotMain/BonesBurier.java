package com.aiocollector.bot.BotMain;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.Random;

public class BonesBurier {

    private final BotMain script;

    public BonesBurier(BotMain script) {
        this.script = script;
    }

    public void buryBones() {
        if (Inventory.isFull()) {
            bury();
        } else {
            collectBones();
        }
    }

    private void collectBones() {
        Area bonesArea = new Area(3244, 3296, 3264, 3257);  // Zone des bones à Lumbridge
        if (!bonesArea.contains(Players.getLocal())) {
            Tile randomTile = bonesArea.getRandomTile();
            Walking.walk(randomTile);
            script.sleep(randomDelay(1500, 3000));  // Pause aléatoire
        } else {
            GroundItem bones = GroundItems.closest("Bones");  // Chercher les bones
            if (bones != null && bones.interact("Take")) {
                script.sleep(randomDelay(4500, 5500));  // Pause après interaction
            } else {
                Tile randomTile = bonesArea.getRandomTile();
                Walking.walk(randomTile);
                script.sleep(randomDelay(2000, 3000));  // Attendre avant de vérifier à nouveau
            }
        }
    }

    private void bury() {
        // Tant que l'inventaire contient des os
        while (Inventory.contains("Bones")) {
            Inventory.get("Bones").interact("Bury");  // Enterre un os
            script.sleep(randomDelay(1765, 2185));  // Pause après chaque enterrement
        }
        // Une fois l'inventaire vide, commencer à ramasser les os
        if (Inventory.isEmpty()) {
            collectBones();
        }
    }


    private int randomDelay(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;  // Retourne un délai aléatoire
    }
}
