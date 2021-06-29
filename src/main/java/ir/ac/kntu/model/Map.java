package ir.ac.kntu.model;

import javafx.scene.layout.Pane;

public class Map {

    private final int width;

    private final int height;

    private Cell[][] cells;

    private Digger digger;

    public Map(int width, int height, int[][] rawMap) {
        this.width = width;
        this.height = height;
        createAllGameObjects(rawMap);
    }

    public double getPosition(int pos){
        return pos * Cell.CELL_SIZE;
    }

    public Digger getDigger() {
        return digger;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void createAllGameObjects(int[][] rawMap) {
        cells = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = new Cell(j, i);
                int code = rawMap[i][j];
                GameObject object = createGameObjectByCode(j,i,code);
                if (object!=null){
                    cell.add(object);
                }
                cells[i][j] = cell;
            }
        }
    }

    private GameObject createGameObjectByCode(int x, int y, int code) {
        if (GlobalConstants.isCodeOfSoil(code)) {
            return new Soil(this,x,y,code);
        }
        switch (code) {
            case GlobalConstants.DIGGER:
                if (digger==null){
                    Gun gun = new Gun(this,x,y);
                    digger = new Digger(this, x, y, gun);
                    return digger;
                }
                return null;
            case GlobalConstants.POOKA:
                return new Pooka(this,x,y);
            case GlobalConstants.FYGAR:
                return new Fygar(this,x,y);
            case GlobalConstants.STONE:
                return new Stone(this,x,y);
            default:
                break;
        }
        return null;
    }

    public Cell getCell(int x,int y){
        return cells[y][x];
    }

    public void draw(Pane pane){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = cells[i][j];
                cell.getGameObjects().forEach(gameObject -> pane.getChildren().add(gameObject.getImageView()));
            }
        }
        pane.getChildren().add(digger.getGun().getImageView());
    }
}
