package ir.ac.kntu.model;

import javafx.geometry.Point2D;

import java.util.Arrays;

public class Fygar extends Enemy {
    private static final String SIMPLE_IMAGE_1 = "src/main/resources/assets/fygar_simple1.png";

    private static final String SIMPLE_IMAGE_2 = "src/main/resources/assets/fygar_simple2.png";

    private static final String DEATH_IMAGE_1 = "src/main/resources/assets/fygar_death1.png";

    private static final String DEATH_IMAGE_2 = "src/main/resources/assets/fygar_death2.png";

    private static final String DEATH_IMAGE_3 = "src/main/resources/assets/fygar_death3.png";

    private static final String DEATH_IMAGE_4 = "src/main/resources/assets/fygar_death4.png";

    private final Fire fire;

    public Fygar(Map map, int x, int y, Fire fire) {
        super(map, x, y, Arrays.asList(SIMPLE_IMAGE_1,SIMPLE_IMAGE_2),
                Arrays.asList(DEATH_IMAGE_1,DEATH_IMAGE_2,DEATH_IMAGE_3,DEATH_IMAGE_4),
                4, GlobalConstants.FYGAR_SCORE);
        this.fire = fire;
        setImage(SIMPLE_IMAGE_1);
        updateRealPos();
    }

    @Override
    public void moveOneCell() {
        if (!canMove()) {
            return;
        }
        super.moveOneCell();
        checkEnemyToFire();
    }

    private void checkEnemyToFire(){
        Point2D nextPoint = getNextPoint(getGridX(), getGridY(), 1, getDirection());
        int nextGridX = (int) nextPoint.getX();
        int nextGridY = (int) nextPoint.getY();
        if (!getMap().isGridCoordinateInMap(nextGridX, nextGridY)) {
            return;
        }
        Cell cell = getMap().getCell(nextGridX, nextGridY);
        if (cell.hasObjectType(Digger.class)) {
            fire.fire(nextGridX, nextGridY, this);
        }
    }
}
