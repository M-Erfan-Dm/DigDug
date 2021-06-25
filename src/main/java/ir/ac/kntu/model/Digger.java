package ir.ac.kntu.model;

import javafx.scene.Node;

public class Digger extends GameObject {
    public Digger(Map map, int x, int y) {
        super(map, x, y);
    }

    @Override
    public Node getView() {
        return null;
    }
}
