package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class Fire extends GameObject{

    private static final String FYGAR_FIRE = "src/main/resources/assets/fygar_fire.png";

    public Fire(Map map, int gridX, int gridY) {
        super(map, gridX, gridY);

        setImage(FYGAR_FIRE);
        hideImageView();
    }

    public void fire(int gridX, int gridY, Fygar fygar) {
        if (fygar.getDirection() == null) {
            return;
        }
        fygar.stopMoving();
        setGridX(gridX);
        setGridY(gridY);
        updateRealPos();
        setDirection(fygar.getDirection());
        updateViewDirection();
        showImageView();
        double step = (double) GlobalConstants.CELL_SIZE / GlobalConstants.CELL_MOVING_PARTS_COUNT;
        int count = 3 * GlobalConstants.CELL_MOVING_PARTS_COUNT;
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            showAndHideFire();
            Point2D nextPoint = getNextPoint(getRealX(), getRealY(), step, getDirection());
            setRealX(nextPoint.getX());
            setRealY(nextPoint.getY());
        }), new KeyFrame(Duration.millis(150)));
        timeline.setCycleCount(count);
        timeline.play();
        checkDigger();
        timeline.setOnFinished(actionEvent -> {
            fygar.run();
            hideImageView();
        });
    }

    private void checkDigger() {
        Cell cell = getCell();
        Digger digger = cell.getFirstObjectByType(Digger.class);
        if (digger != null) {
            digger.die();
            hideImageView();
        }
    }

    private void showAndHideFire() {
        if (getImageView().isVisible()) {
            hideImageView();
        } else {
            showImageView();
        }
    }

}
