package view;

import global.Configuration;
import global.Coordonnees;
import Model.Jeu;

public class InterfaceTextuelle {
    public void lancer() {
        Configuration.debugeur("Interface textuelle lancée !\n");

        Jeu jeu = new Jeu(50, 30, new Coordonnees(0, 0), 12);
        jeu.afficher();

        jeu.placePion(5, 5);
        jeu.placePion(10, 20);
        jeu.afficher();
    }
}
