package com.aiocollector.bot.BotMain;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

@ScriptManifest(image = "C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\MainIcon.png",
        author = "ThePoff",
        name = "TakeMeAll P'Diddy",
        version = 0.13,
        category = Category.MONEYMAKING,
        description = "Collects items with a UI.")
public class BotMain extends AbstractScript {

    private JFrame mainMenuFrame;
    private JFrame collectItemFrame;
    private JFrame farmingXPFrame;

    private JButton collectItemButton;
    private JButton farmingXPButton;

    private JButton startCowhideButton;
    private JButton startBonesButton;
    private JButton startRawbeefButton;
    private JButton startSalmonButton;
    private JButton startWoodcuttingButton;
    private JButton startBoneBuryingButton;

    private boolean isCollectingCowhide = false;
    private boolean isCollectingBones = false;
    private boolean isCollectingRawbeef = false;
    private boolean isCollectingSalmon = false;
    private boolean isCollectingWoodcutting = false;
    private boolean isBuryingBones = false;

    private Woodcutting woodcutting;
    private BonesBurier bonesBurier;
    private CookRawbeef cookRawbeef;
    private CowhideCollector cowhideCollector;
    private BonesCollector bonesCollector;
    private RawbeefCollector rawbeefCollector;
    private SalmonCollector salmonCollector;

    @Override
    public void onStart() {
        createMainMenu();
        woodcutting = new Woodcutting(this);
        bonesBurier = new BonesBurier(this);
        cookRawbeef = new CookRawbeef(this);
        cowhideCollector = new CowhideCollector(this);
        bonesCollector = new BonesCollector(this);
        rawbeefCollector = new RawbeefCollector(this);
        salmonCollector = new SalmonCollector(this);
        log("Script started. Awaiting user interaction.");
    }

    @Override
    public int onLoop() {
        if (isCollectingCowhide) {
            log("Collecting Cowhides...");
            cowhideCollector.collectCowhides();
            return 500;
        }

        if (isCollectingBones) {
            log("Collecting Bones...");
            bonesCollector.collectBones();
            return 500;
        }

        if (isCollectingRawbeef) {
            log("Collecting Raw Beef...");
            rawbeefCollector.collectRawBeef();
            return 500;
        }

        if (isCollectingSalmon) {
            log("Collecting Salmon...");
            salmonCollector.collectSalmon();
            return 500;
        }

        if (isCollectingWoodcutting) {
            log("Woodcutting in progress...");
            woodcutting.chopWood();
            return 500;
        }

        if (isBuryingBones) {
            log("Burying Bones...");
            bonesBurier.buryBones();
            return 500;
        }

        log("Script is idle.");
        return 500;
    }

    private void createMainMenu() {
        SwingUtilities.invokeLater(() -> {
            mainMenuFrame = new JFrame("TakeMeAll Main Menu");
            mainMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainMenuFrame.setSize(400, 150);
            mainMenuFrame.setLayout(new FlowLayout());

            collectItemButton = new JButton("Collect Item", new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\Collect_icon_Realease.png"));
            collectItemButton.addActionListener(e -> {
                mainMenuFrame.dispose();
                createCollectItemMenu();
            });

            farmingXPButton = new JButton("Farming XP", new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\XP_Farming_Release.png"));
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
            collectItemFrame.setSize(400, 300);
            collectItemFrame.setLayout(new GridLayout(7, 1));

            startCowhideButton = new JButton("Start Cowhide Collector", new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\CowhideIcon.png"));
            startCowhideButton.addActionListener(e -> {
                isCollectingCowhide = true;
                collectItemFrame.dispose();
            });

            startBonesButton = new JButton("Start Bones Collector", new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\BonesIcon.png"));
            startBonesButton.addActionListener(e -> {
                isCollectingBones = true;
                collectItemFrame.dispose();
            });

            startRawbeefButton = new JButton("Start Raw Beef Collector", new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\RawBeefIcon.png"));
            startRawbeefButton.addActionListener(e -> {
                isCollectingRawbeef = true;
                collectItemFrame.dispose();
            });

            startSalmonButton = new JButton("Start Salmon Collector", new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\SalmonIcon.png"));
            startSalmonButton.addActionListener(e -> {
                isCollectingSalmon = true;
                collectItemFrame.dispose();
            });


            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                collectItemFrame.dispose();
                createMainMenu();
            });

            collectItemFrame.add(startCowhideButton);
            collectItemFrame.add(startBonesButton);
            collectItemFrame.add(startRawbeefButton);
            collectItemFrame.add(startSalmonButton);
            collectItemFrame.add(backButton);

            collectItemFrame.setVisible(true);
        });
    }

    private void createFarmingXPMenu() {
        SwingUtilities.invokeLater(() -> {
            farmingXPFrame = new JFrame("Farming XP Menu");
            farmingXPFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            farmingXPFrame.setSize(400, 200);
            farmingXPFrame.setLayout(new GridLayout(4, 1));

            JButton cookRawMeatButton = new JButton("Cook Raw Meat", new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\CookIcon.png"));
            cookRawMeatButton.addActionListener(e -> {
                isCollectingRawbeef = true;
                farmingXPFrame.dispose();
            });

            JButton woodcuttingButton = new JButton("Woodcutting", new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\WoodcuttingIcon.png"));
            woodcuttingButton.addActionListener(e -> {
                isCollectingWoodcutting = true;
                farmingXPFrame.dispose();
            });

            JButton boneBurierButton = new JButton("Bone Burying", new ImageIcon("C:\\Users\\Blast-Pop\\Documents\\AIOCollector\\icon\\BoneBuryingIcon.png"));
            boneBurierButton.addActionListener(e -> {
                isBuryingBones = true;
                farmingXPFrame.dispose();
            });

            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                farmingXPFrame.dispose();
                createMainMenu();
            });

            farmingXPFrame.add(cookRawMeatButton);
            farmingXPFrame.add(woodcuttingButton);
            farmingXPFrame.add(boneBurierButton);
            farmingXPFrame.add(backButton);

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
