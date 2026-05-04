package view;

import global.Configuration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class PionOr extends JComponent {

    private Image pionOr;
    private int nbPions = 6;

    public PionOr() {

        try {
            pionOr = ImageIO.read(Configuration.ouvre("pion_or.png"));
        } catch (Exception e) {
            Configuration.debugeurErreur("Erreur lors du chargement des images : " + e.getMessage());
            System.exit(3);
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // fond noir
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        if (pionOr == null)
            return;

        int width = getWidth();
        int height = getHeight();

        // Taille des pions (responsive)
        int gap = 10; 
        int maxPossibleSizeByHeight = (height - (nbPions + 1) * gap) / nbPions;
        int maxPossibleSizeByWidth = (int) (width * 0.35); 

        int size = Math.min(maxPossibleSizeByHeight, maxPossibleSizeByWidth);
        if (size <= 0) size = 10; // Sécurité

        // Hauteur totale du bloc
        int totalHeight = nbPions * size + (nbPions - 1) * gap;

        // Centrage vertical
        int startY = (height - totalHeight) / 2;
        
        // Alignement à gauche (proche du centre car rightPanel est à droite du centre)
        int startX = 10; // Petit décalage du bord gauche

        for (int i = 0; i < nbPions; i++) {
            int x = startX;
            int y = startY + i * (size + gap);

            g.drawImage(pionOr, x, y, size, size, null);
        }
    }
}
