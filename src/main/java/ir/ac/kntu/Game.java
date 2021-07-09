package ir.ac.kntu;

import ir.ac.kntu.menu.GameInfoSideLayout;
import ir.ac.kntu.menu.PlayerMainMenu;
import ir.ac.kntu.model.GlobalConstants;
import ir.ac.kntu.model.Level;
import ir.ac.kntu.model.Player;
import ir.ac.kntu.services.CountDownTimer;
import ir.ac.kntu.services.PlayersService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Game {

    private Pane root;

    private final Player player;

    private int health = GlobalConstants.TOTAL_HEALTH;

    private int score;

    private Level level;

    private GameInfoSideLayout gameInfoSideLayout;

    private PlayersService playersService;

    public Game(Scene scene, Player player, PlayersService playersService) {
        this.playersService = playersService;
        root = new HBox();
        this.player = player;
        scene.setRoot(root);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        initGameInfoSideLayout();
    }

    public Player getPlayer() {
        return player;
    }

    public GameInfoSideLayout getGameInfoSideLayout() {
        return gameInfoSideLayout;
    }

    public void start() {
        loadNextLevel();
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
        }else {
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
        gameInfoSideLayout.show(player.getHighScore(), score, health, level.getMapNumber());
    }

    private void initGameInfoSideLayout() {
        VBox vBox = new VBox();
        gameInfoSideLayout = new GameInfoSideLayout(vBox);
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
        updateTime(GlobalConstants.LEVEL_TIME_MIN, 0);
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
        }else {
            finish();
        }
    }

    private void repeatLevel(){
        if (level!=null){
            loadLevel(level.getMapNumber());
        }
    }

    public void updateTime(int minute, int second) {
        gameInfoSideLayout.updateTimer(minute, second);
    }

    private void evaluateScore(){
        if (score > player.getHighScore()){
            player.setHighScore(score);
            playersService.add(player);
        }
    }

    private void goToPlayerMainMenu(){
        root.getChildren().clear();
        PlayerMainMenu playerMainMenu = new PlayerMainMenu(player,root.getScene(),playersService);
        playerMainMenu.show();
    }
}
