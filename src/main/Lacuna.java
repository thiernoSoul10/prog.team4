import javax.swing.SwingUtilities;

import global.Configuration;
import view.InterfaceGraphique;
import view.InterfaceTextuelle;

public class Lacuna {

    public static void main(String[] args) {

        if(args.length == 1)
            switch (args[0]) {
                case "graphique":
                    Configuration.mode = Configuration.Mode.GRAPHIQUE;
                    break;
                case "textuel":
                    Configuration.mode = Configuration.Mode.TEXTUEL;
                    break;
                default:
                    break;
            };


        switch (Configuration.mode) {
            case TEXTUEL:
                InterfaceTextuelle inter = new InterfaceTextuelle();
                inter.lancer();
                break;
            // Swing s'exécute dans un thread séparé. En aucun cas il ne faut accéder directement
            // aux composants graphiques depuis le thread principal. Swing fournit la méthode
            // invokeLater pour demander au thread de Swing d'exécuter la méthode run d'un Runnable.
            default:
                SwingUtilities.invokeLater(new InterfaceGraphique());
                break;
        }
    }
}
