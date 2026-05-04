package view;

import global.Configuration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PionArgent extends JComponent {

    private Image pionArgent;
    private int nbPions = 6;
    private java.util.List<Rectangle> zonesPions = new java.util.ArrayList<>();
    private boolean dragging = false;
    private Point dragPoint = null;
    private DragLayer dragLayer;
    private JFrame frame;
    private int draggedIndex = -1;

    public PionArgent(JFrame frame) {
        this.frame = frame;
        this.dragLayer = (DragLayer) frame.getGlassPane();
        try {
            pionArgent = ImageIO.read(Configuration.ouvre("pion_argent.png"));
        } catch (Exception e) {
            Configuration.debugeurErreur("Erreur lors du chargement des images : " + e.getMessage());
            System.exit(3);
        }
        setFocusable(true);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                for (Rectangle r : zonesPions) {
                    if (r.contains(e.getPoint())) {
                        dragLayer.setDraggedImage(pionArgent);
                        dragLayer.setMouse(SwingUtilities.convertPoint(
                                PionArgent.this,
                                e.getPoint(),
                                frame));

                        dragging = true;
                        return;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
                dragLayer.clear();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging)
                    return;

                Point screenPoint = SwingUtilities.convertPoint(
                        PionArgent.this,
                        e.getPoint(),
                        frame);

                dragLayer.setMouse(screenPoint);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // fond noir
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        if (pionArgent == null)
            return;

        int width = getWidth();
        int height = getHeight();

        // Taille des pions (responsive)
        int gap = 10;
        int maxPossibleSizeByHeight = (height - (nbPions + 1) * gap) / nbPions;
        int maxPossibleSizeByWidth = (int) (width * 0.35);

        int size = Math.min(maxPossibleSizeByHeight, maxPossibleSizeByWidth);
        if (size <= 0)
            size = 10; //Sécurité

        // Hauteur totale du bloc
        int totalHeight = nbPions * size + (nbPions - 1) * gap;

        // Centrage vertical
        int startY = (height - totalHeight) / 2;

        // Alignement à DROITE (proche du centre car PionArgent est à gauche du centre)
        int startX = width - size - 10;

        zonesPions.clear();

        for (int i = 0; i < nbPions; i++) {

            int x = startX;
            int y = startY + i * (size + gap);

            zonesPions.add(new Rectangle(x, y, size, size));

            // si on est en train de drag ce pion on ne le dessine pas ici
            if (dragging && i == draggedIndex)
                continue;

            g.drawImage(pionArgent, x, y, size, size, null);
        }
        if (dragging && dragPoint != null && draggedIndex != -1) {
            int s = 40;
            g.drawImage(pionArgent,
                    dragPoint.x - s / 2,
                    dragPoint.y - s / 2,
                    s, s, null);
        }
    }
}
