package model;

import java.util.ArrayList;
import java.util.Random;

import global.Configuration;

public class Jeu {
    private Cercle cercleDeJeu ;  // le cercle du jeu
    private ArrayList<Pion> pions; //contient tous les pions actuel  dans le cercle
    private ArrayList<Fleur> fleurs; // contient toutes les fleurs dans le jeu

    int nbPions = 12 ; // nombre de pions
    int nbFleurs = 49 ; // nombre de fleurs 

    private static final double DIST_MIN_FLEURS = 20; // distance min entre les fleurs 
    private static final int  NB_JOUEURS = 2; // distance min entre les fleurs 
    

    //// Attributs pour les joueurs
    private Joueur[] joueurs; // la liste des joueurs 
    int currentPlayerIndex; // 0 pour le joueur 1 
    

    private Random random;

    private int WIDTH = 30;
    private int HEIGHT = 30;

    public boolean againstIA = false;

   ////// CONSTRUCTEUR //////////////////////////////

    public Jeu(int WIDTH, int HEIGHT, Cercle cercleDeJeu) {

        if (isAgainstIA()) {
            System.out.println("Game created in IA mode");
        } else {
            System.out.println("Game created in MULTI mode");
        }

        this.cercleDeJeu = cercleDeJeu;
        pions = new ArrayList<>();
        fleurs = new ArrayList<>();

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    
        random = new Random();

        initPlayers();
        initFleurs();
        initPions();

        System.out.println("Game created");
    }

    ////////////////////////////////////GETTERS ET SETTERS METHODES /////////////////////////////////////////

    public Cercle getCercleDeJeu() {
        return cercleDeJeu;
    }

    public ArrayList<Pion> getPions() {
        return pions;
    }

    public ArrayList<Fleur> getFleurs() {
        return fleurs;
    }
    public int getNbPions(){
        return nbPions;
    }

    public int getNbFleurs(){
        return nbFleurs;
    }

    public int getNbJoueurs(){
        return NB_JOUEURS;
    }

    public Joueur[] getJoueurs(){
        return joueurs;
    }

    public Joueur getJoueurActuel(){
        return joueurs[currentPlayerIndex];
    }

    public void joueurSuivant() {
        currentPlayerIndex = (currentPlayerIndex + 1) % NB_JOUEURS;
    }

    public boolean isAgainstIA(){
        return againstIA;
    }

    private void initPlayers() {
        if (isAgainstIA()) {
            joueurs = new Joueur[] {
                new JoueurHumain("HUMAN", Types.TypePion.OR),
                new JoueurIa("IA", Types.TypePion.ARGENT)
            };
            currentPlayerIndex = 0;
        } else {
            joueurs = new Joueur[] {
                new JoueurHumain("Joueur 1", Types.TypePion.OR),
                new JoueurHumain("Joueur 2", Types.TypePion.ARGENT)
            };
            currentPlayerIndex = random.nextInt(getNbJoueurs());
        }
    }

    //////////////////initialisatin des fleurs////////////////////////////////////
    public void initFleurs() {
        fleurs.clear();

        for (Types.TypeFleur type : Types.TypeFleur.values()) {
            for (int i = 0; i < 7; i++) {
                Coordonnees pos = genererPositionAleatoire(fleurs);
                fleurs.add(new Fleur(type, pos));
            }
        }
    }
    
    public void initPions() {
        pions.clear();

        for (Types.TypePion type : Types.TypePion.values()) {
            for (int i = 0; i < 6; i++) {
                Coordonnees pos = null;
                pions.add(new Pion(type, pos));
            }
        }
    }

    private Coordonnees genererPositionAleatoire(ArrayList<Fleur> fleurs) {
        Coordonnees pos;

        do {
            double angle = Math.random() * 2 * Math.PI;
            double r = Math.sqrt(Math.random()) * cercleDeJeu.getRayon();

            double x = cercleDeJeu.getCentre().getX() + r * Math.cos(angle);
            double y = cercleDeJeu.getCentre().getY() + r * Math.sin(angle);

            pos = new Coordonnees(x, y);

        } while (!positionValide(pos, fleurs));

        return pos;
    }

    private boolean positionValide(Coordonnees pos, ArrayList<Fleur> fleurs) {
        for(Fleur f : fleurs) {
            if (distance(pos, f.getPosition()) < DIST_MIN_FLEURS) {
                return false;
            }
        }
        return true;
    }

    private double distance(Coordonnees a, Coordonnees b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    //////////////// FONCTIONS POUR LES REGLES DU JEU ///////////////
    ///////////// les 3 premieres fonctions sont les principales pour le jeu///////////

    // l'avantage du premier joueur au tout debut
    public void avantagePremierJoueur() {
        Joueur joueur = getJoueurActuel();

        if (fleurs.isEmpty()) return; //on va verifier la liste des fleurs 

        Fleur f = fleurs.get(random.nextInt(fleurs.size()));
        joueur.ajouterFleur(f);
        fleurs.remove(f);
    }

     // pour placer un pion dans le cercle de jeu 
    public boolean placePion(Pion pion, Coordonnees pos) {

        if (!cercleDeJeu.contient(pos)) {
            System.out.println("PION HORS DU CERCLE : " + pos.getX() + ", " + pos.getY());
            return false;
        }

        if (!positionLibrePourPion(pos)) {
            System.out.println("POSITION OCCUPEE");
            return false;
        }

        pion.setPosition(pos);
        return true;
    }

    //pour jouer un coup (controller appelle ça )
    public void jouerCoup(Joueur joueur, Pion pion, Coordonnees pos, Fleur f1, Fleur f2){
        
        if (!placePion(pion, pos)) return ;
        
        if (!mangerFleurs(joueur, f1, f2)) {
            pion.setPosition(null); //on remet la position à null (car si on a passé la premiere condition, la position n'est pas null)
            return ;
        }
        
        //si tous les pions sont placés
        if(tousLesPionsPlaces()){
             attribuerFleursRestantes();// on attribue les fleurs restantes 
             return ; //fin du jeu
        }
        
         joueurSuivant();// passons au joueur suivant
       
    }


    /*============= fonctions auxiliares =================*/


    // on verifie si la position où on place le pion est libre
    private boolean positionLibrePourPion(Coordonnees pos) {
        //on verifie qu'il y a une distance avec toutes les fleurs
        for (Fleur f : fleurs) {
            if (distance(pos, f.getPosition()) < DIST_MIN_FLEURS) {
                return false;
            }
        }
        //on verifie qu'il y a une distance aussi avec les pions
        for (Pion p : pions) {
            if (p.getPosition() != null && distance(pos, p.getPosition()) < DIST_MIN_FLEURS) {
                return false;
            }
        }

        return true;
    }

   
    // pour savoir si le joueur mange les fleurs  ( mange et renvoie vrai si oui) 
    public boolean mangerFleurs(Joueur joueur, Fleur f1, Fleur f2) {
        //on verifie les type d'abord
        if (f1.getType() != f2.getType()) return false; 
        // s'assrere que les fleurs sont dans le plateau (robustesse)
        if (!fleurs.contains(f1) || !fleurs.contains(f2)) return false;
        
        //le joueur mange les 2 fleurs
        joueur.ajouterFleur(f1);
        joueur.ajouterFleur(f2);
        // les fleurs ne font plus partie du plateau 
        fleurs.remove(f1);
        fleurs.remove(f2);

        return true;
    }

    

    //attribuer les fleurs restantes ( après avoir jouer , il se peut que le joueur suivant ai 2 fleurs)
    // qui sont proche de lui, car des obstacles ont disparus sur le plateau, si on peut dire comme ça )
    public void attribuerFleursRestantes() {

        ArrayList<Fleur> copie = new ArrayList<>(fleurs);

        for (Fleur f : copie) {
            Joueur j = joueurLePlusProche(f);
            if (j != null) {
                j.ajouterFleur(f);
                fleurs.remove(f);
            }
        }
    }

    //le joueur le plus proche 
    private Joueur joueurLePlusProche(Fleur fleur) {

        Pion meilleur = null;
        double min = Double.MAX_VALUE;
        for (Pion p : pions) {
            if (p.getPosition() != null) {
                double d = distance(fleur.getPosition(), p.getPosition());
                if (d < min) {
                    min = d;
                    meilleur = p;
                }
            }
        }

        if (meilleur == null) return null; // pas de joueur plus proche alors

        for (Joueur j : joueurs) {
            if (j.getTypePion() == meilleur.getType()) {
                return j;
            }
        }

        return null;
    }

    private boolean tousLesPionsPlaces(){
        return pions.size() == 0; 
    }

    //////////////////////////////pour l'affichage ////////////////////////////////////////
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
                
                for (Pion pion : pions) {
                    if (pion.getPosition() != null && pion.getPosition().equals(position)) {
                        symbole = pion.typePionToChar();
                        break;
                    }
                }

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
}