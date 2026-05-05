/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package global;

import java.io.InputStream;

public final class Configuration {
    private static final String PATH = "/images/";
    private static boolean debug = true; // tous les messages sont affichés si vrai
    private static boolean debugError = true;
    public enum Mode {GRAPHIQUE, TEXTUEL }

    public static Mode mode = Mode.GRAPHIQUE;
    
    public static InputStream ouvre(String path){

        InputStream in =
            Configuration.class.getResourceAsStream(
                PATH + path
            );

        if(in == null){
            debugeurErreur(
                "Impossible de trouver : " + PATH + path
            );
            System.exit(2);
        }

        return in;
    }

    public static InputStream ouvre(String path, String name){
        InputStream in = Configuration.class.getResourceAsStream(
                path + "/" + name);
        
        if(in == null){
            debugeurErreur("ERREUR : impossible de trouver le fichier" + path + "/" + name);
            System.exit(2);
        }

        return in;
    }

    /*
    * 0 pour ne rien afficher
    * 1 pour afficher juste les erreur
    * default tout afficher
    */
    public static void setModeAffichage(int option){
        switch (option) {
            case 0:
                debug = false;
                break;
            case 1:
                debug = false;
                debugError = true;
                break;
            default:
                debug = true;
                break;
        }
    }

    public static void debugeur(String message, Object... args){
        if(debug)
            System.out.printf(message, args);
    }

    public static void debugeurErreur(String message, Object... args){
        if(debug || debugError)
            System.err.printf(message, args);
    }
}
