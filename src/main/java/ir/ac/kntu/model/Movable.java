package ir.ac.kntu.model;

public interface Movable {
    void move(int gridX,int gridY);

    static double getNextPositionByStep(double curPos, double finalPos, double step){
        return curPos + Math.signum(finalPos - curPos) * step;
    }
}
