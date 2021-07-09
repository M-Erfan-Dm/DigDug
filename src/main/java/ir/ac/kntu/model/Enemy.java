package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

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

    public Enemy(Map map, int gridX, int gridY, List<String> simpleImagesPath, List<String> deathImagesPath, int totalHealth, int score, Integer numericalMapCode) {
        super(map, gridX, gridY, numericalMapCode);
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
                canMove = false;
                if (movingAnimation != null) {
                    movingAnimation.stop();
                }
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
        getMap().decrementEnemyCount();
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent ->
                setImage(deathImagesPath.get(deathImagesPath.size() - 1))),
                new KeyFrame(Duration.seconds(2)));
        stopMoving();
        if (hitAnimation != null) {
            hitAnimation.stop();
        }
        if (movingAnimation != null) {
            movingAnimation.stop();
        }
        timeline.play();
        timeline.setOnFinished(actionEvent -> {
            hideImageView();
            if (onEnemyDeathListener != null) {
                onEnemyDeathListener.onDeath();
            }
        });
    }

    public boolean canEnemyGoToCell(int gridX, int gridY) {
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
        Direction nextDirection = getMap().getEnemyAIService().getNextEnemyDirection(this);
        setDirection(nextDirection);
        updateViewDirection();
        Point2D curGridPoint = getNextPoint(getGridX(), getGridY(), 1, getDirection());
        setGridCoordinate((int) curGridPoint.getX(), (int) curGridPoint.getY());

        double step = (double) GlobalConstants.CELL_SIZE / GlobalConstants.CELL_MOVING_PARTS_COUNT;
        movingAnimation = new Timeline(new KeyFrame(Duration.millis(80), actionEvent -> {
            alternateImages(simpleImagesPath);
            checkDigger();
            Point2D point = getNextPoint(getRealX(), getRealY(), step, getDirection());
            setRealX(point.getX());
            setRealY(point.getY());
            checkEnemyEscape();
        }));
        movingAnimation.setCycleCount(GlobalConstants.CELL_MOVING_PARTS_COUNT);
        movingAnimation.play();
        movingAnimation.setOnFinished(actionEvent -> moveOneCell());
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
        if (movingAnimation != null) {
            movingAnimation.stop();
        }
        if (hitAnimation != null) {
            hitAnimation.stop();
        }
    }

    private void checkEnemyEscape() {
        if (getMap().canEnemiesEscape() && getGridX() == 0) {
            hideImageView();
            movingAnimation.stop();
            getMap().getLevel().finish(LevelState.LOSE);
        }
    }

    private void addScoreToPlayer() {
        getMap().getLevel().getGame().incrementScore(score);
    }
}
