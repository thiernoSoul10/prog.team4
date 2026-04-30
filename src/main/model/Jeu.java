package model;

import java.util.ArrayList;
import java.util.Random;

import global.Configuration;


public class Jeu {
    private ArrayList<Pion> pions;
    private Fleur[] fleurs;
    private Random random;
    private int WIDTH = 30; // Largeur de l'aire de jeu pour laffichage dans la console.
    private int HEIGHT = 30; // Hauteur de l'aire de jeu pour laffichage dans la console.
    int currentPlayer = 0; // 0 pour le joueur 1, 1 pour le joueur 2.
    Coordonnees centreCercleDeJeu;
    int RAYON_CERCLE = 30;

    
    // private int[] choixfleur = new int[7]; pour le choix des fleurs au hasard

    public Jeu(int WIDTH, int HEIGHT, Coordonnees cercleDeJeu, int RAYON_CERCLE) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.centreCercleDeJeu = cercleDeJeu;
        this.RAYON_CERCLE = RAYON_CERCLE;
        pions = new ArrayList<>()   ;
        fleurs = new Fleur[49];
        random = new Random();

        int fleursTotales = fleurs.length;
        int fleursRestantes = fleursTotales;
        int couleursFleurs = 0;
        
        // Initialisation des fleurs
        while (fleursRestantes > 0) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            Coordonnees position = new Coordonnees(x, y);

            // Vérifier si la position est déjà occupée par une fleur ou un pion
            // pas besoin de vérifier les pions pour l'instant car ils ne sont pas encore placés
            // Si la position n'est pas occupée, ajouter une fleur
            if (limitesJeu(x, y) && !positionOccupeeParFleur(position)) {
                Types.TypeFleur typeFleur = Types.TypeFleur.values()[couleursFleurs];
                fleurs[fleursTotales - fleursRestantes] = new Fleur(typeFleur, position);
                fleursRestantes--;  
                couleursFleurs++;
                if(couleursFleurs == Types.TypeFleur.values().length) {
                    couleursFleurs = 0;
                }
            }
        }
    }

    private boolean limitesJeu(int x, int y) {
        // à implémenter si nécessaire pour vérifier les limites du jeu
        return (x >= centreCercleDeJeu.x - RAYON_CERCLE) && (x < centreCercleDeJeu.x + RAYON_CERCLE) && (y >= centreCercleDeJeu.y - RAYON_CERCLE) && (y < centreCercleDeJeu.y + RAYON_CERCLE);
    }

    public Fleur[] getFleurs() {
        return fleurs;
    }

    public ArrayList<Pion> getPions() {
        return pions;
    }

    public boolean placePion(int x, int y) {
        if(!limitesJeu(x, y)) return false; // Vérifier les limites du jeu avant de tenter de placer un pion
        Types.TypePion typePion = (currentPlayer == 0) ? Types.TypePion.OR : Types.TypePion.ARGENT;
        
        return ajouterPion(x, y, typePion);
    }

    // affichade du jeu dans la console pour le debuggage
    // complexité reste à désirer, mais c'est pas grave pour l'instant
    public void afficher() {
        Configuration.debugeur("Affichage du jeu : \n#");
        for(int i = 0; i < WIDTH; i++) {
            Configuration.debugeur("-");
        }
        Configuration.debugeur("#\n");

        for(int y = 0; y < HEIGHT; y++) {
            Configuration.debugeur("|");
            for(int x = 0; x < WIDTH; x++) {
                Coordonnees position = new Coordonnees(x, y);
                char symbole = ' ';
                
                // Vérifier si un pion est à cette position
                for (Pion pion : pions) {
                    if (pion.getPosition().equals(position)) {
                        symbole = pion.typePionToChar();
                        break;
                    }
                }

                // Si aucun pion n'est à cette position, vérifier les fleurs
                if (symbole == ' ') {
                    for (Fleur fleur : fleurs) {
                        if (fleur != null && fleur.getPosition().equals(position)) {
                            symbole = fleur.typeFleurToChar();
                            break;
                        }
                    }
                }

                Configuration.debugeur("%c", symbole);
            }
            Configuration.debugeur("|\n");
        }

        Configuration.debugeur("#");
        for(int i = 0; i < WIDTH; i++) {
            Configuration.debugeur("-");
        }
        Configuration.debugeur("#\n");
    }

    //--------------------------------- Fonctions auxiliaires ---------------------------------//
    private boolean positionOccupeeParFleur(Coordonnees position) {
        for (Fleur fleur : fleurs) {
            if (fleur != null && fleur.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    private boolean positionOccupeeParPion(Coordonnees position) {
        for (Pion pion : pions) {
            if (pion.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    private boolean ajouterPion(int x, int y, Types.TypePion typePion) {
        Coordonnees position = new Coordonnees(x, y);
        Pion pion = new Pion(typePion, position);

        Configuration.debugeur("Tentative d'ajout du pion %s à la position (%d, %d)\n", pion.getTypePion(), pion.getPosition().x, pion.getPosition().y);
        
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            if (!positionOccupeeParPion(position) && !positionOccupeeParFleur(position)) {
                pions.add(pion);
                currentPlayer = (currentPlayer + 1) % 2; // Changer de joueur après chaque placement réussi
                return true;
            } else {
                Configuration.debugeur("Impossible d'ajouter le pion à la position (%d, %d) : position déjà occupée.\n", pion.getPosition().x, pion.getPosition().y);
            }
        } else {
            Configuration.debugeur("Impossible d'ajouter le pion à la position (%d, %d) : position en dehors des limites.", pion.getPosition().x, pion.getPosition().y);
        }
        return false;
    }
}
