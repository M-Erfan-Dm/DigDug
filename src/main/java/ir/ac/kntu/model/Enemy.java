package ir.ac.kntu.model;

public abstract class Enemy extends GameObject implements Movable{
    private int health = 4;

    public Enemy(Map map, int gridX, int gridY) {
        super(map, gridX, gridY);
    }

    public void hit(){
    }
}
