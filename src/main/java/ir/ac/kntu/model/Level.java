package ir.ac.kntu.model;

import ir.ac.kntu.Game;
import ir.ac.kntu.exceptions.InvalidMapException;
import ir.ac.kntu.services.CountDownTimer;
import ir.ac.kntu.utils.MapLoader;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;

public class Level {
    private final Game game;

    private final Pane mapPane;

    private Map map;

    private final int mapNumber;

    private CountDownTimer levelTimer;

    public Level(Game game, Pane mapPane, int mapNumber) {
        this.game = game;
        this.mapPane = mapPane;
        this.mapNumber = mapNumber;
        initTimer();
    }

    public Game getGame() {
        return game;
    }

    public Pane getMapPane() {
        return mapPane;
    }

    public int getMapNumber() {
        return mapNumber;
    }

    public void run(){
        String mapPath = getMapPath();
        if (mapPath==null){
            return;
        }
        loadMap(mapPath);
        CountDownTimer timer = new CountDownTimer(0,GlobalConstants.ENEMY_INITIAL_DELAY_SEC);
        timer.setOnTimerFinishListener(() -> {
            map.runGameObjects(mapPane);
            levelTimer.start();
        });
        timer.start();
    }

    private void loadMap(String mapPath) {
        try {
            map = MapLoader.load(mapPath,this);
            map.draw(mapPane);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidMapException e) {
            e.printStackTrace();
        }
    }

    private String getMapPath(){
        switch (mapNumber){
            case 1:
                return "src/main/resources/map/main_map.txt";
            default:
                return null;
        }
    }

    private void initTimer(){
        levelTimer = new CountDownTimer(GlobalConstants.LEVEL_TIME_MIN,0);
        levelTimer.setOnTimerTickListener((min, sec) -> {
            game.updateTime(min,sec);
            if (sec==50){
                levelTimer.stop();
            }
        });
    }
}
