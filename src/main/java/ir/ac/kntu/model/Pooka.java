package ir.ac.kntu.model;

import javafx.scene.Node;

public class Pooka extends GameObject {
    public Pooka(Map map, int x, int y) {
        super(map, x, y);
    }

    @Override
    public Node getView() {
        return null;
    }
}
