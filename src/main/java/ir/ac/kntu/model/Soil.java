package ir.ac.kntu.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;

public class Soil extends GameObject {

    private int type;

    public Soil(Map map, int x,int y,int type) {
        super(map,x,y);
        this.type = getValidType(type);
        updateImageByType();
        updateRealPos();
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void updateImageByType() {
        switch (type) {
            case GlobalConstants.SOIL_1:
                setImagePath("src/main/resources/assets/soil1.png");
                break;
            case GlobalConstants.SOIL_2:
                setImagePath("src/main/resources/assets/soil2.png");
                break;
            case GlobalConstants.SOIL_3:
                setImagePath("src/main/resources/assets/soil3.png");
                break;
            case GlobalConstants.SOIL_4:
                setImagePath("src/main/resources/assets/soil4.png");
                break;
            default:
                break;
        }
        if (getImagePath() != null) {
            setImage();
        }
    }

    private int getValidType(int code) {
        if (GlobalConstants.isCodeOfSoil(code)) {
            return code;
        }
        return GlobalConstants.SOIL_1;
    }

    public void destroy(Cell cell){
        cell.remove(this);
        getImageView().setVisible(false);
    }
}
