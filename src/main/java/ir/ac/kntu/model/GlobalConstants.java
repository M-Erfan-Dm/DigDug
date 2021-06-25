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

    public static final int GUN = 9;

    public static final int HEART = 10;

    public static final int MUSHROOM = 11;


    public static boolean isCodeOfSoil(int code) {
        return code == SOIL_1 || code == SOIL_2 ||
                code == SOIL_3 || code == SOIL_4;
    }
}