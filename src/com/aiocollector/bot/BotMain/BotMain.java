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
    private JButton stopButton;
    private CowhideCollector cowhideCollector;
    private BonesCollector bonesCollector;
    private boolean isCollectingCowhide = false;
    private boolean isCollectingBones = false;

    @Override
    public void onStart() {
        // Créer l'UI au démarrage du script
        createUI();
        cowhideCollector = new CowhideCollector(this);// Crée l'instance de CowhideCollector
        bonesCollector = new BonesCollector(this);
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

            startBonesButton = new JButton("Start Bones Collector");
            startBonesButton.addActionListener(e -> {
                log("Starting Bones Collector...");
                isCollectingBones = true;
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
