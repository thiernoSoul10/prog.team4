package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import global.Configuration;

public class EcouteurDeClavier extends KeyAdapter {

    

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            
        }

        Configuration.debugeur("Le bouton du clavier a été pressé\n");
    }
}