/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import model.Coordonnees;
import model.Fleur;
import model.Cercle;
import model.Jeu;
import model.Joueur;
import model.Pion;
import model.Types;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author thsba
 */
public class Tests {
    
    public Tests() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    void placePionValide(){
        // TODO
        Jeu jeu = new Jeu(30, 30, new Cercle(new Coordonnees(15, 15), 15));
        Pion pion = new Pion(Types.TypePion.OR, null);

        boolean resultat = jeu.placePion(pion, new Coordonnees(15, 15));

        assertTrue(resultat);
        assertEquals(new Coordonnees(15, 15), pion.getPosition());
    }

    @Test
    void placePionInvalide(){
        Jeu jeu = new Jeu(30, 30, new Cercle(new Coordonnees(15, 15), 10));
        Pion pion = new Pion(Types.TypePion.OR, null);

        boolean resultat = jeu.placePion(pion, new Coordonnees(30, 30));

        assertFalse(resultat);
        assertNull(pion.getPosition());
    }

    @Test
    void mangerFleurs(){
        // TODO
        Jeu jeu = new Jeu(30, 30, new Cercle(new Coordonnees(15, 15), 15));
        jeu.getFleurs().clear();

        Fleur f1 = new Fleur(Types.TypeFleur.ROUGE, new Coordonnees(10, 10));
        Fleur f2 = new Fleur(Types.TypeFleur.JAUNE, new Coordonnees(20, 20));
        jeu.getFleurs().add(f1);
        jeu.getFleurs().add(f2);

        Joueur joueur = jeu.getJoueurs()[0];

        assertTrue(jeu.mangerFleurs(joueur, f1, f2));
        assertFalse(jeu.getFleurs().contains(f1));
        assertFalse(jeu.getFleurs().contains(f2));
        assertEquals(2, joueur.getScoreTotal());
    }

    @Test
    void joueurSuivant(){
        // TODO
        Jeu jeu = new Jeu(30, 30, new Cercle(new Coordonnees(15, 15), 15));
        int courant = jeu.currentPlayerIndex;
        jeu.joueurSuivant();
        assertEquals((courant + 1) % jeu.getNbJoueurs(), jeu.currentPlayerIndex);
    }
}
