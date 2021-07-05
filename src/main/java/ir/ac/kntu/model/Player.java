package ir.ac.kntu.model;

public class Player {
    private String name;

    private int totalGamesCount;

    private int highScore;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalGamesCount() {
        return totalGamesCount;
    }

    public void incrementTotalGamesCount(){
        totalGamesCount++;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}
