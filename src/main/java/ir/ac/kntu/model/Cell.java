package ir.ac.kntu.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public <T extends GameObject> List<T> getObjectsByType(Class<T> gameObjectType) {
        return gameObjects.stream().filter(gameObjectType::isInstance)
                .map(gameObjectType::cast).collect(Collectors.toList());
    }

    public <T extends GameObject> boolean hasObjectType(Class<T> gameObjectType){
        List<T> objects = getObjectsByType(gameObjectType);
        return !objects.isEmpty();
    }

    public List<GameObject> getGameObjects() {
        return new ArrayList<>(gameObjects);
    }

    public boolean isEmpty() {
        return gameObjects.isEmpty() || (!hasObjectType(Soil.class)
                && !hasObjectType(Stone.class));
    }
}
