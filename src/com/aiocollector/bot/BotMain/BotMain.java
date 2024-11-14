package com.aiocollector.bot.BotMain;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import javax.swing.*;
import java.awt.*;

@ScriptManifest(author = "ThePoff", name = "TakeMeAll P'Diddy", version = 0.5, category = Category.MONEYMAKING, description = "Collects items with a UI.")
public class BotMain extends AbstractScript {

    private JFrame uiFrame;
    private JButton startCowhideButton;
    private JButton startBonesButton;
    private JButton startBonesBuryButton;  // New button for BonesBurier
    private JButton stopButton;
    private CowhideCollector cowhideCollector;
    private BonesCollector bonesCollector;
    private BonesBurier bonesBurier;  // New BonesBurier instance
    private boolean isCollectingCowhide = false;
    private boolean isCollectingBones = false;
    private boolean isBuryingBones = false;  // Flag to control burying bones

    @Override
    public void onStart() {
        // Créer l'UI au démarrage du script
        createUI();
        cowhideCollector = new CowhideCollector(this);  // Crée l'instance de CowhideCollector
        bonesCollector = new BonesCollector(this);  // Crée l'instance de BonesCollector
        bonesBurier = new BonesBurier(this);  // Crée l'instance de BonesBurier
        log("Script started. Awaiting user interaction.");
    }

    @Override
    public int onLoop() {
        // Si la collecte est activée, commence à collecter
        if (isCollectingCowhide) {
            log("Collecting Cowhides...");
            cowhideCollector.collectCowhides();  // Commence à collecter automatiquement
            return 500;  // Retourne la durée entre les boucles (en ms)
        }
        if (isCollectingBones) {
            log("Collecting Bones...");
            bonesCollector.collectBones();
        }
        if (isBuryingBones) {
            log("Burying Bones...");
            bonesBurier.buryBones();  // Utilise BonesBurier pour enterrer les bones
        }
        log("Script is idle.");
        return 500;  // Aucune action si la collecte n'est pas activée
    }

    private void createUI() {
        SwingUtilities.invokeLater(() -> {
            uiFrame = new JFrame("TakeMeAll Control Panel");
            uiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            uiFrame.setSize(400, 200);
            uiFrame.setLayout(new FlowLayout());

            // Bouton pour commencer la collecte des Cowhides
            startCowhideButton = new JButton("Start Cowhide Collector");
            startCowhideButton.addActionListener(e -> {
                log("Starting Cowhide Collector...");
                isCollectingCowhide = true;  // Active la collecte
                uiFrame.dispose();    // Ferme l'UI une fois la collecte lancée
            });

            // Bouton pour commencer la collecte des Bones
            startBonesButton = new JButton("Start Bones Collector");
            startBonesButton.addActionListener(e -> {
                log("Starting Bones Collector...");
                isCollectingBones = true;
                uiFrame.dispose();  // Ferme l'UI une fois la collecte lancée
            });

            // Nouveau bouton pour commencer à enterrer les Bones
            startBonesBuryButton = new JButton("Start Bones Burying");
            startBonesBuryButton.addActionListener(e -> {
                log("Starting Bones Burying...");
                isBuryingBones = true;  // Active l'enterrement des bones
                uiFrame.dispose();
            });

            // Bouton pour arrêter le script
            stopButton = new JButton("Stop Script");
            stopButton.addActionListener(e -> {
                log("Stopping script...");
                stop();  // Arrêter le script
            });

            // Ajouter les boutons à la fenêtre
            uiFrame.add(startCowhideButton);
            uiFrame.add(startBonesButton);
            uiFrame.add(startBonesBuryButton);  // Ajouter le bouton pour enterrer les bones
            uiFrame.add(stopButton);

            uiFrame.setVisible(true);
        });
    }

    @Override
    public void onExit() {
        // Fermer l'interface lorsque le script s'arrête
        if (uiFrame != null) {
            uiFrame.dispose();
        }
    }
}
