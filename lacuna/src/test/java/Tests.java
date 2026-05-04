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

    // Tests pour Coordonnees
    @Test
    public void testCoordonneesConstructor() {
        Coordonnees c = new Coordonnees(5, 10);
        assertEquals(5, c.getX());
        assertEquals(10, c.getY());
    }

    @Test
    public void testCoordonneesSetters() {
        Coordonnees c = new Coordonnees(0, 0);
        c.setX(3);
        c.setY(7);
        assertEquals(3, c.getX());
        assertEquals(7, c.getY());
    }

    @Test
    public void testCoordonneesEquals() {
        Coordonnees c1 = new Coordonnees(1, 2);
        Coordonnees c2 = new Coordonnees(1, 2);
        Coordonnees c3 = new Coordonnees(2, 1);
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(null));
        assertFalse(c1.equals("string"));
    }

    @Test
    public void testCoordonneesHashCode() {
        Coordonnees c1 = new Coordonnees(1, 2);
        Coordonnees c2 = new Coordonnees(1, 2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void testCoordonneesCompareTo() {
        Coordonnees c1 = new Coordonnees(1, 2);
        Coordonnees c2 = new Coordonnees(1, 2);
        Coordonnees c3 = new Coordonnees(2, 1);
        Coordonnees c4 = new Coordonnees(1, 3);
        assertEquals(0, c1.compareTo(c2));
        assertTrue(c1.compareTo(c3) < 0);
        assertTrue(c1.compareTo(c4) < 0);
        assertTrue(c3.compareTo(c1) > 0);
    }

    // Tests pour Cercle
    @Test
    public void testCercleConstructor() {
        Coordonnees centre = new Coordonnees(0, 0);
        Cercle c = new Cercle(centre, 5);
        assertEquals(centre, c.getCentre());
        assertEquals(5, c.getRayon());
    }

    @Test
    public void testCercleSetters() {
        Coordonnees centre1 = new Coordonnees(0, 0);
        Coordonnees centre2 = new Coordonnees(1, 1);
        Cercle c = new Cercle(centre1, 5);
        c.setCentre(centre2);
        c.setRayon(10);
        assertEquals(centre2, c.getCentre());
        assertEquals(10, c.getRayon());
    }

    @Test
    public void testCercleAire() {
        Coordonnees centre = new Coordonnees(0, 0);
        Cercle c = new Cercle(centre, 2);
        double expectedAire = Math.PI * 4;
        assertEquals(expectedAire, c.aire(), 0.001);
    }

    @Test
    public void testCerclePerimetre() {
        Coordonnees centre = new Coordonnees(0, 0);
        Cercle c = new Cercle(centre, 3);
        double expectedPerimetre = 2 * Math.PI * 3;
        assertEquals(expectedPerimetre, c.perimetre(), 0.001);
    }

    @Test
    public void testCercleDistanceAuCentre() {
        Coordonnees centre = new Coordonnees(0, 0);
        Cercle c = new Cercle(centre, 5);
        Coordonnees p = new Coordonnees(3, 4);
        assertEquals(5.0, c.distanceAuCentre(p), 0.001);
    }

    @Test
    public void testCercleContientPoint() {
        Coordonnees centre = new Coordonnees(0, 0);
        Cercle c = new Cercle(centre, 5);
        Coordonnees inside = new Coordonnees(3, 4);
        Coordonnees onBoundary = new Coordonnees(5, 0);
        Coordonnees outside = new Coordonnees(6, 0);
        assertTrue(c.contientPoint(inside));
        assertTrue(c.contientPoint(onBoundary)); // <= so boundary is included
        assertFalse(c.contientPoint(outside));
    }

    @Test
    public void testCercleCopie() {
        Coordonnees centre = new Coordonnees(1, 2);
        Cercle original = new Cercle(centre, 10);
        Cercle copie = original.copie();
        assertEquals(original.getCentre(), copie.getCentre());
        assertEquals(original.getRayon(), copie.getRayon());
        assertNotSame(original, copie); // different objects
    }

    // Tests pour Fleur
    @Test
    public void testFleurConstructor() {
        Coordonnees pos = new Coordonnees(1, 1);
        Fleur f = new Fleur(Types.TypeFleur.ROUGE, pos);
        assertEquals(Types.TypeFleur.ROUGE, f.getType());
        assertEquals(pos, f.getPosition());
    }

    @Test
    public void testFleurTypeFleurToChar() {
        Coordonnees pos = new Coordonnees(0, 0);
        assertEquals('R', new Fleur(Types.TypeFleur.ROUGE, pos).typeFleurToChar());
        assertEquals('B', new Fleur(Types.TypeFleur.BLEUE, pos).typeFleurToChar());
        assertEquals('J', new Fleur(Types.TypeFleur.JAUNE, pos).typeFleurToChar());
        assertEquals('V', new Fleur(Types.TypeFleur.VERTE, pos).typeFleurToChar());
        assertEquals('O', new Fleur(Types.TypeFleur.ORANGE, pos).typeFleurToChar());
        assertEquals('P', new Fleur(Types.TypeFleur.VIOLETTE, pos).typeFleurToChar());
        assertEquals('S', new Fleur(Types.TypeFleur.ROSE, pos).typeFleurToChar());
    }

    // Tests pour Pion
    @Test
    public void testPionConstructor() {
        Coordonnees pos = new Coordonnees(2, 3);
        Pion p = new Pion(Types.TypePion.OR, pos);
        assertEquals(Types.TypePion.OR, p.getType());
        assertEquals(pos, p.getPosition());
    }

    @Test
    public void testPionSetters() {
        Coordonnees pos1 = new Coordonnees(0, 0);
        Coordonnees pos2 = new Coordonnees(5, 5);
        Pion p = new Pion(Types.TypePion.ARGENT, pos1);
        p.setType(Types.TypePion.OR);
        p.setPosition(pos2);
        assertEquals(Types.TypePion.OR, p.getType());
        assertEquals(pos2, p.getPosition());
    }

    @Test
    public void testPionTypePionToChar() {
        Coordonnees pos = new Coordonnees(0, 0);
        assertEquals('X', new Pion(Types.TypePion.OR, pos).typePionToChar());
        assertEquals('A', new Pion(Types.TypePion.ARGENT, pos).typePionToChar());
    }

    // Tests pour Joueur
    @Test
    public void testJoueurConstructor() {
        JoueurHumain j = new JoueurHumain("Alice", Types.TypePion.OR);
        assertEquals("Alice", j.getNom());
        assertEquals(Types.TypePion.OR, j.getTypePion());
        assertNotNull(j.getFleursGagnees());
        assertEquals(0, j.getFleursGagnees().size());
    }

    @Test
    public void testJoueurAjouterFleur() {
        JoueurHumain j = new JoueurHumain("Bob", Types.TypePion.ARGENT);
        Fleur f1 = new Fleur(Types.TypeFleur.ROUGE, new Coordonnees(0, 0));
        Fleur f2 = new Fleur(Types.TypeFleur.BLEUE, new Coordonnees(1, 1));
        j.ajouterFleur(f1);
        j.ajouterFleur(f2);
        assertEquals(2, j.getFleursGagnees().size());
        assertEquals(f1, j.getFleursGagnees().get(0));
        assertEquals(f2, j.getFleursGagnees().get(1));
    }

    @Test
    public void testJoueurGetScoreTotal() {
        JoueurIa j = new JoueurIa("IA", Types.TypePion.OR);
        assertEquals(0, j.getScoreTotal());
        j.ajouterFleur(new Fleur(Types.TypeFleur.JAUNE, new Coordonnees(0, 0)));
        assertEquals(1, j.getScoreTotal());
    }

    @Test
    public void testJoueurNbFleursDeCouleur() {
        JoueurHumain j = new JoueurHumain("Charlie", Types.TypePion.ARGENT);
        j.ajouterFleur(new Fleur(Types.TypeFleur.ROUGE, new Coordonnees(0, 0)));
        j.ajouterFleur(new Fleur(Types.TypeFleur.ROUGE, new Coordonnees(1, 1)));
        j.ajouterFleur(new Fleur(Types.TypeFleur.BLEUE, new Coordonnees(2, 2)));
        assertEquals(2, j.nbFleursDeCouleur(Types.TypeFleur.ROUGE));
        assertEquals(1, j.nbFleursDeCouleur(Types.TypeFleur.BLEUE));
    // Tests pour Jeu
    @Test
    public void testJeuConstructor() {
        Cercle cercle = new Cercle(new Coordonnees(0, 0), 100);
        Jeu jeu = new Jeu(800, 600, cercle);
        assertEquals(cercle, jeu.getCercleDeJeu());
        assertNotNull(jeu.getPions());
        assertNotNull(jeu.getFleurs());
        assertEquals(2, jeu.getNbJoueurs());
        assertNotNull(jeu.getJoueurs());
        assertEquals(0, jeu.getJoueurActuel().getScoreTotal());
    }

    @Test
    public void testJeuJoueurSuivant() {
        Cercle cercle = new Cercle(new Coordonnees(0, 0), 100);
        Jeu jeu = new Jeu(800, 600, cercle);
        Joueur premier = jeu.getJoueurActuel();
        jeu.joueurSuivant();
        Joueur deuxieme = jeu.getJoueurActuel();
        assertNotEquals(premier, deuxieme);
        jeu.joueurSuivant();
        assertEquals(premier, jeu.getJoueurActuel());
    }

    @Test
    public void testJeuInitFleurs() {
        Cercle cercle = new Cercle(new Coordonnees(0, 0), 100);
        Jeu jeu = new Jeu(800, 600, cercle);
        jeu.initFleurs();
        assertEquals(49, jeu.getFleurs().size()); // 7 types * 7 fleurs
        // Check that all types are present
        int[] counts = new int[Types.TypeFleur.values().length];
        for (Fleur f : jeu.getFleurs()) {
            counts[f.getType().ordinal()]++;
        }
        for (int count : counts) {
            assertEquals(7, count);
        }
    }

    @Test
    public void testJeuInitPions() {
        Cercle cercle = new Cercle(new Coordonnees(0, 0), 100);
        Jeu jeu = new Jeu(800, 600, cercle);
        jeu.initPions();
        assertEquals(12, jeu.getPions().size()); // 2 types * 6 pions
        int orCount = 0, argentCount = 0;
        for (Pion p : jeu.getPions()) {
            if (p.getType() == Types.TypePion.OR) orCount++;
            else if (p.getType() == Types.TypePion.ARGENT) argentCount++;
        }
        assertEquals(6, orCount);
        assertEquals(6, argentCount);
    }
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
