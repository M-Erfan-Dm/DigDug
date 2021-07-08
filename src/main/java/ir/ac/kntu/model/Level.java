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
        startInitialDelayTimer();
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
            case 2:
                return "src/main/resources/map/map_2.txt";
            default:
                return null;
        }
    }

    private void initTimer(){
        levelTimer = new CountDownTimer(GlobalConstants.LEVEL_TIME_MIN,0);
        levelTimer.setOnTimerTickListener((min, sec) -> {
            game.updateTime(min,sec);
            if (min==2 && sec==50){
                game.getGameInfoSideLayout().changeTimerToWarningState();
                map.setEnemiesCanEscape(true);
            }
        });
        levelTimer.setOnTimerFinishListener(() -> finish(LevelState.LOSE));
    }

    public void finish(LevelState levelState){
        map.stopAllObjects();
        levelTimer.stop();
        if (levelState!=null){
            switch (levelState){
                case WIN:
                    game.getGameInfoSideLayout().printGameWin();
                    CountDownTimer timer = new CountDownTimer(0, 3);
                    timer.setOnTimerFinishListener(game::loadNextLevel);
                    timer.start();
                    break;
                case LOSE:
                    game.decrementHealth();
                    break;
            }
        }
    }

    private void startInitialDelayTimer(){
        CountDownTimer timer = new CountDownTimer(0,GlobalConstants.ENEMY_INITIAL_DELAY_SEC);
        timer.setOnTimerTickListener((min, sec) -> game.getGameInfoSideLayout().printInitialDelay(sec));
        timer.setOnTimerFinishListener(() -> {
            game.getGameInfoSideLayout().clearMessage();
            map.runGameObjects(mapPane);
            levelTimer.start();
        });
        timer.start();
    }
}
