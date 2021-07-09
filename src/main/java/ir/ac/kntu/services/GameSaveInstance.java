package ir.ac.kntu.services;

import ir.ac.kntu.model.Player;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class GameSaveInstance implements Serializable {

    private Player player;

    private int width;

    private int height;

    private List<Integer>[][] mapArray;

    private int mapNumber;

    private boolean enemiesCanEscape;

    private LocalTime timer;

    private int health;

    private int score;

    public GameSaveInstance(Player player, int width, int height,
                            List<Integer>[][] mapArray, int mapNumber,
                            boolean enemiesCanEscape, LocalTime timer,
                            int health, int score) {
        this.player = player;
        this.width = width;
        this.height = height;
        this.mapArray = mapArray;
        this.mapNumber = mapNumber;
        this.enemiesCanEscape = enemiesCanEscape;
        this.timer = timer;
        this.health = health;
        this.score = score;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Integer>[][] getMapArray() {
        return mapArray;
    }

    public void setMapArray(List<Integer>[][] mapArray) {
        this.mapArray = mapArray;
    }

    public int getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(int mapNumber) {
        this.mapNumber = mapNumber;
    }

    public boolean enemiesCanEscape() {
        return enemiesCanEscape;
    }

    public void setEnemiesCanEscape(boolean enemiesCanEscape) {
        this.enemiesCanEscape = enemiesCanEscape;
    }

    public LocalTime getTimer() {
        return timer;
    }

    public void setTimer(LocalTime timer) {
        this.timer = timer;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    @Override
    public String toString() {
        return "GameSaveInstance{" +
                "player=" + player +
                ", width=" + width +
                ", height=" + height +
                ", mapArray=" + Arrays.toString(mapArray) +
                ", mapNumber=" + mapNumber +
                ", enemiesCanEscape=" + enemiesCanEscape +
                ", timer=" + timer +
                ", health=" + health +
                ", score=" + score +
                '}';
    }
}
