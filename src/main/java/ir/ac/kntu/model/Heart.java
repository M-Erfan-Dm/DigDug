package ir.ac.kntu.model;

public class Heart extends ExtraSkillObject{
    private static final String IMAGE = "src/main/resources/assets/heart.png";

    public Heart(Map map, int gridX, int gridY) {
        super(map, gridX, gridY);
        setImage(IMAGE);
        updateRealPos();
    }

    @Override
    public void performSkill() {
        getMap().getGame().incrementHealth();
    }
}
