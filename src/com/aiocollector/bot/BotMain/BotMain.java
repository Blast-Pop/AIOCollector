package com.aiocollector.bot.BotMain;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;

@ScriptManifest(image = "C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\MainIcon.png", author = "ThePoff", name = "TakeMeAll P'Diddy", version = 0.8, category = Category.MONEYMAKING, description = "Collects items with a UI.")
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
        cookRawbeef = new CookRawbeef(this);

        new Thread(() -> {
            try {
                for (int i = 0; i < 30; i++) {
                    if (isCollectingCowhide || isCollectingBones || isBuryingBones || isCollectingRawbeef || isCookingRawMeet) {
                        return;
                    }
                    Thread.sleep(1000);
                }
                stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        log("Script started. Awaiting user interaction.");
        sleep(30000);
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
            cookRawbeef.manageInventory();
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

            // Center the main menu frame on the screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) ((screenSize.getWidth() - mainMenuFrame.getWidth()) / 2);
            int y = (int) ((screenSize.getHeight() - mainMenuFrame.getHeight()) / 2);
            mainMenuFrame.setLocation(x, y);

            // Use an icon for the Collect Item button
            collectItemButton = new JButton(new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\Collect_icon_Realease.png"));
            collectItemButton.addActionListener(e -> {
                mainMenuFrame.dispose();
                createCollectItemMenu();
            });

            // Optionally, you can add text below the button (label it)
            collectItemButton.setText("Collect Item");
            collectItemButton.setHorizontalTextPosition(JButton.CENTER);
            collectItemButton.setVerticalTextPosition(JButton.BOTTOM);
            collectItemButton.setIconTextGap(2); // Adds gap between icon and text (can be used for offset)


            farmingXPButton = new JButton("Farming XP");
            try {
                ImageIcon farmingXPIcon = new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\XP_Farming_Release.png");
                farmingXPButton.setIcon(farmingXPIcon);
                farmingXPButton.setText("Farming XP");
                farmingXPButton.setHorizontalTextPosition(JButton.CENTER);
                farmingXPButton.setVerticalTextPosition(JButton.BOTTOM);
                farmingXPButton.setIconTextGap(2); // Adjust gap if necessary
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions if icon isn't found
            }

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
            collectItemFrame.setSize(400, 200);
            collectItemFrame.setLayout(new BorderLayout());

            // Create buttons for the menu
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(4, 1)); // Arrange buttons vertically

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

            // Back button at the bottom, centered
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                collectItemFrame.dispose();
                createMainMenu(); // Go back to the main menu
            });

            buttonPanel.add(startCowhideButton);
            buttonPanel.add(startBonesButton);
            buttonPanel.add(startRawbeefButton);
            collectItemFrame.add(buttonPanel, BorderLayout.CENTER);

            JPanel backPanel = new JPanel();
            backPanel.add(backButton);
            collectItemFrame.add(backPanel, BorderLayout.SOUTH); // Add back button at the bottom

            collectItemFrame.setLocation(mainMenuFrame.getLocation());
            collectItemFrame.setVisible(true);
        });
    }


    private void createFarmingXPMenu() {
        SwingUtilities.invokeLater(() -> {
            farmingXPFrame = new JFrame("Farming XP Menu");
            farmingXPFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            farmingXPFrame.setSize(400, 200);
            farmingXPFrame.setLayout(new BorderLayout());

            // Create buttons for the menu
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(3, 1)); // Arrange buttons vertically

            cookRawMeetButton = new JButton("Cook Raw Meat");
            cookRawMeetButton.addActionListener(e -> {
                isCookingRawMeet = true;
                farmingXPFrame.dispose();
            });

            JButton startBonesBurierButton = new JButton("Bury Bones");
            startBonesBurierButton.addActionListener(e -> {
                isBuryingBones = true;
                farmingXPFrame.dispose();
            });

            // Back button at the bottom, centered
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                farmingXPFrame.dispose();
                createMainMenu(); // Go back to the main menu
            });

            buttonPanel.add(cookRawMeetButton);
            buttonPanel.add(startBonesBurierButton);
            farmingXPFrame.add(buttonPanel, BorderLayout.CENTER);

            JPanel backPanel = new JPanel();
            backPanel.add(backButton);
            farmingXPFrame.add(backPanel, BorderLayout.SOUTH); // Add back button at the bottom

            farmingXPFrame.setLocation(mainMenuFrame.getLocation());
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
