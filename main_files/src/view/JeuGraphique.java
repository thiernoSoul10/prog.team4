package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import global.Configuration;
import model.Cercle;
import model.Coordonnees;
import model.Fleur;
import model.Jeu;

import model.Pion;
import controller.EcouteurDeSouris;

public class JeuGraphique extends JComponent {
    private JFrame frame;
    public Jeu jeu;
    private int width = 660, height = 360;
    //private JButton undoButton;
    //private JButton redoButton;
    //private JButton resetButton;

    // les images des fleurs et des pions seront chargées dans le constructeur de
    // l'interface graphique
    private Image rougeImg, bleueImg, jauneImg, verteImg, orangeImg, violetteImg, roseImg;
    private Image orImg, argentImg;
    private Image plateauImg;

    private EcouteurDeSouris mouse;

    public JeuGraphique(JFrame frame) {
        this.frame = frame;

        this.jeu = new Jeu(660, 360,
                new Cercle(new Coordonnees(width / 2, height / 2), (width > height ? height : width) / 2));

        setFocusable(true);
        requestFocusInWindow();
        mouse = new EcouteurDeSouris(this);

        // undoButton = new JButton("Undo");
        // redoButton = new JButton("Redo");
        // resetButton = new JButton("Reset");
        

        //JPanel buttonPanel = new JPanel();
        //buttonPanel.add(undoButton);
        //buttonPanel.add(redoButton);
        //buttonPanel.add(resetButton);


        try {
            // Chargement des images des fleurs
            rougeImg = ImageIO.read(Configuration.ouvre("fleur_rouge.png"));
            bleueImg = ImageIO.read(Configuration.ouvre("fleur_bleue.png"));
            jauneImg = ImageIO.read(Configuration.ouvre("fleur_jaune.png"));
            verteImg = ImageIO.read(Configuration.ouvre("fleur_verte.png"));
            orangeImg = ImageIO.read(Configuration.ouvre("fleur_orange.png"));
            violetteImg = ImageIO.read(Configuration.ouvre("fleur_violette.png"));
            roseImg = ImageIO.read(Configuration.ouvre("fleur_rose.png"));

            // Chargement des images des pions
            orImg = ImageIO.read(Configuration.ouvre("pion_or.png"));
            argentImg = ImageIO.read(Configuration.ouvre("pion_argent.png"));

            // Chargement de l'image du plateau
            plateauImg = ImageIO.read(Configuration.ouvre("plateau.png"));
        } catch (Exception e) {
            Configuration.debugeurErreur("Erreur lors du chargement des images : " + e.getMessage());
            System.exit(3);
        }

        // Aucun pion placé au démarrage : les pions seront ajoutés par le joueur en jeu.
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        //frame.setLayout(new BorderLayout());
        //frame.add(buttonPanel, BorderLayout.NORTH);
        //frame.add(this, BorderLayout.CENTER);
    }

    public Coordonnees screenToModel(int screenX, int screenY) {
        int componentWidth = getWidth();
        int componentHeight = getHeight();
        int boardSize = Math.min(componentWidth, componentHeight);
        int xOffset = (componentWidth - boardSize) / 2;
        int yOffset = (componentHeight - boardSize) / 2;
        int centerX = xOffset + boardSize / 2;
        int centerY = yOffset + boardSize / 2;
        double rayonMax = boardSize * 0.35;

        // On rapporte x et y par rapport au centre et on scale selon le rayon
        double relX = (screenX - centerX) / rayonMax;
        double relY = (screenY - centerY) / rayonMax;

        // Le rayon du modèle est 180, et le centre est (330, 180)
        int modelX = (int) (330 + relX * 180.0);
        int modelY = (int) (180 + relY * 180.0);

        return new Coordonnees(modelX, modelY);
    }

    public Coordonnees modelToScreen(int modelX, int modelY) {
        int componentWidth = getWidth();
        int componentHeight = getHeight();
        int boardSize = Math.min(componentWidth, componentHeight);
        int xOffset = (componentWidth - boardSize) / 2;
        int yOffset = (componentHeight - boardSize) / 2;
        int centerX = xOffset + boardSize / 2;
        int centerY = yOffset + boardSize / 2;
        double rayonMax = boardSize * 0.35;

        // Le centre du modèle est (330, 180) et le rayon est 180
        double relX = (modelX - 330) / 180.0;
        double relY = (modelY - 180) / 180.0;

        int screenX = (int) (centerX + relX * rayonMax);
        int screenY = (int) (centerY + relY * rayonMax);

        return new Coordonnees(screenX, screenY);
    }

    public void paintComponent(Graphics g) {
        ArrayList<Pion> pions = jeu.getPions();
        ArrayList<Fleur> fleurs = jeu.getFleurs();
         

        int componentWidth = getWidth();
        int componentHeight = getHeight();

        int boardSize = Math.min(componentWidth, componentHeight);
        int taille = (int)(boardSize*0.05); // taille des fleures relative à la taille du plateau
        int xOffset = (componentWidth - boardSize) / 2;
        int yOffset = (componentHeight - boardSize) / 2;

        int centerX = xOffset + boardSize / 2;
        int centerY = yOffset + boardSize / 2;

        // Rayon utilisable dans le cercle noir
        double rayonMax = boardSize * 0.35;

        g.drawImage(plateauImg, xOffset, yOffset, boardSize, boardSize, null);

        // Cercle de jeu : limites visibles
        g.setColor(Color.RED);
        int diameter = (int) (2 * rayonMax);
        int circleX = centerX - (int) rayonMax;
        int circleY = centerY - (int) rayonMax;
        g.drawOval(circleX, circleY, diameter, diameter);
        g.fillOval(centerX - 3, centerY - 3, 6, 6);
        g.setColor(Color.BLACK);

        // Dessiner les fleurs avec mise à l'échelle
        for (Fleur fleur : fleurs) {
            if (fleur != null) {
                Image img = null;
                switch (fleur.getType()) {
                    case ROUGE:
                        img = rougeImg;
                        break;
                    case BLEUE:
                        img = bleueImg;
                        break;
                    case JAUNE:
                        img = jauneImg;
                        break;
                    case VERTE:
                        img = verteImg;
                        break;
                    case ORANGE:
                        img = orangeImg;
                        break;
                    case VIOLETTE:
                        img = violetteImg;
                        break;
                    case ROSE:
                        img = roseImg;
                        break;
                    default:
                        break;
                }
                if (img != null) {
                    Coordonnees pos = modelToScreen(fleur.getPosition().x, fleur.getPosition().y);
                    g.drawImage(img, pos.getX() - taille / 2, pos.getY() - taille / 2, taille, taille, null);
                }
            }
        }
        Fleur f1 = jeu.getFleurSelectionnee1();
        Fleur f2 = jeu.getFleurSelectionnee2();
        int size = 40;
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));

        if (f1 != null && f1.getPosition() != null) {
            Coordonnees pos1 = modelToScreen(f1.getPosition().x, f1.getPosition().y);

            g2.drawRoundRect(
                    pos1.getX() - size / 2,
                    pos1.getY() - size / 2, size,
                    size,
                    15,
                    15);
        }

        if (f2 != null && f2.getPosition() != null) {
            Coordonnees pos2 = modelToScreen(f2.getPosition().x, f2.getPosition().y);

            g2.drawRoundRect(
                    pos2.getX() - size / 2,
                    pos2.getY() - size / 2,
                    size,
                    size,
                    15,
                    15);
        }

        //aligner les fleurs sélectionnées

        if (f1 != null && f2 != null && jeu.fleursConnectées(f1, f2)) {

            Coordonnees p1 = modelToScreen(
                    f1.getPosition().getX(),
                    f1.getPosition().getY());

            Coordonnees p2 = modelToScreen(
                    f2.getPosition().getX(),
                    f2.getPosition().getY());

            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3));

            g2.drawLine(
                    p1.getX(), p1.getY(),
                    p2.getX(), p2.getY());
        }
        // Dessiner les pions avec mise à l'échelle
        for (Pion pion : pions) {
            if (pion.getPosition() == null)
                continue;
            Image img = null;
            switch (pion.getType()) {
                case OR:
                    img = orImg;
                    break;
                case ARGENT:
                    img = argentImg;
                    break;
                default:
                    break;
            }
            if (img != null) {
                Coordonnees pos = modelToScreen(pion.getPosition().x, pion.getPosition().y);
                g.drawImage(img, pos.getX() - taille / 2, pos.getY() - taille / 2, taille, taille, null);
            }
        }
    }

}
