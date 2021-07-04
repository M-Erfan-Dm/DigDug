package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.*;

public abstract class Enemy extends GameObject implements Movable{

    private Timeline movingAnimation;

    private boolean firstRun = true;

    private Random random;


    public Enemy(Map map, int gridX, int gridY) {
        super(map, gridX, gridY);
        random = new Random();
    }

    public Timeline getMovingAnimation() {
        return movingAnimation;
    }

    abstract public void run();

    abstract public void die();

    abstract public void alternateSimpleImages();

    public boolean canEnemyGoToCell(int gridX, int gridY){
        if (!getMap().isGridCoordinateInMap(gridX, gridY)) {
            return false;
        }
        Cell cell = getMap().getCell(gridX, gridY);
        return cell.isEmpty();
    }

    public void moveOneCell(){
        setNextEnemyRandomDirection();
        Point2D curGridPoint = getNextPoint(getGridX(), getGridY(), 1, getDirection());
        setGridCoordinate((int) curGridPoint.getX(),(int) curGridPoint.getY());
        double step = (double) GlobalConstants.CELL_SIZE / GlobalConstants.CELL_MOVING_PARTS_COUNT;
        movingAnimation = new Timeline(new KeyFrame(Duration.millis(80), actionEvent -> {
            alternateSimpleImages();
            checkDigger();
            Point2D point = getNextPoint(getRealX(), getRealY(), step, getDirection());
            setRealX(point.getX());
            setRealY(point.getY());
        }));
        movingAnimation.setCycleCount(GlobalConstants.CELL_MOVING_PARTS_COUNT);
        if (firstRun){
            movingAnimation.setDelay(Duration.seconds(GlobalConstants.ENEMY_INITIAL_DELAY_SEC));
        }
        firstRun = false;
        movingAnimation.play();
        movingAnimation.setOnFinished(actionEvent -> moveOneCell());
    }

    private void setNextEnemyRandomDirection(){
        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
        if (getDirection()!=null) {
            directions.remove(Direction.reverseDirection(getDirection()));
        }
        Iterator<Direction> iterator = directions.iterator();
        while (iterator.hasNext()){
            Direction direction = iterator.next();
            Point2D nextPoint = getNextPoint(getGridX(), getGridY(), 1, direction);
            if (!canEnemyGoToCell((int) nextPoint.getX(), (int) nextPoint.getY())){
                iterator.remove();
            }
        }
        if (directions.isEmpty()){
            setDirection(Direction.reverseDirection(getDirection()));
            updateViewDirection();
            return;
        }
        int randIndex = random.nextInt(directions.size());
        setDirection(directions.get(randIndex));
        updateViewDirection();
    }

    private void checkDigger(){
        Cell cell = getCell();
        Digger digger = cell.getFirstObjectByType(Digger.class);
        if (digger!=null){
            digger.die();
            movingAnimation.stop();
        }
    }
}
