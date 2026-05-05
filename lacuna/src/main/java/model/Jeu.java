package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.List;
import java.util.Collections;

import global.Configuration;

public class Jeu {
    private Cercle cercleDeJeu;
    private ArrayList<Pion> pions;
    private ArrayList<Fleur> fleurs;
    private Fleur fleurSelectionnee1;
    private Fleur fleurSelectionnee2;
    int nbPions = 12;
    int nbFleurs = 49;

    private static final double DIST_MIN_FLEURS = 30;
    private static final int NB_JOUEURS = 2;
    private static final int MAX_PIONS_PAR_JOUEUR = 6;

    private Joueur[] joueurs;
    public int currentPlayerIndex;

    private Random random;

    private int WIDTH = 30;
    private int HEIGHT = 30;

    public boolean againstIA = false;

    private double marge;

    public Stack<ActionJeu> undoStack = new Stack<>();
    public Stack<ActionJeu> redoStack = new Stack<>();

    private boolean avantageInitialApplique = false;
    private boolean enPhaseSelectionAvantage = false;

    public static class ActionJeu {
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
            Configuration.debugeur("Game created in IA mode");
        } else {
            Configuration.debugeur("Game created in MULTI mode");
        }

        this.cercleDeJeu = cercleDeJeu;
        pions = new ArrayList<>();
        fleurs = new ArrayList<>();

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        random = new Random();

        Configuration.debugeur("Initialisation des Joueurs\n");
        initPlayers();
        
        Configuration.debugeur("Initialisation des Fleurs\n");
        initFleurs();
        
        Configuration.debugeur("Initialisation des Pions\n");
        initPions();
        
        Configuration.debugeur("Demarage Phase de Selection\n");
        demarrerPhaseSelectionAvantage();

        Configuration.debugeur("Game created\n");
    }

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

    // ------------------------------------------------------------
    //   *** INIT FLEURS — VERSION DEMANDÉE (shuffle + placement) ***
    // ------------------------------------------------------------
    public void initFleurs() {
        Configuration.debugeur("Suppression des Fleurs\n");
        fleurs = new ArrayList<>();

        Configuration.debugeur("Initialisation\n");

        List<Fleur> nouvelles = new ArrayList<>();

        // 1. Générer toutes les fleurs
        for (Types.TypeFleur type : Types.TypeFleur.values()) {
            for (int i = 0; i < 7; i++) {
                nouvelles.add(new Fleur(type));
            }
        }

        // 2. Mélanger pour éviter les patterns
        Collections.shuffle(nouvelles);
        Configuration.debugeur("Melange\n");


        // 3. Assigner une position unique à chaque fleur
        for (Fleur f : nouvelles) {
            Coordonnees pos = genererPositionAleatoire(fleurs);
            f.setPosition(pos);
            fleurs.add(f);
        }
        Configuration.debugeur("Initialisation Terminée");
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
        Configuration.debugeur("Phase d'avantage initial: " + getJoueurActuel().getNom() + " doit choisir une fleur");
    }

    public void appliquerAvantageAvecFleur(Fleur fleurChoisie) {
        if (enPhaseSelectionAvantage && !avantageInitialApplique && fleurs.contains(fleurChoisie)) {
            Joueur joueurCommence = getJoueurActuel();
            joueurCommence.ajouterFleur(fleurChoisie);
            fleurs.remove(fleurChoisie);

            avantageInitialApplique = true;
            enPhaseSelectionAvantage = false;

            Configuration.debugeur("Avantage initial: " + joueurCommence.getNom() + " choisit une fleur " + fleurChoisie.getType());
        }
    }

    private int nombreDePionsPlacees(Types.TypePion type) {
        int compteur = 0;
        for (Pion p : pions) {
            if (p.getType() == type && p.getPosition() != null) {
                compteur++;
            }
        }
        return compteur;
    }

    private boolean peutPlacerPionDeType(Types.TypePion type) {
        return nombreDePionsPlacees(type) < MAX_PIONS_PAR_JOUEUR;
    }

    private Coordonnees genererPositionAleatoire(ArrayList<Fleur> fleurs) {
        Coordonnees pos;

        do {
            double angle = Math.random() * 2 * Math.PI;
            double margeFleur = marge;
            double r = Math.sqrt(Math.random()) * (cercleDeJeu.getRayon() - margeFleur);

            double x = cercleDeJeu.getCentre().getX() + r * Math.cos(angle);
            double y = cercleDeJeu.getCentre().getY() + r * Math.sin(angle);
            pos = new Coordonnees((int) x, (int) y);

        } while (!positionValide(pos, fleurs));

        return pos;
    }

    private boolean positionValide(Coordonnees pos, ArrayList<Fleur> fleurs) {
        for (Fleur f : fleurs) {
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

    public void avantagePremierJoueur() {
        Joueur joueur = getJoueurActuel();

        if (fleurs.isEmpty())
            return;

        Fleur f = fleurs.get(random.nextInt(fleurs.size()));
        joueur.ajouterFleur(f);
        fleurs.remove(f);
    }

    public boolean placePion(Pion pion, Coordonnees pos) {

        if (!peutPlacerPionDeType(pion.getType())) {
            Configuration.debugeur("IMPOSSIBLE DE PLACER PLUS DE " + MAX_PIONS_PAR_JOUEUR + " PIONS DE CE TYPE : " + pion.getType());
            return false;
        }

        if (!cercleDeJeu.contientPoint(pos)) {
            Configuration.debugeur("PION HORS DU CERCLE : " + pos.getX() + ", " + pos.getY());
            return false;
        }

        if (!positionLibrePourPion(pos)) {
            Configuration.debugeur("POSITION NON APPROPRIEE : " + pos.getX() + ", " + pos.getY());
            return false;
        }

        pion.setPosition(pos);
        return true;
    }

    public void jouerCoup(Joueur joueur, Pion pion, Coordonnees pos, Fleur f1, Fleur f2) {

        if (!placePion(pion, pos))
            return;

        if (!mangerFleurs(joueur, f1, f2)) {
            pion.setPosition(null);
            return;
        }

        if (tousLesPionsPlaces()) {
            attribuerFleursRestantes();
            return;
        }

        joueurSuivant();
    }

    private boolean positionLibrePourPion(Coordonnees pos) {
        for (Pion p : pions) {
            if (p.getPosition() != null && distance(pos, p.getPosition()) < DIST_MIN_FLEURS) {
                return false;
            }
        }

        return true;
    }

    public boolean mangerFleurs(Joueur joueur, Fleur f1, Fleur f2) {
        if (f1.getType() != f2.getType())
            return false;

        if (!fleurs.contains(f1) || !fleurs.contains(f2))
            return false;

        joueur.ajouterFleur(f1);
        joueur.ajouterFleur(f2);

        fleurs.remove(f1);
        fleurs.remove(f2);

        return true;
    }

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
            return null;

        for (Joueur j : joueurs) {
            if (j.getTypePion() == meilleur.getType()) {
                return j;
            }
        }

        return null;
    }

    private boolean tousLesPionsPlaces() {
        for (Pion p : pions){
            if(p.getPosition()==null) return false;
        }
        return true;
    }

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

        long det = Math.abs((long) (x2 - x1) * (y - y1) - (long) (y2 - y1) * (x - x1));
        double distAB = distance(A, B);

        if (distAB == 0)
            return false;

        double distanceC_AB = det / distAB;
        if (distanceC_AB > 12)
            return false;

        int marge = 5;
        return x >= Math.min(x1, x2) - marge && x <= Math.max(x1, x2) + marge
                && y >= Math.min(y1, y2) - marge && y <= Math.max(y1, y2) + marge;
    }

    private boolean segmentLibre(Coordonnees A, Coordonnees B) {

        for (Fleur f : fleurs) {

            Coordonnees C = f.getPosition();

            if (C.equals(A) || C.equals(B))
                continue;

            if (surSegment(A, B, C)) {
                return false;
            }
        }

        for (Pion p : pions) {
            Coordonnees C = p.getPosition();
            if (C == null)
                continue;

            if (surSegment(A, B, C)) {
                return false;
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
            action.pion.setPosition(null);
            if (action.f1 != null) {
                action.joueur.getFleursGagnees().remove(action.f1);
                fleurs.add(action.f1);
            }
            if (action.f2 != null) {
                action.joueur.getFleursGagnees().remove(action.f2);
                fleurs.add(action.f2);
            }
            this.redoStack.push(action);
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