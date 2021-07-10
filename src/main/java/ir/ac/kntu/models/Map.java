package ir.ac.kntu.models;

import ir.ac.kntu.services.EnemyAIService;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private final int width;

    private final int height;

    private Cell[][] cells;

    private Digger digger;

    private ExtraSkillObjectController extraSkillObjectController;

    private final Level level;

    private int enemiesCount;

    private boolean enemiesCanEscape = false;

    private final EnemyAIService enemyAIService;

    public Map(int width, int height, List<Integer>[][] rawMap, Level level) {
        this.width = width;
        this.height = height;
        this.level = level;
        enemyAIService = new EnemyAIService();
        createAllGameObjects(rawMap);
    }

    public Map(int width, int height, List<Integer>[][] rawMap, Level level, boolean enemiesCanEscape) {
        this(width, height, rawMap, level);
        this.enemiesCanEscape = enemiesCanEscape;
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

    public boolean canEnemiesEscape() {
        return enemiesCanEscape;
    }

    public void setEnemiesCanEscape(boolean enemiesCanEscape) {
        this.enemiesCanEscape = enemiesCanEscape;
    }

    public EnemyAIService getEnemyAIService() {
        return enemyAIService;
    }

    public ExtraSkillObjectController getExtraSkillObjectController() {
        return extraSkillObjectController;
    }

    private void createAllGameObjects(List<Integer>[][] rawMap) {
        cells = new Cell[height][width];
        iterateOverMapArray((i, j) -> {
            Cell cell = new Cell(j, i);
            cells[i][j] = cell;
            List<Integer> codes = rawMap[i][j];
            for (int code : codes) {
                GameObject object = createGameObjectByCode(j, i, code);
                if (object != null) {
                    cell.add(object);
                }
            }
        });
    }

    private GameObject createGameObjectByCode(int x, int y, int code) {
        if (GlobalConstants.isCodeOfSoil(code)) {
            return createSoil(x, y, code);
        }
        switch (code) {
            case GlobalConstants.DIGGER:
                return createDigger(x, y);
            case GlobalConstants.POOKA:
                return createPooka(x, y);
            case GlobalConstants.FYGAR:
                return createFygar(x, y);
            case GlobalConstants.STONE:
                return createStone(x, y);
            default:
                break;
        }
        return null;
    }

    private Digger createDigger(int x, int y) {
        if (digger != null) {
            return null;
        }
        Gun gun = new Gun(this, x, y);
        cells[y][x].add(gun);
        digger = new Digger(this, x, y, gun);
        return digger;
    }

    private Pooka createPooka(int x, int y) {
        enemiesCount++;
        return new Pooka(this, x, y);
    }

    private Fygar createFygar(int x, int y) {
        enemiesCount++;
        Fire fire = new Fire(this, x, y);
        cells[y][x].add(fire);
        return new Fygar(this, x, y, fire);
    }

    private Stone createStone(int x, int y) {
        return new Stone(this, x, y);
    }

    private Soil createSoil(int x, int y, int code) {
        return new Soil(this, x, y, code);
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
        iterateOverMapArray((i, j) -> {
            Cell cell = cells[i][j];
            if (cell.isEmpty()) {
                points.add(new Point2D(j, i));
            }
        });
        return points;
    }

    public void draw(Pane pane) {
        getAllObjectsByType(GameObject.class).
                forEach(gameObject -> pane.getChildren().add(gameObject.getImageView()));
    }

    public void runGameObjects(Pane pane) {
        digger.attachKeyboardHandlers(pane.getScene());
        extraSkillObjectController = new ExtraSkillObjectController(pane, this);
        extraSkillObjectController.run();
        getAllObjectsByType(Enemy.class).forEach(Enemy::run);
    }

    public void decrementEnemyCount() {
        enemiesCount--;
        if (enemiesCount == 0) {
            level.finish(LevelState.WIN);
        }
    }

    public void stopAllObjects() {
        getAllObjectsByType(Movable.class).forEach(Movable::stopMoving);
        extraSkillObjectController.cancelTimer();
    }

    private <T> List<T> getAllObjectsByType(Class<T> gameObjectType) {
        List<T> objects = new ArrayList<>();
        iterateOverMapArray((i, j) -> {
            Cell cell = cells[i][j];
            objects.addAll(cell.getAllObjectsByType(gameObjectType));
        });
        return objects;
    }

    public List<Integer>[][] getNumericalMapArray() {
        List<Integer>[][] mapArray = new List[height][width];
        iterateOverMapArray((i, j) -> {
            Cell cell = cells[i][j];
            List<Integer> numericalCodes = new ArrayList<>();
            for (GameObject object : cell.getGameObjects()) {
                Integer code = object.getNumericalMapCode();
                if (code != null) {
                    numericalCodes.add(code);
                }
            }
            mapArray[i][j] = numericalCodes;
        });
        return mapArray;
    }

    private void iterateOverMapArray(OnMapArrayIteration onMapArrayIteration) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (onMapArrayIteration != null) {
                    onMapArrayIteration.onIteration(i, j);
                }
            }
        }
    }
}
