package ir.ac.kntu.model;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.List;

public abstract class GameObject {

    private final Integer numericalMapCode;

    private final Map map;

    private int gridX;

    private int gridY;

    private final ImageView imageView;

    private String imagePath = null;

    private Direction direction;

    public GameObject(Map map, int gridX, int gridY, Integer numericalMapCode) {
        this.map = map;
        this.gridX = gridX;
        this.gridY = gridY;
        this.numericalMapCode = numericalMapCode;
        imageView = new ImageView();
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        getCell().remove(this);
        this.gridX = gridX;
        getCell().add(this);
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        getCell().remove(this);
        this.gridY = gridY;
        getCell().add(this);
    }

    public void setGridCoordinate(int gridX, int gridY) {
        getCell().remove(this);
        this.gridX = gridX;
        this.gridY = gridY;
        getCell().add(this);
    }

    public Map getMap() {
        return map;
    }

    public String getImagePath() {
        return imagePath;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Direction getDirection() {
        return direction;
    }

    public Integer getNumericalMapCode() {
        return numericalMapCode;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void updateRealPos() {
        setRealX(map.getPosition(gridX));
        setRealY(map.getPosition(gridY));
    }

    public void updateViewDirection() {
        if (direction != null) {
            switch (direction) {
                case UP:
                    imageView.setScaleX(-1);
                    imageView.setRotate(90);
                    break;
                case DOWN:
                    imageView.setRotate(90);
                    imageView.setScaleX(1);
                    break;
                case RIGHT:
                    imageView.setRotate(0);
                    imageView.setScaleX(1);
                    break;
                case LEFT:
                    imageView.setScaleX(-1);
                    imageView.setRotate(0);
                    break;
                default:
                    break;
            }
        }
    }

    public void setImage(String path) {
        imagePath = path;
        imageView.setImage(new Image(new File(imagePath).toURI().toString(),
                GlobalConstants.CELL_SIZE, GlobalConstants.CELL_SIZE, false, false));
    }

    public Point2D getNextPoint(double startX, double startY, double step, Direction direction) {
        if (direction != null) {
            double x;
            double y;
            step = Math.abs(step);
            switch (direction) {
                case UP:
                    x = startX;
                    y = startY - step;
                    break;
                case DOWN:
                    x = startX;
                    y = startY + step;
                    break;
                case RIGHT:
                    x = startX + step;
                    y = startY;
                    break;
                case LEFT:
                    x = startX - step;
                    y = startY;
                    break;
                default:
                    return null;
            }
            return new Point2D(x, y);
        }
        return null;
    }

    public void alternateImages(List<String> images) {
        if (images.size() == 0) {
            return;
        }
        for (int i = 0; i < images.size() - 1; i++) {
            String path = images.get(i);
            if (getImagePath().equals(path)) {
                setImage(images.get(i + 1));
                return;
            }
        }
        setImage(images.get(0));
    }

    public void showImageView() {
        imageView.setVisible(true);
    }

    public void hideImageView() {
        imageView.setVisible(false);
    }

    public double getRealX() {
        return imageView.getLayoutX();
    }

    public void setRealX(double x) {
        imageView.setLayoutX(x);
    }

    public double getRealY() {
        return imageView.getLayoutY();
    }

    public void setRealY(double y) {
        imageView.setLayoutY(y);
    }

    public Cell getCell() {
        return map.getCell(gridX, gridY);
    }

}
