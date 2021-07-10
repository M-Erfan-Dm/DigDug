package ir.ac.kntu.utils;

import ir.ac.kntu.models.Player;

import java.util.Comparator;

public class PlayersHighScoreComparator implements Comparator<Player> {

    private final boolean isAscending;

    public PlayersHighScoreComparator(boolean isAscending) {
        this.isAscending = isAscending;
    }

    @Override
    public int compare(Player o1, Player o2) {
        if (isAscending) {
            return o1.getHighScore() - o2.getHighScore();
        } else {
            return o2.getHighScore() - o1.getHighScore();
        }
    }
}
