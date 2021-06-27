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

    private static final double NORMAL_VELOCITY_MILLISECOND = 39;

    private double velocityMilliSecond = NORMAL_VELOCITY_MILLISECOND;

    private boolean canMove = true;

    public Digger(Map map, int x, int y) {
        super(map, x, y);
        updateRealPos();
        setImagePath(SIMPLE_IMAGE_1);
        setImage();
    }


    @Override
    public void move(int gridX, int gridY) {
        if (!canMoveToNextCell(gridX, gridY)) {
            return;
        }
        setGridX(gridX);
        setGridY(gridY);
        if (isSimpleMove(gridX, gridY)) {
            simpleMove();
        } else if (isDiggingMove(gridX, gridY)) {
            diggingMove();
        }
    }

    public void attachKeyboardHandlers(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            if (canMove) {
                Direction direction = null;
                switch (keyEvent.getCode()) {
                    case UP:
                        direction = Direction.UP;
                        move(getGridX(), getGridY() - 1);
                        break;
                    case DOWN:
                        direction = Direction.DOWN;
                        move(getGridX(), getGridY() + 1);
                        break;
                    case LEFT:
                        direction = Direction.LEFT;
                        move(getGridX() - 1, getGridY());
                        break;
                    case RIGHT:
                        direction = Direction.RIGHT;
                        move(getGridX() + 1, getGridY());
                        break;
                    default:
                        break;
                }
                changeViewDirection(direction);
            }
        });
    }

    private boolean isSimpleMove(int nextGridX, int nextGridY) {
        return getMap().getCell(nextGridX, nextGridY).isEmpty();
    }

    private void simpleMove() {
        double realX = getMap().getPosition(getGridX());
        double realY = getMap().getPosition(getGridY());
        int count = 3;
        double step = (double) Cell.CELL_SIZE / count;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(velocityMilliSecond), actionEvent -> {
            if (!getImagePath().equals(SIMPLE_IMAGE_1)) {
                setImagePath(SIMPLE_IMAGE_1);
            } else if (!getImagePath().equals(SIMPLE_IMAGE_2)) {
                setImagePath(SIMPLE_IMAGE_2);
            }
            setImage();
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
        return cell.getObjectByType(Soil.class) != null;
    }

    private void diggingMove() {
        double realX = getMap().getPosition(getGridX());
        double realY = getMap().getPosition(getGridY());
        int count = 3;
        double step = (double) Cell.CELL_SIZE / count;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(velocityMilliSecond), actionEvent -> {
            if (!getImagePath().equals(DIGGING_IMAGE_1)) {
                setImagePath(DIGGING_IMAGE_1);
            } else if (!getImagePath().equals(DIGGING_IMAGE_2)) {
                setImagePath(DIGGING_IMAGE_2);
            }
            setImage();
            Cell cell = getMap().getCell(getGridX(), getGridY());
            Soil soil = cell.getObjectByType(Soil.class);
            if (soil != null) {
                soil.destroy(cell);
            }
            getImageView().setLayoutX(Movable.getNextPositionByStep(getImageView().getLayoutX(), realX, step));
            getImageView().setLayoutY(Movable.getNextPositionByStep(getImageView().getLayoutY(), realY, step));
        }));
        timeline.setCycleCount(count);
        timeline.play();
        canMove = false;
        timeline.setOnFinished(actionEvent -> canMove = true);
    }

    private boolean canMoveToNextCell(int x, int y) {
        if (x < 0 || x >= getMap().getWidth() || y < 0 || y >= getMap().getHeight()) {
            return false;
        }
        Cell cell = getMap().getCell(x, y);
        return cell.getObjectByType(Stone.class) == null;
    }

    public void setVelocityFast() {
        velocityMilliSecond = NORMAL_VELOCITY_MILLISECOND / 1.5;
    }

    public void setVelocityNormal() {
        velocityMilliSecond = NORMAL_VELOCITY_MILLISECOND;
    }

}
