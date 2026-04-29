package Controleur;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Global.Configuration;
import Global.Coordonnees;

public class EcouteurDeClavier extends KeyAdapter {

    

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            
        }

        Configuration.debugeur("Le bouton du clavier a été pressé\n");
    }
}