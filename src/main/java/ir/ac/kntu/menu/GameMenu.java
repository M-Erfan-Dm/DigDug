package ir.ac.kntu.menu;

import ir.ac.kntu.model.GlobalConstants;
import ir.ac.kntu.model.Level;
import ir.ac.kntu.model.Player;
import ir.ac.kntu.services.CountDownTimer;
import ir.ac.kntu.services.GameSaveInstance;
import ir.ac.kntu.services.GameSaveInstanceService;
import ir.ac.kntu.services.PlayersService;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GameMenu {

    private final HBox root;

    private final Player player;

    private int health = GlobalConstants.TOTAL_HEALTH;

    private int score;

    private Level level;

    private GameInfoSideLayout gameInfoSideLayout;

    private final PlayersService playersService;

    private final GameSaveInstanceService saveInstanceService;

    public GameMenu(HBox root, Player player, PlayersService playersService, GameSaveInstanceService saveInstanceService) {
        this.playersService = playersService;
        this.saveInstanceService = saveInstanceService;
        this.root = root;
        this.player = player;
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        initGameInfoSideLayout();
    }

    public GameMenu(HBox hBox, GameSaveInstance gameSaveInstance, PlayersService playersService,
                    GameSaveInstanceService saveInstanceService) {
        this(hBox, gameSaveInstance.getPlayer(), playersService, saveInstanceService);
        health = gameSaveInstance.getHealth();
        score = gameSaveInstance.getScore();
        loadLevelFromGameSaveInstance(gameSaveInstance);
    }

    public Player getPlayer() {
        return player;
    }

    public GameInfoSideLayout getGameInfoSideLayout() {
        return gameInfoSideLayout;
    }

    public void start() {
        if (level != null) {
            level.run();
        } else {
            loadNextLevel();
        }
        showGameInfo();
    }

    public void incrementHealth() {
        health++;
        gameInfoSideLayout.updateHealth(health);
    }

    public void decrementHealth() {
        health--;
        gameInfoSideLayout.updateHealth(health);
        CountDownTimer timer = new CountDownTimer(0, 3);
        if (health <= 0) {
            gameInfoSideLayout.printGameOver();
            timer.setOnTimerFinishListener(this::finish);
        } else {
            gameInfoSideLayout.printGameLose();
            timer.setOnTimerFinishListener(this::repeatLevel);
        }
        timer.start();
    }

    public void incrementScore(int value) {
        score += value;
        gameInfoSideLayout.updateScore(score);
    }

    public void finish() {
        evaluateScore();
        goToPlayerMainMenu();
    }

    private void showGameInfo() {
        gameInfoSideLayout.show(player.getHighScore(),
                score, health, level.getMapNumber());
    }

    private void initGameInfoSideLayout() {
        VBox vBox = new VBox();
        gameInfoSideLayout = new GameInfoSideLayout(vBox);
        gameInfoSideLayout.setOnSaveButtonClickListener(mouseEvent -> save());
        root.getChildren().add(vBox);
    }

    private void loadLevel(int mapNumber) {
        if (level != null) {
            root.getChildren().remove(level.getMapPane());
        }
        gameInfoSideLayout.changeTimerToNormalState();
        Pane mapPane = new Pane();
        level = new Level(this, mapPane, mapNumber);
        root.getChildren().add(0, mapPane);
        updateTimer(GlobalConstants.LEVEL_TIME.getMinute(), GlobalConstants.LEVEL_TIME.getSecond());
        level.run();
    }

    public void loadNextLevel() {
        gameInfoSideLayout.clearMessage();
        int nextLevel = 1;
        if (level != null) {
            nextLevel = level.getMapNumber() + 1;
        }
        if (nextLevel <= GlobalConstants.TOTAL_MAPS) {
            loadLevel(nextLevel);
            gameInfoSideLayout.updateLevel(nextLevel);
        } else {
            finish();
        }
    }

    private void repeatLevel() {
        if (level != null) {
            loadLevel(level.getMapNumber());
        }
    }

    public void updateTimer(int minute, int second) {
        gameInfoSideLayout.updateTimer(minute, second);
    }

    private void evaluateScore() {
        if (score > player.getHighScore()) {
            player.setHighScore(score);
            playersService.add(player);
        }
    }

    private void goToPlayerMainMenu() {
        root.getChildren().clear();
        StackPane stackPane = new StackPane();
        root.getScene().setRoot(stackPane);
        PlayerMainMenu playerMainMenu = new PlayerMainMenu(player, stackPane,
                playersService, saveInstanceService);
        playerMainMenu.show();
    }

    private void loadLevelFromGameSaveInstance(GameSaveInstance gameSaveInstance) {
        Pane mapPane = new Pane();
        level = new Level(this, mapPane, gameSaveInstance);
        root.getChildren().add(0, mapPane);
        updateTimer(gameSaveInstance.getTimer().getMinute(), gameSaveInstance.getTimer().getSecond());
    }

    private void save() {
        GameSaveInstance instance = new GameSaveInstance(player, level.getMap().getWidth(),
                level.getMap().getHeight(), level.getMap().getNumericalMapArray(),
                level.getMapNumber(), level.getMap().canEnemiesEscape(),
                level.getTimerTick(), health, score);

        saveInstanceService.add(instance);
    }
}
