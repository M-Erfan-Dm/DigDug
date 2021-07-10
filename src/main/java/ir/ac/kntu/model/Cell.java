package ir.ac.kntu.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Cell {

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

    public <T> List<T> getAllObjectsByType(Class<T> gameObjectType) {
        return gameObjects.stream().filter(gameObjectType::isInstance)
                .map(gameObjectType::cast).collect(Collectors.toList());
    }

    public <T extends GameObject> boolean hasObjectType(Class<T> gameObjectType) {
        List<T> objects = getAllObjectsByType(gameObjectType);
        return !objects.isEmpty();
    }

    public <T extends GameObject> T getFirstObjectByType(Class<T> gameObjectType) {
        List<T> objects = getAllObjectsByType(gameObjectType);
        if (!objects.isEmpty()) {
            return objects.get(0);
        }
        return null;
    }

    public List<GameObject> getGameObjects() {
        return new ArrayList<>(gameObjects);
    }

    public boolean isEmpty() {
        return gameObjects.isEmpty() || !hasBlock();
    }

    public boolean hasBlock() {
        return hasObjectType(Soil.class) || hasObjectType(Stone.class);
    }
}
