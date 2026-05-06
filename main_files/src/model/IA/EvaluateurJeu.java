package model.IA;


// calcule le score heuristique d'un état
public class EvaluateurJeu {

    public int evaluer(EtatJeu etat, int indexIA) {
        int scoreIA = compterFleurs(etat, indexIA);
        int scoreJoueur = compterFleurs(etat, 1 - indexIA);
        return scoreIA - scoreJoueur;
    }

    private int compterFleurs(EtatJeu etat, int joueurIndex) {
        return etat.fleursCollectees[joueurIndex];
    }
}

