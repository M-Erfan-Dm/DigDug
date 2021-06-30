package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.List;

public class Stone extends GameObject {
    private static final String IMAGE = "src/main/resources/assets/stone.png";

    private static final int FALL_DOWN_DELAY_MILLISECOND = 600;
    private boolean firstFall;

    public Stone(Map map, int x, int y) {
        super(map, x, y);
        setImage(IMAGE);
        updateRealPos();
    }

    public void fallDown() {
        firstFall = true;
        moveOneCell();
    }

    private void moveOneCell() {
        Point2D nextPoint = getNextPoint(getGridX(), getGridY(), 1, Direction.DOWN);
        if (nextPoint == null || !canStoneGoToCell(((int) nextPoint.getX()), ((int) nextPoint.getY()))) {
            return;
        }
        double step = (double) Cell.CELL_SIZE / GlobalConstants.CELL_MOVING_PARTS_COUNT;
        Timeline fallDownAnimation = new Timeline(new KeyFrame(Duration.millis(30), actionEvent -> {
            Point2D nextCellPart = getNextPoint(getRealX(), getRealY(), step, Direction.DOWN);
            if (nextCellPart != null) {
                setRealX(((int) nextCellPart.getX()));
                setRealY(((int) nextCellPart.getY()));
            }
        }));
        if (firstFall){
            fallDownAnimation.setDelay(Duration.millis(FALL_DOWN_DELAY_MILLISECOND));
        }
        fallDownAnimation.setCycleCount(GlobalConstants.CELL_MOVING_PARTS_COUNT);
        fallDownAnimation.play();
        firstFall = false;
        fallDownAnimation.setOnFinished(actionEvent -> {
            setGridY(getGridY() + 1);
            checkDiggerAndEnemies();
            moveOneCell();
        });
    }

    private void checkDiggerAndEnemies() {
        Cell cell = getMap().getCell(getGridX(), getGridY());
        List<Enemy> enemies = cell.getAllObjectsByType(Enemy.class);
        for (Enemy enemy : enemies) {
            enemy.die();
        }
        Digger digger = cell.getFirstObjectByType(Digger.class);
        if (digger != null) {
            digger.die();
        }
    }

    private boolean canStoneGoToCell(int gridX, int gridY) {
        if (!getMap().isGridCoordinateInMap(gridX, gridY)) {
            return false;
        }
        Cell cell = getMap().getCell(gridX, gridY);
        return cell.isEmpty();
    }
}
