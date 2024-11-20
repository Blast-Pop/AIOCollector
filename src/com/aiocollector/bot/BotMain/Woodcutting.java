package com.aiocollector.bot.BotMain;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.utilities.Logger;

import static org.dreambot.api.utilities.Logger.log;

import java.util.Random;

public class Woodcutting {
    private final BotMain script;

    public Woodcutting(BotMain script) {
        this.script = script;
    }

    // Méthode pour gérer l'inventaire et l'équipement
    public void inventoryManager() {
        // Si l'axe est déjà équipé, on n'a pas besoin de vérifier l'inventaire
        if (Equipment.contains("Bronze axe")) {
            log("Bronze Axe already equipped, ready to chop wood.");
        } else {
            log("Player has a Bronze Axe in inventory.");
            // Si l'axe n'est pas encore équipé et qu'il est dans l'inventaire, on l'équipe
            if (Inventory.contains("Bronze axe")) {
                log("Equipping Bronze Axe.");
                equipBronzeAxe(); // Equiper l'axe si il est dans l'inventaire
            } else {
                log("Player doesn't have a Bronze Axe in inventory.");
                getBronzeAxe();  // Chercher l'axe en banque si l'inventaire ne le contient pas
            }
        }
    }

    // Méthode pour récupérer un Bronze Axe dans la banque
    private void getBronzeAxe() {
        Area bankArea = new Area(3185, 3436, 3188, 3433);  // Zone de la banque (ajuster si nécessaire)

        // Aller à la banque si l'axe n'est pas dans l'inventaire
        if (!bankArea.contains(Players.getLocal())) {
            Walking.walk(bankArea.getRandomTile());
            script.sleep(randomDelay(2000, 3000));  // Pause pour simuler un déplacement réaliste
        } else {
            if (!Bank.isOpen()) {
                Bank.open();
                script.sleepUntil(Bank::isOpen, randomDelay(2000, 4000));
            } else {
                // Vérifier si un Bronze Axe est disponible dans la banque
                if (Bank.contains("Bronze axe")) {
                    Bank.withdraw("Bronze axe", 1);  // Retirer un axe en bronze de la banque
                    script.sleepUntil(() -> Inventory.contains("Bronze axe"), randomDelay(2000, 4000));
                    Bank.close();
                    equipBronzeAxe();  // Equiper l'axe après l'avoir retiré
                } else {
                    log("No Bronze Axe in bank, please acquire one manually.");
                }
            }
        }
    }

    // Fonction pour équiper l'axe en bronze
    private void equipBronzeAxe() {
        if (Inventory.contains("Bronze axe") && !Equipment.contains("Bronze axe")) {
            Inventory.get("Bronze axe").interact("Wield");  // Equiper l'axe
            script.sleepUntil(() -> Players.getLocal().getEquipment() != null && Players.getLocal().getEquipment().equals("Bronze axe"), randomDelay(2000, 3000));
            script.sleep(randomDelay(1000, 1500));  // Pause pour simuler un délai d'équipement
            log("Bronze Axe equipped.");
        }
    }

    // Méthode principale pour couper du bois
    public void chopWood() {
        // Avant de couper du bois, vérifier si l'inventaire est plein
        if (Inventory.isFull()) {
            log("Inventory is full, going to bank.");
            goToBank();  // Aller à la banque pour vider l'inventaire
        } else {
            // Vérifier si le joueur a un axe en bronze et s'il est équipé
            inventoryManager();

            // Si l'axe est équipé, on peut couper du bois
            if (Equipment.contains("Bronze axe")) {
                cutTree();  // Commencer à couper du bois
            } else {
                log("Axe not equipped yet, waiting.");
            }
        }
    }

    private void cutTree() {
        Area treeArea = new Area(3173, 3459, 3149, 3450);  // Zone des arbres (à ajuster selon votre besoin)
        String treeName = "Tree";  // Nom des arbres à couper

        if (!treeArea.contains(Players.getLocal())) {
            Tile randomTile = treeArea.getRandomTile();
            Walking.walk(randomTile);
            script.sleep(randomDelay(1500, 3000));  // Pause aléatoire
        } else {
            GameObject tree = GameObjects.closest(treeName);
            if (tree != null && tree.interact("Chop down")) {
                script.sleepUntil(() -> !tree.exists() || Inventory.isFull(), randomDelay(5000, 10000));
            } else {
                Tile randomTile = treeArea.getRandomTile();
                Walking.walk(randomTile);
                script.sleep(randomDelay(2000, 3000));  // Attente avant de vérifier à nouveau
            }
        }
    }

    // Méthode pour retourner à la banque lorsque l'inventaire est plein
    private void goToBank() {
        Area bankArea = new Area(3172, 3480, 3159, 3495);  // Zone de la banque (à ajuster selon votre besoin)
        if (!bankArea.contains(Players.getLocal())) {
            Walking.walk(bankArea.getRandomTile());
            script.sleep(randomDelay(2000, 3000));
        } else {
            if (!Bank.isOpen()) {
                Bank.open();
                script.sleepUntil(Bank::isOpen, randomDelay(2000, 4000));
            } else {
                Bank.depositAllExcept(item -> item.getName().contains("axe"));
                script.sleep(randomDelay(1000, 1500));  // Pause après dépôt
                Bank.close();
            }
        }
    }

    private int randomDelay(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;  // Retourne un nombre aléatoire
    }
}
