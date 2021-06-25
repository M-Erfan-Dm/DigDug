package ir.ac.kntu.model;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Soil extends GameObject {

    private int type;

    private final ImageView imageView;

    public Soil(Map map, int x,int y,int type) {
        super(map,x,y);
        imageView = new ImageView();
        this.type = getValidType(type);
        updateImageByType();
        updateRealPos();
    }

    @Override
    public Node getView() {
        return imageView;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void updateImageByType() {
        String path = null;
        switch (type) {
            case GlobalConstants.SOIL_1:
                path = "src/main/resources/assets/soil1.png";
                break;
            case GlobalConstants.SOIL_2:
                path = "src/main/resources/assets/soil2.png";
                break;
            case GlobalConstants.SOIL_3:
                path = "src/main/resources/assets/soil3.png";
                break;
            case GlobalConstants.SOIL_4:
                path = "src/main/resources/assets/soil4.png";
                break;
            default:
                break;
        }
        if (path != null) {
            imageView.setImage(new Image(new File(path).toURI().toString(), Cell.CELL_SIZE, Cell.CELL_SIZE, false, false));
        }
    }

    private int getValidType(int code) {
        if (GlobalConstants.isCodeOfSoil(code)) {
            return code;
        }
        return GlobalConstants.SOIL_1;
    }
}
