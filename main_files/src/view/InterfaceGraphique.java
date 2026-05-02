package view;

import javax.swing.*;


import java.awt.*;

public class InterfaceGraphique implements Runnable {
    JFrame frame;
    boolean maximized = false;

    public void run() {
        // Creation d'une fenetre
        frame = new JFrame("LACUNA");

        JeuGraphique aire = new JeuGraphique(frame);
        JButton redoButton = new JButton("Redo");
        JButton undoButton = new JButton("Undo");
        JButton resetButton = new JButton("Reset");
        JPanel buttonPanel = new JPanel();
        redoButton.addActionListener(e -> {
            aire.jeu.redo();
            aire.repaint();
        });
        undoButton.addActionListener(e -> {
            aire.jeu.undo();
            aire.repaint();

        });
        resetButton.addActionListener(e ->{
            aire.jeu.reset();
            aire.repaint();
        });
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
        buttonPanel.add(resetButton);
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(aire, BorderLayout.CENTER);
        // Ajout de notre composant de dessin dans la fenetre
        // frame.add(aire);

        // Ecoute des évènements liés à la souris dans l'AireDeDessin
        // aire.addMouseListener(new EcouteurDeSouris(aire));

        // aire.addKeyListener(new EcouteurDeClavier(this, aire));

        // Un clic sur le bouton de fermeture clos l'application
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // On fixe la taille et on demarre
        frame.setSize(800, 800);

        frame.setVisible(true);
    }

    public void toggleFullscreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        if (maximized) {
            device.setFullScreenWindow(null);
            maximized = false;
        } else {
            device.setFullScreenWindow(frame);
            maximized = true;
        }
    }
}
