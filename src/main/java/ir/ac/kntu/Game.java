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

    private Map map;

    private GameInfoSideLayout gameInfoSideLayout;

    private Pane mapLayout;

    public Game(Pane root, Player player) {
        this.root = root;
        this.player = player;
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
            return;
        }
        loadMap("src/main/resources/map/main_map.txt");
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
        root.getChildren().remove(mapLayout);
        mapLayout = new Pane();
        root.getChildren().add(0,mapLayout);
    }

    private void loadMap(String mapPath) {
        initMapLayout();
        try {
            map = MapLoader.load(mapPath,this);
            map.getDigger().attachKeyboardHandlers(root.getScene());
            map.draw(mapLayout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidMapException e) {
            e.printStackTrace();
        }
    }
}
