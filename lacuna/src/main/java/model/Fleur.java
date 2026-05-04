/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;



public class Fleur {
    private Types.TypeFleur type; // Type de la fleur 
    private Coordonnees position; // Position de la fleur

    public Fleur(Types.TypeFleur type, Coordonnees position) {
        this.type = type;
        this.position = position;
    }

    public Types.TypeFleur getType() {
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
