package model.IA;


// Calcule le score heuristique d'un état
public class EvaluateurJeu {

    public int evaluer(EtatJeu etat, int indexIA) {

        // Index du joueur
        int indexJoueur = 1 - indexIA;

        // Calcule des couleurs majoritaires pour IA et joueur
        int couleursIA    = compterCouleursMajoritaires(etat, indexIA);
        int couleursJoueur = compterCouleursMajoritaires(etat, indexJoueur);

        // Calcul du bonus pour IA et joueur
        int bonusIA    = bonus(etat, indexIA, indexJoueur);
        int bonusJoueur = bonus(etat, indexJoueur, indexIA);

        // Formule de calcul du score
        return (couleursIA - couleursJoueur) * 100
                + (bonusIA - bonusJoueur) * 10;
    }

    // Renvoie le nombre de couleurs majoritaires pour un joueur et un état donné
    public int compterCouleursMajoritaires(EtatJeu etat, indexJoueur){
        return 0
    }

    // Renvoie le bonus pour départager deux coups de valeures égales
    public int bonus(EtatJeu etat,int  indexIA, int indexJoueur){
        return 0;
    }


}

// TODO: implémenter bonus et compterCouleursMajoritaires

