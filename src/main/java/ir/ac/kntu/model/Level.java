package ir.ac.kntu.model;

import ir.ac.kntu.exceptions.InvalidMapException;
import ir.ac.kntu.menu.GameMenu;
import ir.ac.kntu.services.CountDownTimer;
import ir.ac.kntu.services.GameSaveInstance;
import ir.ac.kntu.services.MapFileParser;
import ir.ac.kntu.utils.MapLoader;
import javafx.scene.layout.Pane;

import java.time.LocalTime;

public class Level {
    private final GameMenu gameMenu;

    private final Pane mapPane;

    private Map map;

    private final int mapNumber;

    private LocalTime timerTick = GlobalConstants.LEVEL_TIME;

    private CountDownTimer levelTimer;

    public Level(GameMenu gameMenu, Pane mapPane, int mapNumber) {
        this.gameMenu = gameMenu;
        this.mapPane = mapPane;
        this.mapNumber = mapNumber;
        initTimer();
    }

    public Level(GameMenu gameMenu, Pane mapPane, GameSaveInstance gameSaveInstance) {
        this.gameMenu = gameMenu;
        this.mapPane = mapPane;
        this.mapNumber = gameSaveInstance.getMapNumber();
        timerTick = gameSaveInstance.getTimer();
        map = MapLoader.load(this, gameSaveInstance);
        initTimer();
    }

    public GameMenu getGame() {
        return gameMenu;
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

    public void run() {
        gameMenu.getGameInfoSideLayout().setCanSaveGame(true);
        loadMap();
        startInitialDelayTimer();
    }

    private void loadMap() {
        if (map != null) {
            drawMap();
            return;
        }
        String mapPath = getMapPath();
        if (mapPath == null) {
            return;
        }
        try {
            map = MapLoader.load(this, new MapFileParser(mapPath));
            drawMap();
        } catch (InvalidMapException e) {
            e.printStackTrace();
        }
    }

    private void drawMap() {
        map.draw(mapPane);
    }

    private String getMapPath() {
        switch (mapNumber) {
            case 1:
                return "src/main/resources/map/map_1.txt";
            case 2:
                return "src/main/resources/map/map_2.txt";
            case 3:
                return "src/main/resources/map/map_3.txt";
            case 4:
                return "src/main/resources/map/map_4.txt";
            default:
                return null;
        }
    }

    private void initTimer() {
        levelTimer = new CountDownTimer(timerTick.getMinute(), timerTick.getSecond());
        levelTimer.setOnTimerTickListener((min, sec) -> {
            timerTick = LocalTime.of(0, min, sec);
            gameMenu.updateTimer(min, sec);
            checkEscapingTime(min, sec);
        });
        levelTimer.setOnTimerFinishListener(() -> finish(LevelState.LOSE));
    }

    public void finish(LevelState levelState) {
        gameMenu.getGameInfoSideLayout().setCanSaveGame(false);
        map.stopAllObjects();
        levelTimer.stop();
        if (levelState != null) {
            switch (levelState) {
                case WIN:
                    win();
                    break;
                case LOSE:
                    lose();
                    break;
                default:
                    break;
            }
        }
    }

    private void win() {
        gameMenu.getGameInfoSideLayout().printGameWin();
        gameMenu.incrementScore(GlobalConstants.LEVEL_FINISH_SCORE);
        CountDownTimer timer = new CountDownTimer(0, 3);
        timer.setOnTimerFinishListener(gameMenu::loadNextLevel);
        timer.start();
    }

    private void lose() {
        gameMenu.decrementHealth();
    }

    private void startInitialDelayTimer() {
        CountDownTimer timer = new CountDownTimer(0, GlobalConstants.ENEMY_INITIAL_DELAY_SEC);
        timer.setOnTimerTickListener((min, sec) -> gameMenu.getGameInfoSideLayout().printInitialDelay(sec));
        timer.setOnTimerFinishListener(() -> {
            gameMenu.getGameInfoSideLayout().clearMessage();
            map.runGameObjects(mapPane);
            levelTimer.start();
        });
        timer.start();
    }

    private void checkEscapingTime(int min, int sec) {
        if (min <= GlobalConstants.LEVEL_WARNING_TIME.getMinute() &&
                sec <= GlobalConstants.LEVEL_WARNING_TIME.getSecond()) {
            gameMenu.getGameInfoSideLayout().changeTimerToWarningState();
            map.setEnemiesCanEscape(true);
        }
    }
}
