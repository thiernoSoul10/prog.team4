/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

        System.out.println(
                "Clic écran: (" + x + ", " + y + ") -> Modèle: (" + modelPos.getX() + ", " + modelPos.getY() + ")");

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
            Pion newPion;

            if (game.currentPlayerIndex == 0) {
                newPion = new Pion(Types.TypePion.OR, pos);
            } else {
                newPion = new Pion(Types.TypePion.ARGENT, pos);
            }

            // Placement du pion
            if (game.placePion(newPion, pos)) {

                game.getPions().add(newPion);

                // ✅ UN SEUL appel ici
                game.mangerFleurs(
                        joueurActuel,
                        game.getFleurSelectionnee1(),
                        game.getFleurSelectionnee2());

                game.undoStack.push(new ActionJeu(
                        newPion,
                        game.getFleurSelectionnee1(),
                        game.getFleurSelectionnee2(),
                        joueurActuel));

                game.redoStack.clear();

                game.joueurSuivant();

                // reset sélection
                game.resetFleursSelectionnee1();
                game.resetFleursSelectionnee2();

                // ✅ MAJ SCORE AU BON MOMENT
                jeuGraphique.refreshScores();

                jeuGraphique.repaint();

            } else {
                System.out.println("Placement impossible : limite de pions ou position invalide.");
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Souris relâchée: (" + e.getX() + ", " + e.getY() + ")");
    }
}
