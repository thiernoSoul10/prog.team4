package global;

public class Coordonnees implements Comparable<Coordonnees> {
    public int x;
    public int y;

    public Coordonnees(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /////////GETTERS ET SETTERS METHODES////////////
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }
    /////////////////////

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordonnees that = (Coordonnees) obj;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

    @Override
    public int compareTo(Coordonnees arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
}
