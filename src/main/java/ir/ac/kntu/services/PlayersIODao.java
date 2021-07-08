package ir.ac.kntu.services;

import ir.ac.kntu.model.Player;

import java.util.List;

public interface PlayersIODao {
    void save(List<Player> players);
    List<Player> load();
}
