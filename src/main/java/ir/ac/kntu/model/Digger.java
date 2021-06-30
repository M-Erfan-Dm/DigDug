package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.util.Duration;

public class Digger extends GameObject implements Movable {
    private static final String SIMPLE_IMAGE_1 = "src/main/resources/assets/digger_simple1.png";

    private static final String SIMPLE_IMAGE_2 = "src/main/resources/assets/digger_simple2.png";

    private static final String DIGGING_IMAGE_1 = "src/main/resources/assets/digger_digging1.png";

    private static final String DIGGING_IMAGE_2 = "src/main/resources/assets/digger_digging2.png";

    private static final String SHOOTING_IMAGE = "src/main/resources/assets/digger_shooting.png";

    private static final String DEATH_1 = "src/main/resources/assets/digger_death1.png";

    private static final String DEATH_2 = "src/main/resources/assets/digger_death2.png";

    private static final String DEATH_3 = "src/main/resources/assets/digger_death3.png";

    private static final String DEATH_4 = "src/main/resources/assets/digger_death4.png";

    private static final String DEATH_5 = "src/main/resources/assets/digger_death5.png";

    private static final double NORMAL_VELOCITY_MILLISECOND = 39;

    private double velocityMilliSecond = NORMAL_VELOCITY_MILLISECOND;

    private boolean canMove = true;

    private final Gun gun;

    private OnGameObjectDeathListener onDiggerDeathListener;

    public Digger(Map map, int x, int y, Gun gun) {
        super(map, x, y);
        this.gun = gun;
        updateRealPos();
        setImage(SIMPLE_IMAGE_1);
        gun.setOnGunShootingFinish(() -> canMove = true);
    }

    public Gun getGun() {
        return gun;
    }

    public void setOnDiggerDeathListener(OnGameObjectDeathListener onDiggerDeathListener) {
        this.onDiggerDeathListener = onDiggerDeathListener;
    }

    @Override
    public void move(int gridX, int gridY) {
        if (!canMoveToNextCell(gridX, gridY)) {
            return;
        }
        setCoordinate(gridX, gridY);
        if (isSimpleMove(gridX, gridY)) {
            simpleMove();
        } else if (isDiggingMove(gridX, gridY)) {
            diggingMove();
        }
        checkStone();
    }

    public void attachKeyboardHandlers(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            if (canMove) {
                switch (keyEvent.getCode()) {
                    case UP:
                        setDirection(Direction.UP);
                        move(getGridX(), getGridY() - 1);
                        break;
                    case DOWN:
                        setDirection(Direction.DOWN);
                        move(getGridX(), getGridY() + 1);
                        break;
                    case LEFT:
                        setDirection(Direction.LEFT);
                        move(getGridX() - 1, getGridY());
                        break;
                    case RIGHT:
                        setDirection(Direction.RIGHT);
                        move(getGridX() + 1, getGridY());
                        break;
                    case SPACE:
                        shoot();
                    default:
                        break;
                }
                updateViewDirection();
            }
        });
    }

    private boolean isSimpleMove(int nextGridX, int nextGridY) {
        return getMap().getCell(nextGridX, nextGridY).isEmpty();
    }

    private void simpleMove() {
        double realX = getMap().getPosition(getGridX());
        double realY = getMap().getPosition(getGridY());
        int count = GlobalConstants.CELL_MOVING_PARTS_COUNT;
        double step = (double) Cell.CELL_SIZE / count;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(velocityMilliSecond), actionEvent -> {
            if (!getImagePath().equals(SIMPLE_IMAGE_1)) {
                setImage(SIMPLE_IMAGE_1);
            } else if (!getImagePath().equals(SIMPLE_IMAGE_2)) {
                setImage(SIMPLE_IMAGE_2);
            }
            getImageView().setLayoutX(Movable.getNextPositionByStep(getImageView().getLayoutX(), realX, step));
            getImageView().setLayoutY(Movable.getNextPositionByStep(getImageView().getLayoutY(), realY, step));
        }));
        timeline.setCycleCount(count);
        timeline.play();
        canMove = false;
        timeline.setOnFinished(actionEvent -> canMove = true);
    }

    private boolean isDiggingMove(int nextGridX, int nextGridY) {
        Cell cell = getMap().getCell(nextGridX, nextGridY);
        return cell.hasObjectType(Soil.class);
    }

    private void diggingMove() {
        double realX = getMap().getPosition(getGridX());
        double realY = getMap().getPosition(getGridY());
        int count = GlobalConstants.CELL_MOVING_PARTS_COUNT;
        double step = (double) Cell.CELL_SIZE / count;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(velocityMilliSecond), actionEvent -> {
            if (!getImagePath().equals(DIGGING_IMAGE_1)) {
                setImage(DIGGING_IMAGE_1);
            } else if (!getImagePath().equals(DIGGING_IMAGE_2)) {
                setImage(DIGGING_IMAGE_2);
            }
            setRealX(Movable.getNextPositionByStep(getImageView().getLayoutX(), realX, step));
            setRealY(Movable.getNextPositionByStep(getImageView().getLayoutY(), realY, step));
        }));
        timeline.setCycleCount(count);
        timeline.play();
        canMove = false;
        timeline.setOnFinished(actionEvent -> canMove = true);
        Cell cell = getMap().getCell(getGridX(), getGridY());
        Soil soil = cell.getFirstObjectByType(Soil.class);
        if (soil != null) {
            soil.destroy();
        }
    }

    private void shoot() {
        canMove = false;
        setImage(SHOOTING_IMAGE);
        gun.shoot(getDirection(), getGridX(), getGridY());
    }

    private boolean canMoveToNextCell(int gridX, int gridY) {
        if (!getMap().isGridCoordinateInMap(gridX, gridY)) {
            return false;
        }
        Cell cell = getMap().getCell(gridX, gridY);
        return !cell.hasObjectType(Stone.class);
    }

    public void setVelocityFast() {
        velocityMilliSecond = NORMAL_VELOCITY_MILLISECOND / 1.5;
    }

    public void setVelocityNormal() {
        velocityMilliSecond = NORMAL_VELOCITY_MILLISECOND;
    }

    public void die() {
        canMove = false;
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            switch (getImagePath()) {
                case DEATH_1:
                    setImage(DEATH_2);
                    break;
                case DEATH_2:
                    setImage(DEATH_3);
                    break;
                case DEATH_3:
                    setImage(DEATH_4);
                    break;
                case DEATH_4:
                    setImage(DEATH_5);
                    break;
                default:
                    setImage(DEATH_1);
                    break;
            }
        }),new KeyFrame(Duration.millis(250)));
        timeline.setCycleCount(6);
        timeline.play();
        timeline.setOnFinished(actionEvent -> {
            hideImageView();
            if (onDiggerDeathListener != null) {
                onDiggerDeathListener.onDeath();
            }
        });
    }

    private void checkStone() {
        Cell cell = getMap().getCell(getGridX(), getGridY() - 1);
        if (cell == null) {
            return;
        }
        Stone stone = cell.getFirstObjectByType(Stone.class);
        if (stone != null) {
            stone.fallDown();
        }
    }
}
