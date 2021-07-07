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

    private int movedCellsCounter;

    public Gun(Map map, int gridX, int gridY) {
        super(map, gridX, gridY);
        setImage(GUN_IMAGE);
        getImageView().setVisible(false);
    }

    public void setOnGunShootingFinish(OnGunShootingFinishListener onGunShootingFinishListener) {
        this.onGunShootingFinishListener = onGunShootingFinishListener;
    }

    public void shoot(Direction direction, int diggerGridX, int diggerGridY) {
        setGridCoordinate(diggerGridX,diggerGridY);
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
        if (!canBulletMoveToNextCell(gridX,gridY)){
            stopGun();
            return;
        }
        setGridCoordinate(gridX,gridY);
        int count = GlobalConstants.CELL_MOVING_PARTS_COUNT;
        int step = GlobalConstants.CELL_SIZE / count;
        movingAnimation = new Timeline(new KeyFrame(Duration.millis(30),actionEvent -> {
            Point2D nextPoint = getNextPoint(getRealX(), getRealY(), step, getDirection());
            setRealX(nextPoint.getX());
            setRealY(nextPoint.getY());
            boolean enemyExists = checkEnemy();
            if (enemyExists){
                stopGun();
            }
        }));
        movingAnimation.setCycleCount(count);
        movingAnimation.play();
        movingAnimation.setOnFinished(actionEvent -> {
            movedCellsCounter++;
            moveOneCell();
        });
    }

    @Override
    public void stopMoving() {
        if (movingAnimation!=null){
            movingAnimation.stop();
        }
        hideImageView();
    }

    private boolean isCellBlockedForBullet(int gridX, int gridY) {
        if (!getMap().isGridCoordinateInMap(gridX,gridY)){
            return true;
        }
        Cell cell = getMap().getCell(gridX, gridY);
        return cell.hasObjectType(Soil.class) || cell.hasObjectType(Stone.class);
    }

    private boolean canBulletMoveToNextCell(int gridX,int gridY){
        return movedCellsCounter < distanceRange && !isCellBlockedForBullet(gridX,gridY);
    }

    private boolean checkEnemy(){
        Cell cell = getMap().getCell(getGridX(),getGridY());
        List<Enemy> enemies = cell.getAllObjectsByType(Enemy.class);
        if (!enemies.isEmpty()){
            for (Enemy enemy : enemies){
                enemy.hit();
            }
            return true;
        }
        return false;
    }

    private void stopGun(){
        movingAnimation.stop();
        hideImageView();
        movedCellsCounter = 0;
        if (onGunShootingFinishListener!=null){
            onGunShootingFinishListener.onFinish();
        }
    }
}
