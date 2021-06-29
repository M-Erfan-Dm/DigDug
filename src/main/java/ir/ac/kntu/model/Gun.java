package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Gun extends GameObject implements Movable {
    private static final String GUN_IMAGE = "src/main/resources/assets/digger_gun.png";

    private static final int SIMPLE_DISTANCE_RANGE = 3;

    private static final int LONG_DISTANCE_RANGE = 5;

    private int distanceRange = SIMPLE_DISTANCE_RANGE;

    private Timeline timeline;

    public Gun(Map map, int gridX, int gridY) {
        super(map, gridX, gridY);
        setImage(GUN_IMAGE);
        getImageView().setVisible(false);
    }

    public void shoot(Direction direction, int diggerGridX, int diggerGridY) {
        Point2D nextPoint = getNextPoint(diggerGridX, diggerGridY, 1, direction);
        if (nextPoint == null || isCellBlockedForBullet(((int) nextPoint.getX()), ((int) nextPoint.getY()))) {
            return;
        }
        setGridX((int) nextPoint.getX());
        setGridY((int) nextPoint.getY());
        updateRealPos();
        setDirection(direction);
        updateViewDirection();
        showGun();
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
        int count = Math.max(Math.abs(gridX - getGridX()), Math.abs(gridY - getGridY())) * 3;
        int step = Cell.CELL_SIZE / 3;
        AtomicInteger counter = new AtomicInteger();
        AtomicBoolean canMove = new AtomicBoolean(true);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30),actionEvent -> {
            if (canMove.get()) {
                canMove.set(canGunMoveToNextCell(counter.get()));
                getImageView().setLayoutX(Movable.getNextPositionByStep(getImageView().getLayoutX(), realX, step));
                getImageView().setLayoutY(Movable.getNextPositionByStep(getImageView().getLayoutY(), realY, step));
                counter.getAndIncrement();
            }
        }));
        timeline.setCycleCount(count);
        timeline.play();
        timeline.setOnFinished(actionEvent -> hideGun());
    }

    private boolean isCellBlockedForBullet(int gridX, int gridY) {
        if (gridX < 0 || gridX >= getMap().getWidth() || gridY < 0 || gridY >= getMap().getHeight()) {
            return true;
        }
        Cell cell = getMap().getCell(gridX, gridY);
        return cell.getObjectByType(Soil.class) != null || cell.getObjectByType(Stone.class) != null;
    }

    public void showGun() {
        getImageView().setVisible(true);
    }

    public void hideGun() {
        getImageView().setVisible(false);
    }

    private boolean canGunMoveToNextCell(int counter){
        if (counter % 3 == 0) {
            Point2D nextPoint = getNextPoint(getGridX(), getGridY(), 1, getDirection());
            if (nextPoint == null || isCellBlockedForBullet(((int) nextPoint.getX()), ((int) nextPoint.getY()))) {
                hideGun();
                return false;
            } else {
                setGridX((int) nextPoint.getX());
                setGridY((int) nextPoint.getY());
            }
        }
        return true;
    }
}
