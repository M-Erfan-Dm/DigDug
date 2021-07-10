package ir.ac.kntu.models;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {

    private final String username;

    private final String password;

    private int totalGamesCount;

    private int highScore;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getTotalGamesCount() {
        return totalGamesCount;
    }

    public void incrementTotalGamesCount() {
        totalGamesCount++;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", totalGamesCount=" + totalGamesCount +
                ", highScore=" + highScore +
                '}';
    }
}
