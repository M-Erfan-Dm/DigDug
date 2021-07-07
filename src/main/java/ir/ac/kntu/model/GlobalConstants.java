package ir.ac.kntu.model;

public class GlobalConstants {

    public static final int EMPTY = 0;

    public static final int SOIL_1 = 1;

    public static final int SOIL_2 = 2;

    public static final int SOIL_3 = 3;

    public static final int SOIL_4 = 4;

    public static final int DIGGER = 5;

    public static final int POOKA = 6;

    public static final int FYGAR = 7;

    public static final int STONE = 8;

    public static final int CELL_MOVING_PARTS_COUNT = 3;

    public static final int CELL_SIZE = 24;

    public static final int ENEMY_INITIAL_DELAY_SEC = 5;

    public static final int POOKA_SCORE = 800;

    public static final int FYGAR_SCORE = 1000;

    public static final int LEVEL_TIME_MIN = 3;

    public static final int TOTAL_MAPS = 2;

    public static boolean isCodeOfSoil(int code) {
        return code == SOIL_1 || code == SOIL_2 ||
                code == SOIL_3 || code == SOIL_4;
    }
}
