package ir.ac.kntu.model;

public enum Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT;

    public static Direction reverseDirection(Direction direction){
        if (direction==null){
            return UP;
        }
        switch (direction){
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            default:
                break;
        }
        return UP;
    }
}
