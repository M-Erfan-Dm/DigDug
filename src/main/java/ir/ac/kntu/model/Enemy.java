package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.*;

public abstract class Enemy extends GameObject implements Movable {

    private Timeline movingAnimation;

    private Timeline hitAnimation;

    private final Random random;

    private boolean canMove = true;

    private final List<String> simpleImagesPath;

    private final List<String> deathImagesPath;

    private OnGameObjectDeathListener onEnemyDeathListener;

    private int totalHealth;

    private int curHealth;

    private int score;

    public Enemy(Map map, int gridX, int gridY, List<String> simpleImagesPath, List<String> deathImagesPath, int totalHealth, int score) {
        super(map, gridX, gridY);
        this.simpleImagesPath = simpleImagesPath;
        this.deathImagesPath = deathImagesPath;
        this.totalHealth = totalHealth;
        this.curHealth = totalHealth;
        this.score = score;
        random = new Random();
    }

    public Timeline getMovingAnimation() {
        return movingAnimation;
    }

    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setOnEnemyDeathListener(OnGameObjectDeathListener onEnemyDeathListener) {
        this.onEnemyDeathListener = onEnemyDeathListener;
    }

    public void run() {
        updateRealPos();
        canMove = true;
        moveOneCell();
    }

    public void hit() {
        curHealth--;
        if (curHealth == 0) {
            die();
            return;
        }
        if (hitAnimation == null) {
            hitAnimation = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
                alternateImages(deathImagesPath);
                stopMoving();
            }), new KeyFrame(Duration.seconds(2)));
            hitAnimation.setOnFinished(actionEvent -> {
                curHealth = totalHealth;
                run();
            });
        }
        hitAnimation.stop();
        hitAnimation.playFromStart();
    }

    public void die() {
        getCell().remove(this);
        addScoreToPlayer();
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent ->
                setImage(deathImagesPath.get(deathImagesPath.size()-1))),
                new KeyFrame(Duration.seconds(2)));
        stopMoving();
        if (hitAnimation != null) {
            hitAnimation.stop();
        }
        timeline.play();
        timeline.setOnFinished(actionEvent -> {
            hideImageView();
            if (onEnemyDeathListener != null) {
                onEnemyDeathListener.onDeath();
            }
        });
    }

    private boolean canEnemyGoToCell(int gridX, int gridY) {
        if (!getMap().isGridCoordinateInMap(gridX, gridY)) {
            return false;
        }
        Cell cell = getMap().getCell(gridX, gridY);
        return cell.isEmpty();
    }

    @Override
    public void moveOneCell() {
        if (!canMove) {
            return;
        }
        setNextEnemyRandomDirection();
        Point2D curGridPoint = getNextPoint(getGridX(), getGridY(), 1, getDirection());
        setGridCoordinate((int) curGridPoint.getX(), (int) curGridPoint.getY());
        double step = (double) GlobalConstants.CELL_SIZE / GlobalConstants.CELL_MOVING_PARTS_COUNT;
        movingAnimation = new Timeline(new KeyFrame(Duration.millis(80), actionEvent -> {
            alternateImages(simpleImagesPath);
            checkDigger();
            Point2D point = getNextPoint(getRealX(), getRealY(), step, getDirection());
            setRealX(point.getX());
            setRealY(point.getY());
        }));
        movingAnimation.setCycleCount(GlobalConstants.CELL_MOVING_PARTS_COUNT);
        movingAnimation.play();
        movingAnimation.setOnFinished(actionEvent -> moveOneCell());
    }

    private void setNextEnemyRandomDirection() {
        List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
        if (getDirection() != null) {
            directions.remove(Direction.reverseDirection(getDirection()));
        }
        Iterator<Direction> iterator = directions.iterator();
        while (iterator.hasNext()) {
            Direction direction = iterator.next();
            Point2D nextPoint = getNextPoint(getGridX(), getGridY(), 1, direction);
            if (!canEnemyGoToCell((int) nextPoint.getX(), (int) nextPoint.getY())) {
                iterator.remove();
            }
        }
        if (directions.isEmpty()) {
            setDirection(Direction.reverseDirection(getDirection()));
            updateViewDirection();
            return;
        }
        int randIndex = random.nextInt(directions.size());
        setDirection(directions.get(randIndex));
        updateViewDirection();
    }

    private void checkDigger() {
        Cell cell = getCell();
        Digger digger = cell.getFirstObjectByType(Digger.class);
        if (digger != null) {
            digger.die();
            movingAnimation.stop();
        }
    }

    @Override
    public void stopMoving() {
        canMove = false;
        movingAnimation.stop();
    }

    private void addScoreToPlayer(){
        getMap().getLevel().getGame().incrementScore(score);
    }
}
