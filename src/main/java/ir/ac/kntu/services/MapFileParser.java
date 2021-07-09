package ir.ac.kntu.services;

import ir.ac.kntu.exceptions.InvalidMapException;
import ir.ac.kntu.model.GlobalConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapFileParser implements MapParserDao {
    private String path;

    public MapFileParser(String path) {
        this.path = path;
    }

    @Override
    public List<Integer>[][] parse() throws InvalidMapException {
        File file = new File(path);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new InvalidMapException("Map not found");
        }
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        if (width <= 0 || width > GlobalConstants.MAX_WIDTH) {
            throw new InvalidMapException("Map width must be less than " + (GlobalConstants.MAX_WIDTH + 1)
                    + " and more than 0");
        } else if (height <= 0 || height > GlobalConstants.MAX_HEIGHT) {
            throw new InvalidMapException("Map height must be less than " + (GlobalConstants.MAX_HEIGHT + 1)
                    + " and more than 0");
        }
        List<Integer>[][] mapArray = new ArrayList[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!scanner.hasNextInt()) {
                    throw new InvalidMapException("Map houses are not enough");
                }
                List<Integer> codes = new ArrayList<>();
                mapArray[i][j] = codes;
                int value = scanner.nextInt();
                codes.add(value);
            }
        }
        return mapArray;
    }
}
