package ir.ac.kntu.utils;

import ir.ac.kntu.model.InvalidMapException;
import ir.ac.kntu.model.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapLoader {

    public static final int MAX_WIDTH = 34;

    public static final int MAX_HEIGHT = 27;

    private MapLoader() {
    }

    public static Map load(String path) throws FileNotFoundException, InvalidMapException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        if (width <= 0 || width > MAX_WIDTH) {
            throw new InvalidMapException("Map width must be less than " + (MAX_WIDTH + 1)
                    + " and more than 0");
        } else if (height <= 0 || height > MAX_HEIGHT) {
            throw new InvalidMapException("Map height must be less than " + (MAX_HEIGHT + 1)
                    + " and more than 0");
        }
        int[][] mapArray = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!scanner.hasNextInt()) {
                    throw new InvalidMapException("Map houses are not enough");
                }
                int value = scanner.nextInt();
                mapArray[i][j] = value;
            }
        }
        return new Map(width, height, mapArray);
    }
}
