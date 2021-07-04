package ir.ac.kntu.model;

public class Fygar extends Enemy {
    public Fygar(Map map, int x, int y) {
        super(map, x, y);
        setImage("src/main/resources/assets/gun.png");
        updateRealPos();
    }

    @Override
    public void run() {

    }

    @Override
    public void die() {

    }

    @Override
    public void alternateSimpleImages() {

    }


    @Override
    public void move(int gridX, int gridY) {

    }
}
