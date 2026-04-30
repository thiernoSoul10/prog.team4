package view;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import global.Configuration;
import global.Coordonnees;
import model.Fleur;
import model.Jeu;
import model.Pion;

public class JeuGraphique extends JComponent {
    private JFrame frame;
    public Jeu jeu;
    private int width = 660, height = 360;
    Coordonnees cercleDeJeu = new Coordonnees(width / 2, height / 2);
    

    // les images des fleurs et des pions seront chargées dans le constructeur de l'interface graphique 
    private Image rougeImg, bleueImg, jauneImg, verteImg, orangeImg, violetteImg, roseImg;
    private Image orImg, argentImg;
    private Image plateauImg;

    public JeuGraphique(JFrame frame) {
        this.frame = frame;
        // width = frame.getWidth();
        //  height = frame.getHeight();

        this.jeu = new Jeu(660, 360, cercleDeJeu, (width > height ? height : width) / 2); // les dimensions du plateau de jeu pour l'affichage graphique à calculer après le chargement de l'image du plateau pour éviter les problèmes de redimensionnement

        setFocusable(true);
        requestFocusInWindow();

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


        jeu.placePion(150, 150);
        jeu.placePion(200, 300);

        frame.add(this);

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
        Fleur[] fleurs = jeu.getFleurs();
        int taille = 40;

        // Facteurs de mise à l'échelle
        double scaleX = getWidth() / 660.0;
        double scaleY = getHeight() / 360.0;

        // Dessiner le plateau de jeu
        g.drawImage(plateauImg, 0, 0, getWidth(), getHeight(), null);

        // Dessiner les fleurs avec mise à l'échelle
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
                    int x = (int)(fleur.getPosition().x * scaleX);
                    int y = (int)(fleur.getPosition().y * scaleY);
                    g.drawImage(img, x, y, taille, taille, null);
                }
            }
        }

        // Dessiner les pions avec mise à l'échelle
        for (Pion pion : pions) {
            Image img = null;
            switch (pion.getTypePion()) {
                case OR: img = orImg; break;
                case ARGENT: img = argentImg; break;
                default: break;
            }
            if (img != null) {
                int x = (int)(pion.getPosition().x * scaleX);
                int y = (int)(pion.getPosition().y * scaleY);
                g.drawImage(img, x, y, taille, taille, null);
            }
        }   
    }
}
