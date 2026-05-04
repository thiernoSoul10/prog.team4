package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import view.JeuGraphique;
import model.*;
import model.Jeu.ActionJeu;

public class EcouteurDeSouris extends MouseAdapter {
    private Jeu game;
    private JeuGraphique jeuGraphique;

    public EcouteurDeSouris(JeuGraphique jeuGraphique) {
        this.game = jeuGraphique.jeu;
        this.jeuGraphique = jeuGraphique;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        Coordonnees modelPos = jeuGraphique.screenToModel(x, y);


        // Vérifie si clic dans le cercle
        if (!game.getCercleDeJeu().contientPoint(modelPos)) {
            System.out.println("Clic en dehors du cercle modèle");
            return;
        }

        // Sélection de fleur
        if (game.toucherFleur(modelPos)) {
            System.out.println("Clic sur une fleur !");
            jeuGraphique.repaint();
            return;
        }

        // Si deux fleurs sélectionnées et connectées
        if (game.fleursConnectées(
                game.getFleurSelectionnee1(),
                game.getFleurSelectionnee2())) {

            Coordonnees pos = game.milieu(
                    game.getFleurSelectionnee1().getPosition(),
                    game.getFleurSelectionnee2().getPosition());

            Joueur joueurActuel = game.getJoueurActuel();
            Types.TypePion pionType = game.currentPlayerIndex == 0 ? Types.TypePion.OR : Types.TypePion.ARGENT;
            Pion pionLibre = game.getPionLibre(pionType);

            if (pionLibre == null) {
                System.out.println("Aucun pion libre disponible pour ce joueur.");
                return;
            }

            // Placement du pion
            if (game.placePion(pionLibre, pos)) {

                // UN SEUL appel ici
                game.mangerFleurs(
                        joueurActuel,
                        game.getFleurSelectionnee1(),
                        game.getFleurSelectionnee2());

                game.undoStack.push(new ActionJeu(
                        pionLibre,
                        game.getFleurSelectionnee1(),
                        game.getFleurSelectionnee2(),
                        joueurActuel));

                game.redoStack.clear();

                game.joueurSuivant();

                // reset sélection
                game.resetFleursSelectionnee1();
                game.resetFleursSelectionnee2();

             
                //  MAJ SCORE AU BON MOMENT
                jeuGraphique.refreshScores();

                jeuGraphique.repaint();

            } else {
                System.out.println("Placement impossible : limite de pions ou position invalide.");
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      
    }
}