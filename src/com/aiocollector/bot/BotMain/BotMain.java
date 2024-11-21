package com.aiocollector.bot.BotMain;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;
import javax.swing.ImageIcon;

@ScriptManifest(image = "C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\MainIcon.png",
        author = "ThePoff",
        name = "TakeMeAll P'Diddy",
        version = 0.12,
        category = Category.MONEYMAKING,
        description = "Collects items with a UI.")
public class BotMain extends AbstractScript {

    private JFrame mainMenuFrame;
    private JButton collectItemButton;
    private JButton farmingXPButton;

    private JFrame collectItemFrame;
    private JButton startCowhideButton;
    private JButton startBonesButton;
    private JButton startRawbeefButton;
    private JButton startSalmonButton; // Bouton pour SalmonCollector

    private JFrame farmingXPFrame;
    private JButton cookRawMeetButton;

    private CowhideCollector cowhideCollector;
    private BonesCollector bonesCollector;
    private BonesBurier bonesBurier;
    private RawbeefCollector rawbeefCollector;
    private CookRawbeef cookRawbeef;
    private Woodcutting woodcutting;
    private SalmonCollector salmonCollector; // Instance du SalmonCollector

    private boolean isCollectingCowhide = false;
    private boolean isCollectingBones = false;
    private boolean isBuryingBones = false;
    private boolean isCollectingRawbeef = false;
    private boolean isCollectingSalmon = false; // Indicateur pour SalmonCollector
    private boolean isCookingRawMeet = false;
    private boolean isCollectingWoodcutting = false;

    private boolean hasLoggedCowhideStart = false;
    private boolean hasLoggedBonesStart = false;
    private boolean hasLoggedRawbeefStart = false;
    private boolean hasLoggedSalmonStart = false; // Journalisation pour SalmonCollector
    private boolean hasLoggedCookStart = false;
    private boolean hasLoggedWoodcuttingStart = false;
    private boolean hasLoggedScriptIdle = false;

    @Override
    public void onStart() {
        createMainMenu();
        cowhideCollector = new CowhideCollector(this);
        bonesCollector = new BonesCollector(this);
        bonesBurier = new BonesBurier(this);
        rawbeefCollector = new RawbeefCollector(this);
        cookRawbeef = new CookRawbeef(this);
        woodcutting = new Woodcutting(this);
        salmonCollector = new SalmonCollector(this); // Initialisation du SalmonCollector

        // Thread pour arrêter le script après 30 secondes d'inactivité
        new Thread(() -> {
            try {
                int idleTime = 0;
                while (idleTime < 30) { // Attendre 30 secondes sans interaction
                    if (isCollectingCowhide || isCollectingBones || isBuryingBones || isCollectingRawbeef || isCollectingSalmon || isCookingRawMeet || isCollectingWoodcutting) {
                        idleTime = 0; // Réinitialiser si une action démarre
                    } else {
                        idleTime++;
                    }
                    Thread.sleep(1000);
                }
                log("No action selected. Stopping the script.");
                stop(); // Arrêter le script
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

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

        if (isCollectingSalmon) {
            if (!hasLoggedSalmonStart) {
                log("Starting Salmon Collector...");
                hasLoggedSalmonStart = true;
            }
            salmonCollector.collectSalmon(); // Exécuter SalmonCollector
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

        if (isCollectingWoodcutting) {
            if (!hasLoggedWoodcuttingStart) {
                log("Starting Woodcutting...");
                hasLoggedWoodcuttingStart = true;
            }
            woodcutting.chopWood();
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

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (int) ((screenSize.getWidth() - mainMenuFrame.getWidth()) / 2);
            int y = (int) ((screenSize.getHeight() - mainMenuFrame.getHeight()) / 2);
            mainMenuFrame.setLocation(x, y);

            collectItemButton = new JButton(new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\Collect_icon_Realease.png"));
            collectItemButton.addActionListener(e -> {
                mainMenuFrame.dispose();
                createCollectItemMenu();
            });

            collectItemButton.setText("Collect Item");
            collectItemButton.setHorizontalTextPosition(JButton.CENTER);
            collectItemButton.setVerticalTextPosition(JButton.BOTTOM);
            collectItemButton.setIconTextGap(2);

            farmingXPButton = new JButton("Farming XP");
            try {
                ImageIcon farmingXPIcon = new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\XP_Farming_Release.png");
                farmingXPButton.setIcon(farmingXPIcon);
                farmingXPButton.setText("Farming XP");
                farmingXPButton.setHorizontalTextPosition(JButton.CENTER);
                farmingXPButton.setVerticalTextPosition(JButton.BOTTOM);
                farmingXPButton.setIconTextGap(2);
            } catch (Exception e) {
                e.printStackTrace();
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

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(5, 1)); // 5 boutons maintenant

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

            startSalmonButton = new JButton("Start Salmon Collector"); // Nouveau bouton
            startSalmonButton.addActionListener(e -> {
                isCollectingSalmon = true;
                collectItemFrame.dispose();
            });

            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                collectItemFrame.dispose();
                createMainMenu();
            });

            buttonPanel.add(startCowhideButton);
            buttonPanel.add(startBonesButton);
            buttonPanel.add(startRawbeefButton);
            buttonPanel.add(startSalmonButton); // Ajout du bouton pour SalmonCollector
            collectItemFrame.add(buttonPanel, BorderLayout.CENTER);

            JPanel backPanel = new JPanel();
            backPanel.add(backButton);
            collectItemFrame.add(backPanel, BorderLayout.SOUTH);

            collectItemFrame.setLocation(mainMenuFrame.getLocation());
            collectItemFrame.setVisible(true);
        });
    }

    private void createFarmingXPMenu() {
        SwingUtilities.invokeLater(() -> {
            farmingXPFrame = new JFrame("Farming XP Menu");
            farmingXPFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            farmingXPFrame.setSize(400, 150);
            farmingXPFrame.setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 1));

            cookRawMeetButton = new JButton("Cook Raw Meat");
            cookRawMeetButton.addActionListener(e -> {
                isCookingRawMeet = true;
                farmingXPFrame.dispose();
            });

            buttonPanel.add(cookRawMeetButton);

            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                farmingXPFrame.dispose();
                createMainMenu();
            });

            farmingXPFrame.add(buttonPanel, BorderLayout.CENTER);

            JPanel backPanel = new JPanel();
            backPanel.add(backButton);
            farmingXPFrame.add(backPanel, BorderLayout.SOUTH);

            farmingXPFrame.setLocation(mainMenuFrame.getLocation());
            farmingXPFrame.setVisible(true);
        });
    }

    public void sleepUntil(Supplier<Boolean> condition, int timeout) {
        long startTime = System.currentTimeMillis();
        while (!condition.get() && (System.currentTimeMillis() - startTime) < timeout) {
            sleep(50); // Pause pour réduire la charge CPU
        }
    }
}
