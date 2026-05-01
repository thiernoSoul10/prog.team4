package model;




public class Pion {
    private Types.TypePion type;
    private Coordonnees position;

    public Pion(Types.TypePion type, Coordonnees position) {
        this.type = type;
        this.position = position;
    }

    public Types.TypePion getType() {
        return type;
    }

    public Coordonnees getPosition() {
        return position;
    }

    public void setType(Types.TypePion type){
        this.type = type ;
    }
    
    public void setPosition(Coordonnees position){
        this.position = position ;
        
    }

    public char typePionToChar() {
        switch (type) {
            case OR:
                return 'X';
            case ARGENT:
                return 'A';
            default:
                return 'I';
        }
    }
}
