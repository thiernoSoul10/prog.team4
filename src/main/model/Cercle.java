package model;

public class Cercle {
    
   private  Coordonnees centre;
    private int rayon;

    //CONSTRUCTEUR////

    public Cercle(Coordonnees centre, int rayon ){
        this.Coordonnees = Coordonnees;
        this.rayon = rayon ;
    }

    ////////////////////////////////////////

    ////////// GETTER(S) AND SETTER(S) METHODS ///////////////

    //======= GETTER(S)===========/
    public Coordonnees getCentre(){
        return centre;
    }
   
    public int getRayon(){
        return rayon;
    }

    //======= SETTER(S)===========/
    public void setCentre(Coordonnees centre){
        this.centre = centre;
    }

    public void setRayon(int rayon){
        this.rayon = rayon ;
    }


     ////////// AUTRES  METHODES ///////////////
     
    /*les methodes aire et perimetre sont ajoutées car vu qu'on est sur un objet de  type cercle,
        c'est les infos de base à savoir (peut être que ça nous aidera plus tard aussi ) */
    
    public double aire() {
        return Math.PI * rayon * rayon;
    }

    public double perimetre() {
        return 2 * Math.PI * rayon;
    }

    //connaitre la distance d'un point par rapport au centre 
    public double distanceAuCentre(Coordonnees p){
        int x = p.get
    }

    public boolean contientPoint(Coordonnees p){

    }


    




}