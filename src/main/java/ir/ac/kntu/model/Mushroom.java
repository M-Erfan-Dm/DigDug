package ir.ac.kntu.model;

public class Mushroom extends ExtraSkillObject{
    private static final String IMAGE = "src/main/resources/assets/mushroom.png";

    public Mushroom(Map map, int gridX, int gridY) {
        super(map, gridX, gridY);
        setImage(IMAGE);
        updateRealPos();
    }

    @Override
    public void performSkill() {
        getMap().getDigger().setVelocityFast();
    }
}
