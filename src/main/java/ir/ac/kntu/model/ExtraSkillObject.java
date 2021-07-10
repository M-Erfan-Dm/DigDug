package ir.ac.kntu.model;

public abstract class ExtraSkillObject extends GameObject {

    public ExtraSkillObject(Map map, int gridX, int gridY) {
        super(map, gridX, gridY, null);
    }

    abstract public void performSkill();

    public void addToMap() {
        getCell().add(this);
    }

    public void destroy() {
        getCell().remove(this);
        hideImageView();
    }
}
