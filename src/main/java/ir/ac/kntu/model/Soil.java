package ir.ac.kntu.model;

public class Soil extends GameObject {

    private int type;

    public Soil(Map map, int x, int y, int type) {
        super(map, x, y, type);
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
            case GlobalConstants.SOIL_2:
                setImage("src/main/resources/assets/soil2.png");
                break;
            case GlobalConstants.SOIL_3:
                setImage("src/main/resources/assets/soil3.png");
                break;
            case GlobalConstants.SOIL_4:
                setImage("src/main/resources/assets/soil4.png");
                break;
            case GlobalConstants.SOIL_1:
            default:
                setImage("src/main/resources/assets/soil1.png");
                break;
        }
    }

    private int getValidType(int code) {
        if (GlobalConstants.isCodeOfSoil(code)) {
            return code;
        }
        return GlobalConstants.SOIL_1;
    }

    public void destroy() {
        getMap().getCell(getGridX(), getGridY()).remove(this);
        getImageView().setVisible(false);
    }
}
