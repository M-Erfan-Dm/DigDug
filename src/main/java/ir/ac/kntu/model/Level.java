package ir.ac.kntu.model;

import ir.ac.kntu.Game;
import ir.ac.kntu.exceptions.InvalidMapException;
import ir.ac.kntu.services.CountDownTimer;
import ir.ac.kntu.services.GameSaveInstance;
import ir.ac.kntu.services.MapFileParser;
import ir.ac.kntu.utils.MapLoader;
import javafx.scene.layout.Pane;

import java.time.LocalTime;

public class Level {
    private final Game game;

    private final Pane mapPane;

    private Map map;

    private final int mapNumber;

    private LocalTime timerTick = LocalTime.of(0,GlobalConstants.LEVEL_TIME_MIN,0);

    private CountDownTimer levelTimer;

    public Level(Game game, Pane mapPane, int mapNumber) {
        this.game = game;
        this.mapPane = mapPane;
        this.mapNumber = mapNumber;
        initTimer();
    }

    public Level(Game game, Pane mapPane, GameSaveInstance gameSaveInstance){
        this.game = game;
        this.mapPane = mapPane;
        this.mapNumber = gameSaveInstance.getMapNumber();
        timerTick = gameSaveInstance.getTimer();
        map = MapLoader.load(this,gameSaveInstance);
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

    public Map getMap() {
        return map;
    }

    public LocalTime getTimerTick() {
        return timerTick;
    }

    public void run(){
        game.getGameInfoSideLayout().setCanSaveGame(true);
        loadMap();
        startInitialDelayTimer();
    }

    private void loadMap() {
        if (map!=null){
            drawMap();
            return;
        }
        String mapPath = getMapPath();
        if (mapPath==null){
            return;
        }
        try {
            map = MapLoader.load(this,new MapFileParser(mapPath));
            drawMap();
        }  catch (InvalidMapException e) {
            e.printStackTrace();
        }
    }

    private void drawMap(){
        map.draw(mapPane);
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
        levelTimer = new CountDownTimer(timerTick.getMinute(), timerTick.getSecond());
        levelTimer.setOnTimerTickListener((min, sec) -> {
            timerTick = LocalTime.of(0,min,sec);
            game.updateTime(min,sec);
            if (min==2 && sec==50){
                game.getGameInfoSideLayout().changeTimerToWarningState();
                map.setEnemiesCanEscape(true);
            }
        });
        levelTimer.setOnTimerFinishListener(() -> finish(LevelState.LOSE));
    }

    public void finish(LevelState levelState){
        game.getGameInfoSideLayout().setCanSaveGame(false);
        map.stopAllObjects();
        levelTimer.stop();
        if (levelState!=null){
            switch (levelState){
                case WIN:
                    game.getGameInfoSideLayout().printGameWin();
                    game.removeGameSave();
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
