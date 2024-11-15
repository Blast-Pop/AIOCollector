package com.aiocollector.bot.BotMain;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.Random;

import static org.dreambot.api.utilities.Logger.log;

public class CookRawbeef {

    private final BotMain script;

    public CookRawbeef(BotMain script) {
        this.script = script;
    }

    public void manageInventory() {
        if (!Inventory.contains("Bronze axe", "Tinderbox")) {
            log("Missing essential items. Heading to the bank.");
            goToBank();
        } else if (!Inventory.contains("Logs")) {
            chopWood();
        } else if (Inventory.contains("Bronze axe", "Tinderbox", "Logs") && !Inventory.isFull()) {
            collectRawBeef();
        } else if (Inventory.isFull() && Inventory.contains("Raw beef")) {
            cookAllRawBeef();
        } else if (Inventory.isFull() && !Inventory.contains("Raw beef")) {
            dropCookedAndBurntMeat();
        }
    }

    private void goToBank() {
        Area bankArea = new Area(3206, 3214, 3211, 3220, 2);
        if (!bankArea.contains(Players.getLocal())) {
            Walking.walk(bankArea.getRandomTile());
        } else {
            if (!Bank.isOpen()) {
                Bank.open();
            } else {
                Bank.withdraw("Bronze axe", 1);
                Bank.withdraw("Tinderbox", 1);
                script.sleep(randomDelay(2000, 3000));
                Bank.close();
            }
        }
    }

    private void chopWood() {
        if (Inventory.contains("Bronze axe") && !Inventory.contains("Logs")) {
            GameObject tree = GameObjects.closest("Tree");
            if (tree != null && tree.interact("Chop down")) {
                log("Chopping down a tree for logs.");
                script.sleep(randomDelay(4000, 5000));
            } else {
                log("No tree found to chop.");
            }
        }
    }

    private void collectRawBeef() {
        Area cowArea = new Area(3244, 3296, 3264, 3257);
        if (!cowArea.contains(Players.getLocal())) {
            Walking.walk(cowArea.getRandomTile());
        } else if (!Inventory.isFull()) {
            GroundItem rawBeef = GroundItems.closest("Raw beef");
            if (rawBeef != null && rawBeef.interact("Take")) {
                log("Collecting raw beef.");
                script.sleep(randomDelay(2000, 2500));
            } else {
                log("No raw beef found on the ground.");
                Tile randomTile = cowArea.getRandomTile();
            }
        } else {
            log("Inventory full. Ready for cooking.");
        }
    }

    private void cookAllRawBeef() {
        GameObject fire = GameObjects.closest("Fire");

        if (fire == null && Inventory.contains("Tinderbox") && Inventory.contains("Logs")) {
            Inventory.get("Tinderbox").useOn("Logs");
            script.sleep(randomDelay(3000, 4000));
            fire = GameObjects.closest("Fire");
        }

        if (fire != null && Inventory.contains("Raw beef")) {
            Inventory.get("Raw beef").useOn(fire);
            script.sleep(randomDelay(2000, 2500));

            if (Widgets.getWidgetChild(270, 14) != null && Widgets.getWidgetChild(270, 14).isVisible()) {
                Widgets.getWidgetChild(270, 14).interact();
                log("Selected 'Make All' to cook all raw beef.");
            } else {
                log("Could not find 'Make All' button.");
            }
            script.sleep(randomDelay(4000, 5000));
        } else {
            log("No fire found and unable to create one.");
        }
    }

    private void dropCookedAndBurntMeat() {
        while (Inventory.contains("Cooked meat") || Inventory.contains("Burnt meat")) {
            if (Inventory.contains("Cooked meat")) {
                Inventory.get("Cooked meat").interact("Drop");
            }
            if (Inventory.contains("Burnt meat")) {
                Inventory.get("Burnt meat").interact("Drop");
            }
            script.sleep(randomDelay(750, 1115));
        }
        log("Cleared inventory of Cooked and Burnt meat.");
    }

    private int randomDelay(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
}
