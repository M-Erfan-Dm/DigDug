package ir.ac.kntu.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cell {
    public static final int CELL_SIZE = 24;

    private final Set<GameObject> gameObjects;

    private final int x;

    private final int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        gameObjects = new HashSet<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void add(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void remove(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public <T extends GameObject> T getObjectByType(Class<T> gameObjectType) {
        GameObject gameObject = gameObjects.stream().filter(object ->
                object.getClass().equals(gameObjectType)).findFirst().orElse(null);
        if (gameObject==null){
            return null;
        }
        return gameObjectType.cast(gameObject);
    }

    public List<GameObject> getGameObjects(){
        return new ArrayList<>(gameObjects);
    }

    public boolean isEmpty(){
        return gameObjects.isEmpty();
    }
}
