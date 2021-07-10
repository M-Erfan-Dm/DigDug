package ir.ac.kntu.model;

import java.time.LocalTime;

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

    public static final int LEVEL_FINISH_SCORE = 1200;

    public static final LocalTime LEVEL_TIME = LocalTime.of(0, 3, 0);

    public static final LocalTime LEVEL_WARNING_TIME = LocalTime.of(0, 1, 0);

    public static final int TOTAL_MAPS = 4;

    public static final int SCENE_WIDTH = 1000;

    public static final int SCENE_HEIGHT = 700;

    public static final int TOTAL_HEALTH = 3;

    public static final int MAX_WIDTH = 34;

    public static final int MAX_HEIGHT = 27;

    public static final int EXTRA_SKILL_OBJECT_DURATION_SEC = 15;

    public static boolean isCodeOfSoil(int code) {
        return code == SOIL_1 || code == SOIL_2 ||
                code == SOIL_3 || code == SOIL_4;
    }
}
