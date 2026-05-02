package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import model.ActionJeu;

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

        // Convertir les coordonnées écran en coordonnées modèle
        Coordonnees modelPos = jeuGraphique.screenToModel(x, y);

        System.out.println(
                "Clic écran: (" + x + ", " + y + ") -> Modèle: (" + modelPos.getX() + ", " + modelPos.getY() + ")");

        // vérifie si dans le cercle (en utilisant le modèle)
        if (!game.getCercleDeJeu().contientPoint(modelPos)) {
            System.out.println("Clic en dehors du cercle modèle");
            return;
        }
        if (game.toucherFleur(modelPos)) {
            System.out.println("Clic sur une fleur !");
            jeuGraphique.repaint();

            return;
        }

        
        if (game.fleursConnectées(game.getFleurSelectionnee1(), game.getFleurSelectionnee2())) {

            Coordonnees pos = game.milieu(
                    game.getFleurSelectionnee1().getPosition(),
                    game.getFleurSelectionnee2().getPosition());

            Pion newPion;
            Joueur joueurActuel = this.game.getJoueurs()[this.game.currentPlayerIndex];

            if (this.game.currentPlayerIndex == 0) {
                newPion = new Pion(Types.TypePion.OR, pos);
            } else {
                newPion = new Pion(Types.TypePion.ARGENT, pos);
            }

            if (this.game.placePion(newPion, pos)) {
                this.game.getPions().add(newPion);
                this.game.mangerFleurs(joueurActuel, this.game.getFleurSelectionnee1(), this.game.getFleurSelectionnee2());
                this.game.undoStack.push(new ActionJeu(newPion, this.game.getFleurSelectionnee1(), this.game.getFleurSelectionnee2(), joueurActuel));
                this.game.redoStack.clear();
                this.game.joueurSuivant();

                // reset sélection après coup
                game.resetFleursSelectionnee1();
                game.resetFleursSelectionnee2();
                this.jeuGraphique.repaint();
            } else {
                System.out.println("Placement impossible : limite de pions ou position invalide.");
            }
        }
    }

    /*
     * @Override
     * public void mouseMoved(MouseEvent e) {
     * int x = e.getX();
     * int y = e.getY();
     * 
     * System.out.println("Souris déplacée: (" + x + ", " + y + ")");
     * // Convertir les coordonnées écran en coordonnées modèle
     * Coordonnees modelPos = jeuGraphique.screenToModel(x, y);
     * 
     * 
     * 
     * Fleur f1 = game.getFleurProche(modelPos, 20);
     * 
     * if (f1 == null)
     * return;
     * 
     * Fleur f2 = game.getFleurAlignee(f1);
     * if (f2 != null) {
     * System.out.println("TRAIT POSSIBLE ENTRE " + f1 + " et " + f2);
     * }
     * }
     */
}
