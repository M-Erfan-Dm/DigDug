package ir.ac.kntu.utils;

import ir.ac.kntu.exceptions.InvalidMapException;
import ir.ac.kntu.model.Level;
import ir.ac.kntu.model.Map;
import ir.ac.kntu.services.GameSaveInstance;
import ir.ac.kntu.services.MapParserDao;

import java.util.List;

public class MapLoader {

    private MapLoader() {
    }

    public static Map load(Level level, MapParserDao mapParserDao) throws InvalidMapException {
        List<Integer>[][] mapArray = mapParserDao.parse();
        int height = mapArray.length;
        int width = mapArray[0].length;
        return new Map(width, height, mapArray, level);
    }

    public static Map load(Level level,GameSaveInstance gameSaveInstance){
        return new Map(gameSaveInstance.getWidth(),gameSaveInstance.getHeight(),
                gameSaveInstance.getMapArray(),level, gameSaveInstance.enemiesCanEscape());
    }
}
