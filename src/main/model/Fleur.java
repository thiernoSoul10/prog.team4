package model;

import global.Coordonnees;

public class Fleur {
    private Types.TypeFleur type; // Type de la fleur ne jamais modifié après la création
    private Coordonnees position; // Position de la fleur, peut pas être modifiée après la création

    public Fleur(Types.TypeFleur type, Coordonnees position) {
        this.type = type;
        this.position = position;
    }

    public Types.TypeFleur getTypeFleur() {
        return type;
    }

    public Coordonnees getPosition() {
        return position;
    }

    public char typeFleurToChar() {
        switch (type) {
            case ROUGE:
                return 'R';
            case BLEUE:
                return 'B';
            case JAUNE:
                return 'J';
            case VERTE:
                return 'V';
            case ORANGE:
                return 'O';
            case VIOLETTE:
                return 'P';
            case ROSE:
                return 'S'; // S pour rose pour éviter la confusion avec rouge
            default:
                return 'I';
        }
    }   
}
