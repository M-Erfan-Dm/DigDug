package ir.ac.kntu;

import ir.ac.kntu.model.InvalidMapException;
import ir.ac.kntu.model.Map;
import ir.ac.kntu.model.Player;
import ir.ac.kntu.utils.MapLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

public class Game {

    private Pane root;

    private final Player player;

    private int health = 3;

    private int score;

    private int level;

    private GameInfoSideLayout gameInfoSideLayout;

    private Pane mapLayout;

    public Game(Pane root, Player player) {
        this.root = root;
        this.player = player;
        initMapLayout();
        initGameInfoSideLayout();
    }

    public Player getPlayer() {
        return player;
    }

    public void start() {
        loadMap("src/main/resources/map/main_map.txt");
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
        }
    }

    public void incrementScore(int value) {
        score += value;
        gameInfoSideLayout.updateScore(score);
    }

    public void finish() {

    }

    private void showGameInfo() {
        gameInfoSideLayout.show(player.getHighScore(), score, health, level);
    }

    private void initGameInfoSideLayout() {
        VBox vBox = new VBox();
        gameInfoSideLayout = new GameInfoSideLayout(vBox);
        root.getChildren().add(vBox);
    }

    private void initMapLayout() {
        mapLayout = new Pane();
        root.getChildren().add(mapLayout);
    }

    private void loadMap(String mapPath) {
        try {
            Map map = MapLoader.load(mapPath);
            map.getDigger().attachKeyboardHandlers(root.getScene());
            map.draw(mapLayout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidMapException e) {
            e.printStackTrace();
        }
    }
}
