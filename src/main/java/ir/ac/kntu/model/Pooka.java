package ir.ac.kntu.model;

import java.util.Arrays;

public class Pooka extends Enemy {
    private static final String SIMPLE_IMAGE_1 = "src/main/resources/assets/pooka_simple1.png";

    private static final String SIMPLE_IMAGE_2 = "src/main/resources/assets/pooka_simple2.png";

    private static final String DEATH_IMAGE_1 = "src/main/resources/assets/pooka_death1.png";

    private static final String DEATH_IMAGE_2 = "src/main/resources/assets/pooka_death2.png";

    private static final String DEATH_IMAGE_3 = "src/main/resources/assets/pooka_death3.png";

    private static final String DEATH_IMAGE_4 = "src/main/resources/assets/pooka_death4.png";

    public Pooka(Map map, int x, int y) {
        super(map, x, y, Arrays.asList(SIMPLE_IMAGE_1,SIMPLE_IMAGE_2),
                Arrays.asList(DEATH_IMAGE_1,DEATH_IMAGE_2,DEATH_IMAGE_3,DEATH_IMAGE_4));
        setImage(SIMPLE_IMAGE_1);
        updateRealPos();
    }


    @Override
    public void updateViewDirection() {
        if (getDirection() != null) {
            switch (getDirection()) {
                case RIGHT:
                    getImageView().setScaleX(1);
                    break;
                case LEFT:
                    getImageView().setScaleX(-1);
                    break;
                default:
                    break;
            }
        }
    }
}
