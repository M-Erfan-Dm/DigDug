package ir.ac.kntu.model;

import javafx.scene.Node;

public class Stone extends GameObject {
    public Stone(Map map, int x, int y) {
        super(map, x, y);
    }

    @Override
    public Node getView() {
        return null;
    }
}
