package ir.ac.kntu.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public abstract class GameObject {

    private final Map map;

    private int gridX;

    private int gridY;

    private final ImageView imageView;

    private String imagePath = null;

    public GameObject(Map map, int gridX, int gridY) {
        this.map = map;
        this.gridX = gridX;
        this.gridY = gridY;
        imageView = new ImageView();
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
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

    public void updateRealPos(){
        imageView.setLayoutX(map.getPosition(gridX));
        imageView.setLayoutY(map.getPosition(gridY));
    }

    public void changeViewDirection(Direction direction){
        if (direction!=null) {
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

    public void setImage(String path){
        imagePath = path;
        imageView.setImage(new Image(new File(imagePath).toURI().toString(),
                Cell.CELL_SIZE,Cell.CELL_SIZE,false,false));
    }

}
