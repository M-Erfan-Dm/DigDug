package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class Fire extends GameObject implements Movable{

    private static final String FYGAR_FIRE = "src/main/resources/assets/fygar_fire.png";

    private Fygar fygar;

    private Timeline firingAnimation;

    public Fire(Map map, int gridX, int gridY) {
        super(map, gridX, gridY, null);

        setImage(FYGAR_FIRE);
        hideImageView();
    }

    public void fire(int gridX, int gridY, Fygar fygar) {
        if (fygar.getDirection() == null) {
            return;
        }
        this.fygar = fygar;
        fygar.stopMoving();
        setGridX(gridX);
        setGridY(gridY);
        updateRealPos();
        setDirection(fygar.getDirection());
        updateViewDirection();
        showImageView();
        moveOneCell();
    }

    private void checkDigger() {
        Cell cell = getCell();
        Digger digger = cell.getFirstObjectByType(Digger.class);
        if (digger != null) {
            digger.die();
        }
    }

    private void showAndHideFire() {
        if (getImageView().isVisible()) {
            hideImageView();
        } else {
            showImageView();
        }
    }

    @Override
    public void moveOneCell() {
        int count = 3 * GlobalConstants.CELL_MOVING_PARTS_COUNT;
        double step = (double) GlobalConstants.CELL_SIZE / GlobalConstants.CELL_MOVING_PARTS_COUNT;
        firingAnimation = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            showAndHideFire();
            Point2D nextPoint = getNextPoint(getRealX(), getRealY(), step, getDirection());
            setRealX(nextPoint.getX());
            setRealY(nextPoint.getY());
        }), new KeyFrame(Duration.millis(150)));
        firingAnimation.setCycleCount(count);
        firingAnimation.play();
        checkDigger();
        firingAnimation.setOnFinished(actionEvent -> {
            fygar.run();
            hideImageView();
        });
    }

    @Override
    public void stopMoving() {
        if (firingAnimation!=null){
            firingAnimation.stop();
        }
        hideImageView();
    }
}
