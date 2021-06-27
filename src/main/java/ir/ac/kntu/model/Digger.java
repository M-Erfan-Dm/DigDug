package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.util.Duration;

public class Digger extends GameObject implements Movable {
    private static final String DIGGER_SIMPLE_IMAGE_1 = "src/main/resources/assets/digger_simple1.png";

    private static final String DIGGER_SIMPLE_IMAGE_2 = "src/main/resources/assets/digger_simple2.png";

    private static final double NORMAL_VELOCITY_MILLISECOND = 39;

    private double velocityMilliSecond  = NORMAL_VELOCITY_MILLISECOND;

    private boolean canMove = true;

    public Digger(Map map, int x, int y) {
        super(map, x, y);
        updateRealPos();
        setImagePath(DIGGER_SIMPLE_IMAGE_1);
        setImage();
    }


    @Override
    public void move(int gridX, int gridY) {
        if (!canMoveToNextCell(gridX, gridY)) {
            return;
        }
        setX(gridX);
        setY(gridY);
        if (isSimpleMove(gridX, gridY)) {
            simpleMove();
        }
    }

    public void attachKeyboardHandlers(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            if (canMove) {
                Direction direction = null;
                switch (keyEvent.getCode()) {
                    case UP:
                        direction = Direction.UP;
                        move(getX(), getY() - 1);
                        break;
                    case DOWN:
                        direction = Direction.DOWN;
                        move(getX(), getY() + 1);
                        break;
                    case LEFT:
                        direction = Direction.LEFT;
                        move(getX() - 1, getY());
                        break;
                    case RIGHT:
                        direction = Direction.RIGHT;
                        move(getX() + 1, getY());
                        break;
                    default:
                        break;
                }
                changeViewDirection(direction);
            }
        });
    }

    private boolean isSimpleMove(int nextX, int nextY) {
        return getMap().getCell(nextX, nextY).isEmpty();
    }

    private void simpleMove() {
        double realX = getMap().getPosition(getX());
        double realY = getMap().getPosition(getY());
        int count = 3;
        double step = (double) Cell.CELL_SIZE / count;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(velocityMilliSecond), actionEvent -> {
            if (!getImagePath().equals(DIGGER_SIMPLE_IMAGE_1)) {
                setImagePath(DIGGER_SIMPLE_IMAGE_1);
            } else if (!getImagePath().equals(DIGGER_SIMPLE_IMAGE_2)) {
                setImagePath(DIGGER_SIMPLE_IMAGE_2);
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

    private boolean canMoveToNextCell(int x, int y) {
        if (x < 0 || x >= getMap().getWidth() || y < 0 || y >= getMap().getHeight()) {
            return false;
        }
        Cell cell = getMap().getCell(x, y);
        return cell.getObjectByType(Stone.class) == null;
    }

    public void setVelocityFast(){
        velocityMilliSecond = NORMAL_VELOCITY_MILLISECOND / 1.5;
    }

    public void setVelocityNormal(){
        velocityMilliSecond = NORMAL_VELOCITY_MILLISECOND;
    }

}
