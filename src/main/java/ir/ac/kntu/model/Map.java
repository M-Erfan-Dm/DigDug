package ir.ac.kntu.model;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private final int width;

    private final int height;

    private Cell[][] cells;

    private List<GameObject> gameObjects;

    private Digger digger;

    private ExtraSkillObjectController extraSkillObjectController;

    private Level level;

    public Map(int width, int height, int[][] rawMap, Level level) {
        this.width = width;
        this.height = height;
        this.level = level;
        gameObjects = new ArrayList<>();
        createAllGameObjects(rawMap);
    }

    public double getPosition(int pos) {
        return pos * GlobalConstants.CELL_SIZE;
    }

    public Digger getDigger() {
        return digger;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Level getLevel() {
        return level;
    }

    public ExtraSkillObjectController getExtraSkillObjectController() {
        return extraSkillObjectController;
    }

    private void createAllGameObjects(int[][] rawMap) {
        cells = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = new Cell(j, i);
                int code = rawMap[i][j];
                GameObject object = createGameObjectByCode(j, i, code);
                if (object != null) {
                    gameObjects.add(object);
                    cell.add(object);
                }
                cells[i][j] = cell;
            }
        }
    }

    private GameObject createGameObjectByCode(int x, int y, int code) {
        if (GlobalConstants.isCodeOfSoil(code)) {
            return new Soil(this, x, y, code);
        }
        switch (code) {
            case GlobalConstants.DIGGER:
                if (digger == null) {
                    Gun gun = new Gun(this, x, y);
                    gameObjects.add(gun);
                    digger = new Digger(this, x, y, gun);
                    return digger;
                }
                return null;
            case GlobalConstants.POOKA:
                return new Pooka(this, x, y);
            case GlobalConstants.FYGAR:
                Fire fire = new Fire(this, x, y);
                gameObjects.add(fire);
                return new Fygar(this, x, y, fire);
            case GlobalConstants.STONE:
                return new Stone(this, x, y);
            default:
                break;
        }
        return null;
    }

    public Cell getCell(int x, int y) {
        if (isGridCoordinateInMap(x, y)) {
            return cells[y][x];
        }
        return null;
    }

    public boolean isGridCoordinateInMap(int gridX, int gridY) {
        return gridX >= 0 && gridX < width && gridY >= 0 && gridY < height;
    }

    public List<Point2D> getEmptyPoints() {
        List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = cells[i][j];
                if (cell.isEmpty()) {
                    points.add(new Point2D(j, i));
                }
            }
        }
        return points;
    }

    public void draw(Pane pane) {
        gameObjects.forEach(gameObject -> pane.getChildren().add(gameObject.getImageView()));
    }

    public void runGameObjects(Pane pane) {
        digger.attachKeyboardHandlers(pane.getScene());
        extraSkillObjectController = new ExtraSkillObjectController(pane, this);
        extraSkillObjectController.run();
        gameObjects.stream().filter(gameObject -> gameObject instanceof Enemy)
                .forEach(gameObject -> ((Enemy) gameObject).run());
    }
}
