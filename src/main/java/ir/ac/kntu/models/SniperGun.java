package ir.ac.kntu.models;

public class SniperGun extends ExtraSkillObject {
    private static final String IMAGE = "src/main/resources/assets/gun.png";

    public SniperGun(Map map, int gridX, int gridY) {
        super(map, gridX, gridY);
        setImage(IMAGE);
        updateRealPos();
    }

    @Override
    public void performSkill() {
        getMap().getDigger().getGun().setLongDistanceRange();
    }
}
