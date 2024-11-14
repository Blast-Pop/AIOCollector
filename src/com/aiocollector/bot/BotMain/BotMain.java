package com.aiocollector.bot.BotMain;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import javax.swing.*;
import java.awt.*;

@ScriptManifest(author = "ThePoff", name = "TakeMeAll P'Diddy", version = 0.8, category = Category.MONEYMAKING, description = "Collects items with a UI.")
public class BotMain extends AbstractScript {

    private JFrame mainMenuFrame;
    private JButton collectItemButton;
    private JButton farmingXPButton;

    private JFrame collectItemFrame;
    private JButton startCowhideButton;
    private JButton startBonesButton;
    private JButton startRawbeefButton;

    private JFrame farmingXPFrame;
    private JButton cookRawMeetButton;

    private CowhideCollector cowhideCollector;
    private BonesCollector bonesCollector;
    private BonesBurier bonesBurier;
    private RawbeefCollector rawbeefCollector;
    private CookRawbeef cookRawbeef;

    private boolean isCollectingCowhide = false;
    private boolean isCollectingBones = false;
    private boolean isBuryingBones = false;
    private boolean isCollectingRawbeef = false;
    private boolean isCookingRawMeet = false;

    private boolean hasLoggedCowhideStart = false;
    private boolean hasLoggedBonesStart = false;
    private boolean hasLoggedRawbeefStart = false;
    private boolean hasLoggedCookStart = false;
    private boolean hasLoggedScriptIdle = false;

    @Override
    public void onStart() {
        createMainMenu();
        cowhideCollector = new CowhideCollector(this);
        bonesCollector = new BonesCollector(this);
        bonesBurier = new BonesBurier(this);
        rawbeefCollector = new RawbeefCollector(this);
        cookRawbeef = new CookRawbeef(this); // Initialize CookRawbeef
        log("Script started. Awaiting user interaction.");
    }

    @Override
    public int onLoop() {
        if (isCollectingCowhide) {
            if (!hasLoggedCowhideStart) {
                log("Starting Cowhide Collector...");
                hasLoggedCowhideStart = true;
            }
            cowhideCollector.collectCowhides();
            return 500;
        }

        if (isCollectingBones) {
            if (!hasLoggedBonesStart) {
                log("Starting Bones Collector...");
                hasLoggedBonesStart = true;
            }
            bonesCollector.collectBones();
            return 500;
        }

        if (isBuryingBones) {
            log("Burying Bones...");
            bonesBurier.buryBones();
            return 500;
        }

        if (isCollectingRawbeef) {
            if (!hasLoggedRawbeefStart) {
                log("Starting Raw Beef Collector...");
                hasLoggedRawbeefStart = true;
            }
            rawbeefCollector.collectRawBeef();
            return 500;
        }

        if (isCookingRawMeet) {
            if (!hasLoggedCookStart) {
                log("Starting to Cook Raw Meat...");
                hasLoggedCookStart = true;
            }
            cookRawbeef.manageInventory(); // Call to manage cooking and inventory
            return 500;
        }

        if (!hasLoggedScriptIdle) {
            log("Script is idle.");
            hasLoggedScriptIdle = true;
        }

        return 500;
    }

    private void createMainMenu() {
        SwingUtilities.invokeLater(() -> {
            mainMenuFrame = new JFrame("TakeMeAll Main Menu");
            mainMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainMenuFrame.setSize(400, 150);
            mainMenuFrame.setLayout(new FlowLayout());

            collectItemButton = new JButton("Collect Item");
            collectItemButton.addActionListener(e -> {
                mainMenuFrame.dispose();
                createCollectItemMenu();
            });

            farmingXPButton = new JButton("Farming XP");
            farmingXPButton.addActionListener(e -> {
                mainMenuFrame.dispose();
                createFarmingXPMenu();
            });

            mainMenuFrame.add(collectItemButton);
            mainMenuFrame.add(farmingXPButton);
            mainMenuFrame.setVisible(true);
        });
    }

    private void createCollectItemMenu() {
        SwingUtilities.invokeLater(() -> {
            collectItemFrame = new JFrame("Collect Item Menu");
            collectItemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            collectItemFrame.setSize(400, 150);
            collectItemFrame.setLayout(new FlowLayout());

            startCowhideButton = new JButton("Start Cowhide Collector");
            startCowhideButton.addActionListener(e -> {
                isCollectingCowhide = true;
                collectItemFrame.dispose();
            });

            startBonesButton = new JButton("Start Bones Collector");
            startBonesButton.addActionListener(e -> {
                isCollectingBones = true;
                collectItemFrame.dispose();
            });

            startRawbeefButton = new JButton("Start Raw Beef Collector");
            startRawbeefButton.addActionListener(e -> {
                isCollectingRawbeef = true;
                collectItemFrame.dispose();
            });

            collectItemFrame.add(startCowhideButton);
            collectItemFrame.add(startBonesButton);
            collectItemFrame.add(startRawbeefButton);
            collectItemFrame.setVisible(true);
        });
    }

    private void createFarmingXPMenu() {
        SwingUtilities.invokeLater(() -> {
            farmingXPFrame = new JFrame("Farming XP Menu");
            farmingXPFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            farmingXPFrame.setSize(400, 150);
            farmingXPFrame.setLayout(new FlowLayout());

            cookRawMeetButton = new JButton("Cook Raw Meat");
            cookRawMeetButton.addActionListener(e -> {
                isCookingRawMeet = true;
                farmingXPFrame.dispose();
            });

            farmingXPFrame.add(cookRawMeetButton);
            farmingXPFrame.setVisible(true);
        });
    }

    @Override
    public void onExit() {
        if (mainMenuFrame != null) mainMenuFrame.dispose();
        if (collectItemFrame != null) collectItemFrame.dispose();
        if (farmingXPFrame != null) farmingXPFrame.dispose();
    }
}
