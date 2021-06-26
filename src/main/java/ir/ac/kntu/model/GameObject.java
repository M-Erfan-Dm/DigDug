package ir.ac.kntu.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public abstract class GameObject {

    private final Map map;

    private int x;

    private int y;

    private ImageView imageView;

    private String imagePath = null;

    protected GameObject(Map map, int x, int y) {
        this.map = map;
        this.x = x;
        this.y = y;
        imageView = new ImageView();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Map getMap() {
        return map;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void updateRealPos(){
        imageView.setLayoutX(map.getPosition(x));
        imageView.setLayoutY(map.getPosition(y));
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

    public void setImage(){
        imageView.setImage(new Image(new File(imagePath).toURI().toString(),
                Cell.CELL_SIZE,Cell.CELL_SIZE,false,false));
    }

}
