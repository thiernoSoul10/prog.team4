package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import global.Configuration;

public class Jeu {
    private Cercle cercleDeJeu; // le cercle du jeu
    private ArrayList<Pion> pions; // contient tous les pions actuel dans le cercle
    private ArrayList<Fleur> fleurs;
    private Fleur fleurSelectionnee1; // la fleur que le joueur a touché ( pour le moment on suppose que c'est la
                                      // fleur la plus proche du point de clic )
    private Fleur fleurSelectionnee2; // la fleur que le joueur a touché ( pour le moment on suppose que c'est la
                                      // fleur la plus proche du point de clic )
    int nbPions = 12; // nombre de pions
    int nbFleurs = 49; // nombre de fleurs

    private static final double DIST_MIN_FLEURS = 30; // distance min entre les fleurs
    private static final int NB_JOUEURS = 2;
    private static final int MAX_PIONS_PAR_JOUEUR = 6;

    //// Attributs pour les joueurs
    private Joueur[] joueurs; // la liste des joueurs
    public int currentPlayerIndex; // 0 pour le joueur 1

    private Random random;


    private int WIDTH = 30;
    private int HEIGHT = 30;

    public boolean againstIA = false;

    ///////// pour la marge (afin que les fleurs ne sortent pas du cercle)//////////
    ////////// vu qu'on dessine les fleurs avec le format dans view (hauteur * largeur )
    ///////// avec hauteur == largeur, alors la view va nous envoyer la taille qu'on divisera par 2
    ///////// voir dans jeuGraphique, juste après rcalcul de la taille, on fera setMarge(taille/2)
    
    private double marge;


    // pour l'undo redo
    public Stack<ActionJeu> undoStack = new Stack<>();
    public Stack<ActionJeu> redoStack = new Stack<ActionJeu>();

    // Avantage du joueur qui commence
    private boolean avantageInitialApplique = false;
    private boolean enPhaseSelectionAvantage = false;

    // Avantage du joueur qui commence
    private boolean avantageInitialApplique = false;
    private boolean enPhaseSelectionAvantage = false;

    ////////// la classe ActionJeu //////////////////
    public static class ActionJeu {// pour l'undo redo
        Pion pion;
        Coordonnees position;
        Fleur f1;
        Fleur f2;
        Joueur joueur;

        public ActionJeu(Pion pion, Fleur f1, Fleur f2, Joueur joueur) {
            this.pion = pion;
            this.position = pion.getPosition();
            this.f1 = f1;
            this.f2 = f2;
            this.joueur = joueur;
        }
    }


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
        demarrerPhaseSelectionAvantage();

        System.out.println("Game created");
    }

    //////////////////////////////////// GETTERS ET SETTERS METHODES
    //////////////////////////////////// /////////////////////////////////////////
    public void setMarge(double marge){
        this.marge = marge;
    }

    public Cercle getCercleDeJeu() {
        return cercleDeJeu;
    }

    public ArrayList<Pion> getPions() {
        return pions;
    }

    public ArrayList<Fleur> getFleurs() {
        return fleurs;
    }

    public int getNbPions() {
        return nbPions;
    }

    public int getNbFleurs() {
        return nbFleurs;
    }

    public int getNbJoueurs() {
        return NB_JOUEURS;
    }

    public Joueur[] getJoueurs() {
        return joueurs;
    }

    public Joueur getJoueurActuel() {
        return joueurs[currentPlayerIndex];
    }

    public void joueurSuivant() {
        currentPlayerIndex = (currentPlayerIndex + 1) % NB_JOUEURS;
    }

    public boolean isAgainstIA() {
        return againstIA;
    }

    public boolean isAvantageInitialApplique() {
        return avantageInitialApplique;
    }

    public boolean isEnPhaseSelectionAvantage() {
        return enPhaseSelectionAvantage;
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

    ////////////////// initialisatin des fleurs et pions////////////////////////////////////
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
            for (int i = 0; i < MAX_PIONS_PAR_JOUEUR; i++) {
                Coordonnees pos = null;
                pions.add(new Pion(type, pos));
            }
        }
    }

    private void demarrerPhaseSelectionAvantage() {
        enPhaseSelectionAvantage = true;
        System.out.println("Phase d'avantage initial: " + getJoueurActuel().getNom() + " doit choisir une fleur");
    }

    public void appliquerAvantageAvecFleur(Fleur fleurChoisie) {
        if (enPhaseSelectionAvantage && !avantageInitialApplique && fleurs.contains(fleurChoisie)) {
            // Donner cette fleur au joueur qui commence
            Joueur joueurCommence = getJoueurActuel();
            joueurCommence.ajouterFleur(fleurChoisie);
            fleurs.remove(fleurChoisie);

            avantageInitialApplique = true;
            enPhaseSelectionAvantage = false;

            System.out.println("Avantage initial: " + joueurCommence.getNom() + " choisit une fleur " + fleurChoisie.getType());
        }
    }

    private void demarrerPhaseSelectionAvantage() {
        enPhaseSelectionAvantage = true;
        System.out.println("Phase d'avantage initial: " + getJoueurActuel().getNom() + " doit choisir une fleur");
    }

    public void appliquerAvantageAvecFleur(Fleur fleurChoisie) {
        if (enPhaseSelectionAvantage && !avantageInitialApplique && fleurs.contains(fleurChoisie)) {
            // Donner cette fleur au joueur qui commence
            Joueur joueurCommence = getJoueurActuel();
            joueurCommence.ajouterFleur(fleurChoisie);
            fleurs.remove(fleurChoisie);

            avantageInitialApplique = true;
            enPhaseSelectionAvantage = false;

            System.out.println("Avantage initial: " + joueurCommence.getNom() + " choisit une fleur " + fleurChoisie.getType());
        }
    }

    // Le nombre de pions par type placés dans le cercle 
    private int nombreDePionsPlacees(Types.TypePion type) {
        int compteur = 0;
        for (Pion p : pions) {
            if (p.getType() == type && p.getPosition() != null) {
                compteur++;
            }
        }
        return compteur;
    }

    // si on peut placer un pion d'un certain type(or ou argent) dans le cercle
    private boolean peutPlacerPionDeType(Types.TypePion type) {
        return nombreDePionsPlacees(type) < MAX_PIONS_PAR_JOUEUR;
    }

    // Génération de position aleatoire pour les fleurs
    private Coordonnees genererPositionAleatoire(ArrayList<Fleur> fleurs) {
        Coordonnees pos;

        do {
            double angle = Math.random() * 2 * Math.PI;
            double margeFleur = marge ; // pour ne pas déborder
            double r = Math.sqrt(Math.random()) * (cercleDeJeu.getRayon()-margeFleur);

            double x = cercleDeJeu.getCentre().getX() + r * Math.cos(angle);
            double y = cercleDeJeu.getCentre().getY() + r * Math.sin(angle);
            pos = new Coordonnees((int) x, (int) y);

        } while (!positionValide(pos, fleurs));

        return pos;
    }

    // Si la position pour mettre les fleurs est valide
    private boolean positionValide(Coordonnees pos, ArrayList<Fleur> fleurs) {
        for (Fleur f : fleurs) {
            if (distance(pos, f.getPosition()) < DIST_MIN_FLEURS) {
                return false;
            }
        }
        return true;
    }

    // dist entre deux points de coordonnes 
    private double distance(Coordonnees a, Coordonnees b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    //////////////// FONCTIONS POUR LES REGLES DU JEU ///////////////
    ///////////// les 3 premieres fonctions sont les principales pour le
    //////////////// jeu///////////

    // l'avantage du premier joueur au tout debut
    public void avantagePremierJoueur() {
        Joueur joueur = getJoueurActuel();

        if (fleurs.isEmpty())
            return; // on va verifier la liste des fleurs

        Fleur f = fleurs.get(random.nextInt(fleurs.size()));
        joueur.ajouterFleur(f);
        fleurs.remove(f);
    }

    // pour placer un pion dans le cercle de jeu
    public boolean placePion(Pion pion, Coordonnees pos) {

        if (!peutPlacerPionDeType(pion.getType())) {
            System.out.println("IMPOSSIBLE DE PLACER PLUS DE " + MAX_PIONS_PAR_JOUEUR + " PIONS DE CE TYPE : " + pion.getType());
            return false;
        }

        if (!cercleDeJeu.contientPoint(pos)) {
            System.out.println("PION HORS DU CERCLE : " + pos.getX() + ", " + pos.getY());
            return false;
        }

        if (!positionLibrePourPion(pos)) {
            System.out.println("POSITION NON APPROPRIEE : " + pos.getX() + ", " + pos.getY());
            return false;
        }

        pion.setPosition(pos);
        return true;
    }

    // pour jouer un coup (controller appelle ça )
    public void jouerCoup(Joueur joueur, Pion pion, Coordonnees pos, Fleur f1, Fleur f2) {

        if (!placePion(pion, pos))
            return;

        if (!mangerFleurs(joueur, f1, f2)) {
            pion.setPosition(null); // on remet la position à null (car si on a passé la premiere condition, la
                                    // position n'est pas null)
            return;
        }

        // si tous les pions sont placés
        if (tousLesPionsPlaces()) {
            attribuerFleursRestantes();// on attribue les fleurs restantes
            return; // fin du jeu
        }

        joueurSuivant();// passons au joueur suivant

    }

    /* ============= fonctions auxiliares ================= */

    // on verifie si la position où on place le pion est libre
    //A REVOIR
    private boolean positionLibrePourPion(Coordonnees pos) {
        // on verifie qu'il y a une distance avec toutes les fleurs
        /*
         * for (Fleur f : fleurs) {
         * if (distance(pos, f.getPosition()) < DIST_MIN_FLEURS) {
         * return false;
         * }
         * }
         */
        // on verifie qu'il y a une distance aussi avec les pions
        for (Pion p : pions) {
            if (p.getPosition() != null && distance(pos, p.getPosition()) < DIST_MIN_FLEURS) {
                return false;
            }
        }

        return true;
    }

    // pour savoir si le joueur mange les fleurs ( mange et renvoie vrai si oui)
    public boolean mangerFleurs(Joueur joueur, Fleur f1, Fleur f2) {
        // on verifie les type d'abord
        if (f1.getType() != f2.getType())
            return false;
        // s'assrere que les fleurs sont dans le plateau (robustesse)
        if (!fleurs.contains(f1) || !fleurs.contains(f2))
            return false;

        // le joueur mange les 2 fleurs
        joueur.ajouterFleur(f1);
        joueur.ajouterFleur(f2);
        // les fleurs ne font plus partie du plateau
        fleurs.remove(f1);
        fleurs.remove(f2);

        return true;
    }

    // attribuer les fleurs restantes ( après avoir joué , il se peut que le joueur
    // suivant ai 2 fleurs)
    // qui sont proche de lui, car des obstacles ont disparus sur le plateau, si on
    // peut dire comme ça )
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

    // le joueur le plus proche
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

        if (meilleur == null)
            return null; // pas de joueur plus proche alors

        for (Joueur j : joueurs) {
            if (j.getTypePion() == meilleur.getType()) {
                return j;
            }
        }

        return null;
    }

    // verifie si tous les pions sont placés (type argent et or )
    private boolean tousLesPionsPlaces() {
        for (Pion p : pions){
            // le pion n'a pas de coordonnée, donc il n'est pas placé dans le plateau de jeu
            if(p.getPosition()==null) return false ;
        }
        return true;
    }

    ////////////////////////////// pour l'affichage
    ////////////////////////////// ////////////////////////////////////////
    public void afficher() {
        Configuration.debugeur("Affichage du jeu : \n#");
        for (int i = 0; i < WIDTH; i++) {
            Configuration.debugeur("-");
        }
        Configuration.debugeur("#\n");

        for (int y = 0; y < HEIGHT; y++) {
            Configuration.debugeur("|");
            for (int x = 0; x < WIDTH; x++) {
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
        for (int i = 0; i < WIDTH; i++) {
            Configuration.debugeur("-");
        }
        Configuration.debugeur("#\n");
    }

    public boolean toucherFleur(Coordonnees pos) {
        for (Fleur f : fleurs) {

            if (distance(pos, f.getPosition()) < 10) {

                // Phase de sélection d'avantage initial
                if (enPhaseSelectionAvantage) {
                    appliquerAvantageAvecFleur(f);
                    return true;
                }

                if (f == fleurSelectionnee1) {
                    fleurSelectionnee1 = null;
                    return true;
                }

                if (f == fleurSelectionnee2) {
                    fleurSelectionnee2 = null;
                    return true;
                }

                if (fleurSelectionnee1 == null) {
                    fleurSelectionnee1 = f;
                    return true;
                }

                if (fleurSelectionnee2 == null) {
                    fleurSelectionnee2 = f;
                    return true;
                }

                fleurSelectionnee1 = f;
                fleurSelectionnee2 = null;

                return true;
            }
        }
        return false;
    }

    private boolean surSegment(Coordonnees A, Coordonnees B, Coordonnees C) {

        int x1 = A.getX();
        int y1 = A.getY();
        int x2 = B.getX();
        int y2 = B.getY();
        int x = C.getX();
        int y = C.getY();

        // 1. Calcul de la distance du point C au segment [AB]
        // det est le double de l'aire du triangle ABC
        long det = Math.abs((long) (x2 - x1) * (y - y1) - (long) (y2 - y1) * (x - x1));
        double distAB = distance(A, B);

        if (distAB == 0)
            return false;

        // Seuil de collision (environ 12 pixels pour le rayon d'une fleur/pion)
        double distanceC_AB = det / distAB;
        if (distanceC_AB > 12)
            return false;

        // 2. Vérification que C est bien entre A et B
        int marge = 5;
        return x >= Math.min(x1, x2) - marge && x <= Math.max(x1, x2) + marge
                && y >= Math.min(y1, y2) - marge && y <= Math.max(y1, y2) + marge;
    }

    private boolean segmentLibre(Coordonnees A, Coordonnees B) {

        // Vérifier les fleurs comme obstacles
        for (Fleur f : fleurs) {

            Coordonnees C = f.getPosition();

            // ignorer les extrémités (les fleurs sélectionnées)
            if (C.equals(A) || C.equals(B))
                continue;

            if (surSegment(A, B, C)) {
                return false; // obstacle trouvé
            }
        }

        // Vérifier les pions comme obstacles
        for (Pion p : pions) {
            Coordonnees C = p.getPosition();
            if (C == null)
                continue;

            if (surSegment(A, B, C)) {
                return false; // un pion bloque le chemin
            }
        }

        return true;
    }

    public boolean fleursConnectées(Fleur f1, Fleur f2) {

        if (f1 == null || f2 == null)
            return false;

        if (f1.getType() != f2.getType())
            return false;

        Coordonnees A = f1.getPosition();
        Coordonnees B = f2.getPosition();

        return segmentLibre(A, B);
    }

    public Coordonnees milieu(Coordonnees A, Coordonnees B) {

        int x = (A.getX() + B.getX()) / 2;
        int y = (A.getY() + B.getY()) / 2;

        return new Coordonnees(x, y);
    }

    public Fleur getFleurSelectionnee1() {
        return fleurSelectionnee1;
    }

    public Fleur getFleurSelectionnee2() {
        return fleurSelectionnee2;
    }

    public void resetFleursSelectionnee1() {
        fleurSelectionnee1 = null;

    }

    public void resetFleursSelectionnee2() {
        fleurSelectionnee2 = null;

    }


    public Map<Types.TypeFleur, Integer> getScore(Joueur joueur) {

        Map<Types.TypeFleur, Integer> map = new java.util.HashMap<>();

        for (Types.TypeFleur t : Types.TypeFleur.values()) {
            map.put(t, 0);
        }

        for (Fleur f : joueur.getFleursGagnees()) {
            map.put(f.getType(), map.get(f.getType()) + 1);
        }

        return map;
    }
    public void undo(){
        if(!this.undoStack.isEmpty()){
            ActionJeu action = this.undoStack.pop();
            // Annuler l'action
            action.pion.setPosition(null); // Retirer le pion du plateau
            if (action.f1 != null) {
                action.joueur.getFleursGagnees().remove(action.f1); // Retirer la fleur du joueur
                fleurs.add(action.f1); // Remettre la fleur sur le plateau
            }
            if (action.f2 != null) {
                action.joueur.getFleursGagnees().remove(action.f2); // Retirer la fleur du joueur
                fleurs.add(action.f2); // Remettre la fleur sur le plateau
            }
            this.redoStack.push(action); // Ajouter l'action annulée à la pile de redo
        }
    }

    public void redo(){
        if(!this.redoStack.isEmpty()){
            ActionJeu action = this.redoStack.pop();
            // Refaire l'action
            if (placePion(action.pion, action.position)) { // Replacer le pion à la position sauvegardée
                if (action.f1 != null) {
                    action.joueur.ajouterFleur(action.f1); // Retirer la fleur du plateau et la donner au joueur
                    fleurs.remove(action.f1);
                }
                if (action.f2 != null) {
                    action.joueur.ajouterFleur(action.f2); // Retirer la fleur du plateau et la donner au joueur
                    fleurs.remove(action.f2);
                }
                this.undoStack.push(action); // Ajouter l'action refaite à la pile d'undo
            } else {
                System.out.println("Impossible de refaire l'action : placement du pion invalide.");
            }
        }
    }

    public void reset() {
        // Réinitialiser le jeu à son état initial
        initPlayers();
        initFleurs();
        initPions();
        demarrerPhaseSelectionAvantage();
        undoStack.clear();
        redoStack.clear();
    }

    /*
     * public Fleur getFleurProche(Coordonnees pos, double distanceMax) {
     * Fleur best = null;
     * double min = Double.MAX_VALUE;
     * 
     * for (Fleur f : fleurs) {
     * double d = distance(pos, f.getPosition());
     * if (d < min && d < distanceMax) {
     * min = d;
     * best = f;
     * }
     * }
     * return best;
     * }
     * 
     * public Fleur getFleurAlignee(Fleur ref) {
     * 
     * for (Fleur f : fleurs) {
     * 
     * if (f == ref)
     * continue;
     * if (f.getType() != ref.getType())
     * continue;
     * 
     * int dx = Math.abs(f.getPosition().getX() - ref.getPosition().getX());
     * int dy = Math.abs(f.getPosition().getY() - ref.getPosition().getY());
     * 
     * // horizontal OU vertical
     * if (dx < 15 || dy < 15) {
     * return f;
     * }
     * }
     * 
     * return null;
     * }
     */
}