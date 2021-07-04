package ir.ac.kntu.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Pooka extends Enemy {
    private static final String SIMPLE_IMAGE_1 = "src/main/resources/assets/pooka_simple1.png";

    private static final String SIMPLE_IMAGE_2 = "src/main/resources/assets/pooka_simple2.png";

    private static final String DEATH_IMAGE_1 = "src/main/resources/assets/pooka_death1.png";

    private static final String DEATH_IMAGE_2 = "src/main/resources/assets/pooka_death2.png";

    private static final String DEATH_IMAGE_3 = "src/main/resources/assets/pooka_death3.png";

    private static final String DEATH_IMAGE_4 = "src/main/resources/assets/pooka_death4.png";

    private OnGameObjectDeathListener onEnemyDeathListener;

    public Pooka(Map map, int x, int y) {
        super(map, x, y);
        setImage(SIMPLE_IMAGE_1);
        updateRealPos();
    }

    public void setOnEnemyDeathListener(OnGameObjectDeathListener onEnemyDeathListener) {
        this.onEnemyDeathListener = onEnemyDeathListener;
    }

    @Override
    public void die() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> setDeathImages()),
                new KeyFrame(Duration.millis(300)));
        timeline.setCycleCount(4);
        getMovingAnimation().stop();
        timeline.play();
        timeline.setOnFinished(actionEvent -> {
            getCell().remove(this);
            hideImageView();
            if (onEnemyDeathListener != null) {
                onEnemyDeathListener.onDeath();
            }
        });
    }

    @Override
    public void run() {
        moveOneCell();
    }

    @Override
    public void move(int gridX, int gridY) {

    }

    @Override
    public void alternateSimpleImages(){
        if (!getImagePath().equals(SIMPLE_IMAGE_1)) {
            setImage(SIMPLE_IMAGE_1);
        } else if (!getImagePath().equals(SIMPLE_IMAGE_2)) {
            setImage(SIMPLE_IMAGE_2);
        }
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

    private void setDeathImages() {
        switch (getImagePath()) {
            case DEATH_IMAGE_1:
                setImage(DEATH_IMAGE_2);
                break;
            case DEATH_IMAGE_2:
                setImage(DEATH_IMAGE_3);
                break;
            case DEATH_IMAGE_3:
                setImage(DEATH_IMAGE_4);
                break;
            default:
                setImage(DEATH_IMAGE_1);
                break;
        }
    }
}
