package view;

import java.awt.Color;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

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


    // les images des fleurs et des pions seront chargées dans le constructeur de l'interface graphique
    private Image rougeImg, bleueImg, jauneImg, verteImg, orangeImg, violetteImg, roseImg;
    private Image orImg, argentImg;
    private Image plateauImg;

    private EcouteurDeSouris mouse;

    public JeuGraphique(JFrame frame) {
        this.frame = frame;
        //width = frame.getWidth();
        //height = frame.getHeight();

        int boardSize = Math.min(width , height); 
        
        int cx = width / 2;                      
        int cy = height / 2; 
                          
        int rayon = (int)(boardSize * 0.35);
           
        this.jeu = new Jeu(660, 360, new Cercle(new Coordonnees(cx, cy), rayon));

        setFocusable(true);
        requestFocusInWindow();
        mouse = new EcouteurDeSouris(this);

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


        jeu.placePion(jeu.getPions().get(0), new Coordonnees(150, 150));
        jeu.placePion(jeu.getPions().get(1), new Coordonnees(200, 300));

        frame.add(this);
        frame.addMouseListener(mouse);

    }

    /*// Dessin.
    public void paintComponent(Graphics g) {
        ArrayList<Pion> pions = jeu.getPions();
        Fleur[] fleurs = jeu.getFleurs();
        int taille = 40;

        // Dessiner le plateau de jeu
        g.drawImage(plateauImg, 0, 0, getWidth(), getHeight(), null);

        // Dessiner les fleurs
        for (Fleur fleur : fleurs) {
            if (fleur != null) {
                Image img = null;
                switch (fleur.getTypeFleur()) {
                    case ROUGE: img = rougeImg; break;
                    case BLEUE: img = bleueImg; break;
                    case JAUNE: img = jauneImg; break;
                    case VERTE: img = verteImg; break;
                    case ORANGE: img = orangeImg; break;
                    case VIOLETTE: img = violetteImg; break;
                    case ROSE: img = roseImg; break;
                    default: break;
                }
                if (img != null) {
                    g.drawImage(img, fleur.getPosition().x, fleur.getPosition().y, taille, taille, null);
                }
            }
        }

        // Dessiner les pions
        for (Pion pion : pions) {
            Image img = null;
            switch (pion.getTypePion()) {
                case OR: img = orImg; break;
                case ARGENT: img = argentImg; break;
                default: break;
            }
            if (img != null) {
                g.drawImage(img, pion.getPosition().x, pion.getPosition().y, taille, taille, null);
            }
        }
    }
        */

    // Chat pour rendre l'affichage plus joli et pour éviter les problèmes de redimensionnement des images lors du redimensionnement de la fenêtre, on va calculer les positions des fleurs et des pions en fonction de la taille de la fenêtre et de la taille du plateau de jeu
    public void paintComponent(Graphics g) {
        ArrayList<Pion> pions = jeu.getPions();
        ArrayList<Fleur> fleurs = jeu.getFleurs();
         

        // Facteurs de mise à l'échelle
        double scaleX = getWidth() / 660.0;
        double scaleY = getHeight() / 360.0;




        int componentWidth = getWidth();
        int componentHeight = getHeight();

        int boardSize = Math.min(componentWidth, componentHeight);
        int taille = (int)(boardSize*0.05); // taille des fleures relative à la taille du plateau
        int xOffset = (componentWidth - boardSize) / 2;
        int yOffset = (componentHeight - boardSize) / 2;

        // Centre du plateau affiché

        int centerX = xOffset + boardSize / 2;
        int centerY = yOffset + boardSize / 2;

        // Rayon utilisable dans le cercle noir
        double rayonMax = boardSize * 0.35;

        g.drawImage(plateauImg, xOffset, yOffset, boardSize, boardSize, null);

        // Cercle de jeu : limites visibles
        g.setColor(Color.RED);
        int diameter = (int)(2 * rayonMax);
        int circleX = centerX - (int)rayonMax;
        int circleY = centerY - (int)rayonMax;
        g.drawOval(circleX, circleY, diameter, diameter);
        g.fillOval(centerX - 3, centerY - 3, 6, 6);
        g.setColor(Color.BLACK);

        // Dessiner les fleurs avec mise à l'échelle
        for (Fleur fleur : fleurs) {
            if (fleur != null) {
                Image img = null;
                switch (fleur.getType()) {
                    case ROUGE: img = rougeImg; break;
                    case BLEUE: img = bleueImg; break;
                    case JAUNE: img = jauneImg; break;
                    case VERTE: img = verteImg; break;
                    case ORANGE: img = orangeImg; break;
                    case VIOLETTE: img = violetteImg; break;
                    case ROSE: img = roseImg; break;
                    default: break;
                }
                if (img != null) {
                    double rayon = jeu.getCercleDeJeu().getRayon(); 
                    double cx = jeu.getCercleDeJeu().getCentre().getX(); 
                    double cy = jeu.getCercleDeJeu().getCentre().getY(); 

                    // Pour les fleurs :
                    double relX = (fleur.getPosition().x - cx) / rayon;
                    double relY = (fleur.getPosition().y - cy) / rayon;
                    int x = (int)(centerX + relX * rayonMax);
                    int y = (int)(centerY + relY * rayonMax);
                    g.drawImage(img, x - taille/2, y - taille/2, taille, taille, null);
                }
            }
        }

        // Dessiner les pions avec mise à l'échelle
        for (Pion pion : pions) {
            if (pion.getPosition() == null) {
                continue;
            }
            Image img = null;
            switch (pion.getType()) {
                case OR: img = orImg; break;
                case ARGENT: img = argentImg; break;
                default: break;
            }
            if (img != null) {
                double cx = jeu.getCercleDeJeu().getCentre().getX();
                double cy = jeu.getCercleDeJeu().getCentre().getY();
                double rayon = jeu.getCercleDeJeu().getRayon();
                double relX = (pion.getPosition().x - cx) / rayon;
                double relY = (pion.getPosition().y - cy) / rayon;
                int x = (int)(centerX + relX * rayonMax);
                int y = (int)(centerY + relY * rayonMax);
                g.drawImage(img, x - taille/2, y - taille/2, taille, taille, null);
            }
        }
    }
}
