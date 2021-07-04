package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Gun extends GameObject implements Movable {
    private static final String GUN_IMAGE = "src/main/resources/assets/digger_gun.png";

    private static final int SIMPLE_DISTANCE_RANGE = 3;

    private static final int LONG_DISTANCE_RANGE = 5;

    private int distanceRange = SIMPLE_DISTANCE_RANGE;

    private OnGunShootingFinishListener onGunShootingFinishListener;

    private Timeline movingAnimation;

    public Gun(Map map, int gridX, int gridY) {
        super(map, gridX, gridY);
        setImage(GUN_IMAGE);
        getImageView().setVisible(false);
    }

    public void setOnGunShootingFinish(OnGunShootingFinishListener onGunShootingFinishListener) {
        this.onGunShootingFinishListener = onGunShootingFinishListener;
    }

    public void shoot(Direction direction, int diggerGridX, int diggerGridY) {
        Point2D nextPoint = getNextPoint(diggerGridX, diggerGridY, 1, direction);
        if (nextPoint == null || isCellBlockedForBullet(((int) nextPoint.getX()), ((int) nextPoint.getY()))) {
            if (onGunShootingFinishListener !=null){
                onGunShootingFinishListener.onFinish();
            }
            return;
        }
        setGridX((int) nextPoint.getX());
        setGridY((int) nextPoint.getY());
        updateRealPos();
        showImageView();
        setDirection(direction);
        updateViewDirection();
        Point2D finalPoint = getNextPoint(getGridX(), getGridY(), distanceRange, direction);
        if (finalPoint != null) {
            move((int) finalPoint.getX(), (int) finalPoint.getY());
        }
    }

    public void setSimpleDistanceRange() {
        distanceRange = SIMPLE_DISTANCE_RANGE;
    }

    public void setLongDistanceRange() {
        distanceRange = LONG_DISTANCE_RANGE;
    }

    @Override
    public void move(int gridX, int gridY) {
        double realX = getMap().getPosition(gridX);
        double realY = getMap().getPosition(gridY);
        int count = Math.max(Math.abs(gridX - getGridX()), Math.abs(gridY - getGridY())) * GlobalConstants.CELL_MOVING_PARTS_COUNT;
        int step = GlobalConstants.CELL_SIZE / GlobalConstants.CELL_MOVING_PARTS_COUNT;
        AtomicInteger counter = new AtomicInteger();
        AtomicBoolean canMove = new AtomicBoolean(true);
        movingAnimation = new Timeline(new KeyFrame(Duration.millis(30),actionEvent -> {
            if (canMove.get()) {
                canMove.set(checkEnemy());
                canMove.set(canGunMoveToNextCell(counter.get()));
                if (!canMove.get() && onGunShootingFinishListener !=null){
                    onGunShootingFinishListener.onFinish();
                }
                getImageView().setLayoutX(Movable.getNextPositionByStep(getImageView().getLayoutX(), realX, step));
                getImageView().setLayoutY(Movable.getNextPositionByStep(getImageView().getLayoutY(), realY, step));
                counter.getAndIncrement();
            }
        }));
        movingAnimation.setCycleCount(count);
        movingAnimation.play();
        movingAnimation.setOnFinished(actionEvent -> {
            hideImageView();
            if (onGunShootingFinishListener !=null){
                onGunShootingFinishListener.onFinish();
            }
        });
    }

    private boolean isCellBlockedForBullet(int gridX, int gridY) {
        if (gridX < 0 || gridX >= getMap().getWidth() || gridY < 0 || gridY >= getMap().getHeight()) {
            return true;
        }
        Cell cell = getMap().getCell(gridX, gridY);
        return cell.hasObjectType(Soil.class) || cell.hasObjectType(Stone.class);
    }

    private boolean canGunMoveToNextCell(int moveCounter){
        if (moveCounter % GlobalConstants.CELL_MOVING_PARTS_COUNT == 0) {
            Point2D nextPoint = getNextPoint(getGridX(), getGridY(), 1, getDirection());
            if (nextPoint == null || isCellBlockedForBullet(((int) nextPoint.getX()), ((int) nextPoint.getY()))) {
                hideImageView();
                return false;
            } else {
                setGridX((int) nextPoint.getX());
                setGridY((int) nextPoint.getY());
            }
        }
        return true;
    }

    private boolean checkEnemy(){
        Cell cell = getMap().getCell(getGridX(),getGridY());
        List<Enemy> enemies = cell.getAllObjectsByType(Enemy.class);
        if (!enemies.isEmpty()){
            stopGun();
            for (Enemy enemy : enemies){
                enemy.die();
            }
            return true;
        }
        return false;
    }

    private void stopGun(){
        movingAnimation.stop();
        hideImageView();
        if (onGunShootingFinishListener!=null){
            onGunShootingFinishListener.onFinish();
        }
    }
}
