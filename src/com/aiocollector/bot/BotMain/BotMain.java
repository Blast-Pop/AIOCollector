package com.aiocollector.bot.BotMain;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import javax.swing.*;
import java.awt.*;

@ScriptManifest(author = "ThePoff", name = "TakeMeAll P'Diddy", version = 0.7, category = Category.MONEYMAKING, description = "Collects items with a UI.")
public class BotMain extends AbstractScript {

    private JFrame uiFrame;
    private JButton startCowhideButton;
    private JButton startBonesButton;
    private JButton startBuryBonesButton;
    private JButton startRawbeefButton;  // New button for Raw Beef
    private JButton stopButton;
    private CowhideCollector cowhideCollector;
    private BonesCollector bonesCollector;
    private BonesBurier bonesBurier;
    private RawbeefCollector rawbeefCollector;  // Instance of RawbeefCollector
    private boolean isCollectingCowhide = false;
    private boolean isCollectingBones = false;
    private boolean isBuryingBones = false;
    private boolean isCollectingRawbeef = false;  // Track Raw Beef collection status

    private boolean hasLoggedCowhideStart = false;
    private boolean hasLoggedBonesStart = false;
    private boolean hasLoggedRawbeefStart = false;  // Track Raw Beef start log
    private boolean hasLoggedScriptIdle = false;

    @Override
    public void onStart() {
        // Create the UI when the script starts
        createUI();
        cowhideCollector = new CowhideCollector(this);
        bonesCollector = new BonesCollector(this);
        bonesBurier = new BonesBurier(this);
        rawbeefCollector = new RawbeefCollector(this);  // Initialize RawbeefCollector
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

        if (isCollectingRawbeef) {  // Check if Raw Beef collection is active
            if (!hasLoggedRawbeefStart) {
                log("Starting Raw Beef Collector...");
                hasLoggedRawbeefStart = true;
            }
            rawbeefCollector.collectRawBeef();
            return 500;
        }

        if (!hasLoggedScriptIdle) {
            log("Script is idle.");
            hasLoggedScriptIdle = true;
        }

        return 500;
    }

    private void createUI() {
        SwingUtilities.invokeLater(() -> {
            uiFrame = new JFrame("TakeMeAll Control Panel");
            uiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            uiFrame.setSize(400, 200);
            uiFrame.setLayout(new FlowLayout());

            // Start Cowhide Collector button
            startCowhideButton = new JButton("Start Cowhide Collector");
            startCowhideButton.addActionListener(e -> {
                isCollectingCowhide = true;
                uiFrame.dispose();
            });

            // Start Bones Collector button
            startBonesButton = new JButton("Start Bones Collector");
            startBonesButton.addActionListener(e -> {
                isCollectingBones = true;
                uiFrame.dispose();
            });

            // Start Bury Bones button
            startBuryBonesButton = new JButton("Start Bury Bones");
            startBuryBonesButton.addActionListener(e -> {
                isBuryingBones = true;
                uiFrame.dispose();
            });

            // Start Raw Beef Collector button
            startRawbeefButton = new JButton("Start Raw Beef Collector");
            startRawbeefButton.addActionListener(e -> {
                isCollectingRawbeef = true;
                uiFrame.dispose();
            });

            // Stop Script button
            stopButton = new JButton("Stop Script");
            stopButton.addActionListener(e -> stop());

            // Add the buttons to the frame
            uiFrame.add(startCowhideButton);
            uiFrame.add(startBonesButton);
            uiFrame.add(startBuryBonesButton);
            uiFrame.add(startRawbeefButton);  // Add the Raw Beef button
            uiFrame.add(stopButton);

            uiFrame.setVisible(true);
        });
    }

    @Override
    public void onExit() {
        // Close the UI when the script stops
        if (uiFrame != null) {
            uiFrame.dispose();
        }
    }
}
