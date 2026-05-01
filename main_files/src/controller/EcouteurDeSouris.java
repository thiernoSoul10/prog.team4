package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import view.JeuGraphique;
import model.*;
public class EcouteurDeSouris extends MouseAdapter {
        private Jeu game ;
        private JeuGraphique jeuGraphique;

    public EcouteurDeSouris(JeuGraphique jeuGraphique) {
        this.game = jeuGraphique.jeu;
        this.jeuGraphique = jeuGraphique;

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        double nx = (double)x / jeuGraphique.getWidth();
        double ny = (double)y / jeuGraphique.getHeight();

        // récupération cercle
        int cx = game.getCercleDeJeu().getCentre().getX();
        int cy = game.getCercleDeJeu().getCentre().getY();
        int r  = game.getCercleDeJeu().getRayon();

        // conversion des coordonnées normalisées en coordonnées du jeu
        int newX = (int)(cx - r + nx * 2 * r);
        int newY = (int)(cy - r + ny * 2 * r);

        if(this.game.currentPlayerIndex == 0){
            Pion newPion = new Pion(Types.TypePion.OR, new Coordonnees(newX, newY));
            this.game.placePion(newPion, new Coordonnees(newX, newY));
            this.game.getPions().add(newPion);

            this.game.currentPlayerIndex = 1; // Changer de joueur
            this.jeuGraphique.repaint(); // Redessiner le plateau après le placement du pion
        }
        else {
            Pion newPion = new Pion(Types.TypePion.ARGENT, new Coordonnees(newX, newY));
            this.game.placePion(newPion, new Coordonnees(newX, newY));
            this.game.getPions().add(newPion);
            this.game.currentPlayerIndex = 0; // Changer de joueur
            this.jeuGraphique.repaint(); // Redessiner le plateau après le placement du pion
        }
        
    }
}
