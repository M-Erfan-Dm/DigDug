package ir.ac.kntu.model;

import javafx.scene.Node;

public abstract class GameObject {

    private final Map map;

    private int x;

    private int y;

    protected GameObject(Map map, int x, int y) {
        this.map = map;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Map getMap() {
        return map;
    }

    public void updateRealPos(){
        getView().setLayoutX(map.getPosition(x));
        getView().setLayoutY(map.getPosition(y));
    }

    abstract public Node getView();
}
