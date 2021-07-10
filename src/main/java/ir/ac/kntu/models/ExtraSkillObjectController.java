package ir.ac.kntu.models;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class ExtraSkillObjectController {
    private final Pane pane;

    private final Map map;

    private final Random random;

    private ExtraSkillObject extraSkillObject;

    private final Timeline timer;

    public ExtraSkillObjectController(Pane pane, Map map) {
        this.pane = pane;
        this.map = map;
        random = new Random();
        timer = new Timeline();
    }

    public void run() {
        timer.getKeyFrames().removeAll();
        timer.getKeyFrames().add(new KeyFrame(Duration.seconds(GlobalConstants.EXTRA_SKILL_OBJECT_DURATION_SEC), actionEvent -> {
            if (extraSkillObject != null) {
                removeObject();
                map.getDigger().resetExtraSkills();
            }
            assignRandomObject();
            extraSkillObject.addToMap();
            pane.getChildren().add(extraSkillObject.getImageView());
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void catchObject() {
        extraSkillObject.performSkill();
        removeObject();
        timer.playFromStart();
    }

    public void removeObject() {
        extraSkillObject.destroy();
        pane.getChildren().remove(extraSkillObject.getImageView());
    }

    private Point2D getRandomEmptyCell() {
        List<Point2D> emptyPoints = map.getEmptyPoints();
        if (emptyPoints.size() == 0) {
            return null;
        }
        int randomIndex = random.nextInt(emptyPoints.size());
        return emptyPoints.get(randomIndex);
    }

    private void assignRandomObject() {
        Point2D point = getRandomEmptyCell();
        if (point == null) {
            return;
        }
        int gridX = (int) point.getX();
        int gridY = (int) point.getY();
        int randomInt = random.nextInt(3);
        switch (randomInt) {
            case 0:
                extraSkillObject = new Heart(map, gridX, gridY);
                break;
            case 1:
                extraSkillObject = new SniperGun(map, gridX, gridY);
                break;
            case 2:
                extraSkillObject = new Mushroom(map, gridX, gridY);
                break;
            default:
                break;
        }
    }

    public void cancelTimer() {
        timer.stop();
    }
}
