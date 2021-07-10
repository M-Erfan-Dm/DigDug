package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.List;

public class Gun extends GameObject implements Movable {
    private static final String GUN_IMAGE = "src/main/resources/assets/digger_gun.png";

    private static final int SIMPLE_DISTANCE_RANGE = 3;

    private static final int LONG_DISTANCE_RANGE = 5;

    private int distanceRange = SIMPLE_DISTANCE_RANGE;

    private OnGunShootingFinishListener onGunShootingFinishListener;

    private Timeline movingAnimation;

    private int movedCellsCounter;

    public Gun(Map map, int gridX, int gridY) {
        super(map, gridX, gridY, null);
        setImage(GUN_IMAGE);
        hideImageView();
    }

    public void setOnGunShootingFinish(OnGunShootingFinishListener onGunShootingFinishListener) {
        this.onGunShootingFinishListener = onGunShootingFinishListener;
    }

    public void shoot(Direction direction, int diggerGridX, int diggerGridY) {
        setGridCoordinate(diggerGridX, diggerGridY);
        updateRealPos();
        showImageView();
        setDirection(direction);
        updateViewDirection();
        moveOneCell();
    }

    public void setSimpleDistanceRange() {
        distanceRange = SIMPLE_DISTANCE_RANGE;
    }

    public void setLongDistanceRange() {
        distanceRange = LONG_DISTANCE_RANGE;
    }

    @Override
    public void moveOneCell() {
        Point2D nextGridPoint = getNextPoint(getGridX(), getGridY(), 1, getDirection());
        int gridX = (int) nextGridPoint.getX();
        int gridY = (int) nextGridPoint.getY();
        if (!canBulletMoveToNextCell(gridX, gridY)) {
            stopGun();
            return;
        }
        setGridCoordinate(gridX, gridY);
        if (movingAnimation == null) {
            initMovingAnimation();
        }
        movingAnimation.play();
    }

    @Override
    public void stopMoving() {
        if (movingAnimation != null) {
            movingAnimation.stop();
        }
        hideImageView();
    }

    private boolean isCellBlockedForBullet(int gridX, int gridY) {
        Cell cell = getMap().getCell(gridX, gridY);
        if (cell == null) {
            return true;
        }
        return cell.hasBlock();
    }

    private boolean canBulletMoveToNextCell(int gridX, int gridY) {
        return movedCellsCounter < distanceRange && !isCellBlockedForBullet(gridX, gridY);
    }

    private boolean checkEnemy() {
        Cell cell = getCell();
        List<Enemy> enemies = cell.getAllObjectsByType(Enemy.class);
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                enemy.hit();
            }
            return true;
        }
        return false;
    }

    private void stopGun() {
        stopMoving();
        movedCellsCounter = 0;
        if (onGunShootingFinishListener != null) {
            onGunShootingFinishListener.onFinish();
        }
    }

    public void destroyGun() {
        getCell().remove(this);
        stopMoving();
    }

    private void initMovingAnimation() {
        int count = GlobalConstants.CELL_MOVING_PARTS_COUNT;
        int step = GlobalConstants.CELL_SIZE / count;
        movingAnimation = new Timeline(new KeyFrame(Duration.millis(30), actionEvent -> {
            Point2D nextPoint = getNextPoint(getRealX(), getRealY(), step, getDirection());
            setRealX(nextPoint.getX());
            setRealY(nextPoint.getY());
            boolean enemyExists = checkEnemy();
            if (enemyExists) {
                stopGun();
            }
        }));
        movingAnimation.setCycleCount(count);
        movingAnimation.setOnFinished(actionEvent -> {
            movedCellsCounter++;
            moveOneCell();
        });
    }
}
