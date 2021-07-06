package ir.ac.kntu;

import ir.ac.kntu.menu.GameInfoSideLayout;
import ir.ac.kntu.model.Level;
import ir.ac.kntu.model.Player;
import ir.ac.kntu.services.CountDownTimer;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Game {

    private Pane root;

    private final Player player;

    private int health = 3;

    private int score;

    private Level level;

    private GameInfoSideLayout gameInfoSideLayout;

    public Game(Pane root, Player player) {
        this.root = root;
        this.player = player;
        initGameInfoSideLayout();
    }

    public Player getPlayer() {
        return player;
    }

    public void start() {
        initLevel(1);
        showGameInfo();
    }

    public void incrementHealth() {
        health++;
        gameInfoSideLayout.updateHealth(health);
    }

    public void decrementHealth() {
        health--;
        gameInfoSideLayout.updateHealth(health);
        if (health == 0) {
            finish();
            return;
        }
        CountDownTimer timer = new CountDownTimer(0, 3);
        timer.setOnTimerFinishListener(() -> initLevel(1));
        timer.start();
    }

    public void incrementScore(int value) {
        score += value;
        gameInfoSideLayout.updateScore(score);
    }

    public void finish() {

    }

    private void showGameInfo() {
        gameInfoSideLayout.show(player.getHighScore(), score, health, level.getMapNumber());
    }

    private void initGameInfoSideLayout() {
        VBox vBox = new VBox();
        gameInfoSideLayout = new GameInfoSideLayout(vBox);
        root.getChildren().add(vBox);
    }

    private void initLevel(int mapNumber) {
        if (level != null) {
            root.getChildren().remove(level.getMapPane());
        }
        Pane mapPane = new Pane();
        level = new Level(this, mapPane, mapNumber);
        root.getChildren().add(0, mapPane);
        level.run();
    }

    public void updateTime(int minute,int second){
        gameInfoSideLayout.updateTimer(minute,second);
    }
}
