package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class Digger extends GameObject implements Movable {
    private static final String SIMPLE_IMAGE_1 = "src/main/resources/assets/digger_simple1.png";

    private static final String SIMPLE_IMAGE_2 = "src/main/resources/assets/digger_simple2.png";

    private static final String DIGGING_IMAGE_1 = "src/main/resources/assets/digger_digging1.png";

    private static final String DIGGING_IMAGE_2 = "src/main/resources/assets/digger_digging2.png";

    private static final String SHOOTING_IMAGE = "src/main/resources/assets/digger_shooting.png";

    private static final String DEATH_IMAGE_1 = "src/main/resources/assets/digger_death1.png";

    private static final String DEATH_IMAGE_2 = "src/main/resources/assets/digger_death2.png";

    private static final String DEATH_IMAGE_3 = "src/main/resources/assets/digger_death3.png";

    private static final String DEATH_IMAGE_4 = "src/main/resources/assets/digger_death4.png";

    private static final String DEATH_IMAGE_5 = "src/main/resources/assets/digger_death5.png";

    private static final List<String> SIMPLE_IMAGES_PATH = Arrays.asList(SIMPLE_IMAGE_1, SIMPLE_IMAGE_2);

    private static final List<String> DEATH_IMAGES_PATH = Arrays.asList(DEATH_IMAGE_1, DEATH_IMAGE_2, DEATH_IMAGE_3, DEATH_IMAGE_4, DEATH_IMAGE_5);

    private static final List<String> DIGGING_IMAGES_PATH = Arrays.asList(DIGGING_IMAGE_1, DIGGING_IMAGE_2);

    private static final double NORMAL_VELOCITY_MILLISECOND = 39;

    private double velocityMilliSecond = NORMAL_VELOCITY_MILLISECOND;

    private boolean canMove = true;

    private final Gun gun;

    private OnGameObjectDeathListener onDiggerDeathListener;

    private Timeline movingAnimation;

    public Digger(Map map, int x, int y, Gun gun) {
        super(map, x, y, GlobalConstants.DIGGER);
        this.gun = gun;
        updateRealPos();
        setDirection(Direction.RIGHT);
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
    public void moveOneCell() {
        Point2D nextPoint = getNextPoint(getGridX(), getGridY(), 1, getDirection());
        int nextGridX = (int) nextPoint.getX();
        int nextGridY = (int) nextPoint.getY();
        if (!canMoveToNextCell(nextGridX, nextGridY)) {
            return;
        }
        setGridCoordinate(nextGridX, nextGridY);
        if (isSimpleMove(nextGridX, nextGridY)) {
            simpleMove();
        } else if (isDiggingMove(nextGridX, nextGridY)) {
            diggingMove();
        }
        checkObjects();
    }

    @Override
    public void stopMoving() {
        canMove = false;
        if (movingAnimation != null) {
            movingAnimation.stop();
        }
    }

    public void attachKeyboardHandlers(Scene scene) {
        scene.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (canMove) {
                switch (keyEvent.getCode()) {
                    case UP:
                        setDirection(Direction.UP);
                        moveOneCell();
                        break;
                    case DOWN:
                        setDirection(Direction.DOWN);
                        moveOneCell();
                        break;
                    case LEFT:
                        setDirection(Direction.LEFT);
                        moveOneCell();
                        break;
                    case RIGHT:
                        setDirection(Direction.RIGHT);
                        moveOneCell();
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
        defaultMove(SIMPLE_IMAGES_PATH);
    }

    private boolean isDiggingMove(int nextGridX, int nextGridY) {
        Cell cell = getMap().getCell(nextGridX, nextGridY);
        return cell.hasObjectType(Soil.class);
    }

    private void diggingMove() {
        defaultMove(DIGGING_IMAGES_PATH);
        Cell cell = getCell();
        Soil soil = cell.getFirstObjectByType(Soil.class);
        if (soil != null) {
            soil.destroy();
        }
    }

    private void defaultMove(List<String> imagesPath) {
        int count = GlobalConstants.CELL_MOVING_PARTS_COUNT;
        double step = (double) GlobalConstants.CELL_SIZE / count;
        movingAnimation = new Timeline(new KeyFrame(Duration.millis(velocityMilliSecond), actionEvent -> {
            alternateImages(imagesPath);
            Point2D nextPoint = getNextPoint(getRealX(), getRealY(), step, getDirection());
            setRealX(nextPoint.getX());
            setRealY(nextPoint.getY());
        }));
        canMove = false;
        movingAnimation.setCycleCount(count);
        movingAnimation.setOnFinished(actionEvent -> canMove = true);
        movingAnimation.play();
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
        getCell().remove(this);
        stopMoving();
        gun.destroyGun();
        playDeathAnimation();
    }

    private void checkObjects() {
        checkStone();
        checkExtraSkillObject();
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

    private void checkExtraSkillObject() {
        Cell cell = getCell();
        if (cell.hasObjectType(ExtraSkillObject.class)) {
            ExtraSkillObjectController controller = getMap().getExtraSkillObjectController();
            controller.catchObject();
        }
    }

    public void resetExtraSkills() {
        setVelocityNormal();
        gun.setSimpleDistanceRange();
    }

    private void playDeathAnimation() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> alternateImages(DEATH_IMAGES_PATH)), new KeyFrame(Duration.millis(250)));
        timeline.setCycleCount(DEATH_IMAGES_PATH.size());
        timeline.setOnFinished(actionEvent -> {
            hideImageView();
            if (onDiggerDeathListener != null) {
                onDiggerDeathListener.onDeath();
            }
            getMap().getLevel().finish(LevelState.LOSE);
        });
        timeline.play();
    }
}
