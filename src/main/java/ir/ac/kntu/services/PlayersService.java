package ir.ac.kntu.services;

import ir.ac.kntu.model.Player;
import ir.ac.kntu.utils.PlayersHighScoreComparator;

import java.util.*;

public class PlayersService {
    private final ListIODao<Player> ListIODao;

    private final List<Player> players;

    public PlayersService(ListIODao<Player> ListIODao) {
        this.ListIODao = ListIODao;
        players = ListIODao.load();
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public List<Player> getPlayersByHighScore(boolean isAscending){
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort(new PlayersHighScoreComparator(isAscending));
        return sortedPlayers;
    }

    public void add(Player player) {
        players.remove(player);
        players.add(player);
        ListIODao.save(players);
    }

    public boolean remove(Player player){
        boolean result = players.remove(player);
        if (result){
            ListIODao.save(players);
            return true;
        }
        return false;
    }

    public boolean contains(Player player){
        return players.contains(player);
    }

    public boolean contains(String username){
        return players.stream().anyMatch(player -> player.getUsername().equals(username));
    }

    public Player getPlayer(String username){
        return players.stream().filter(player -> player.getUsername().equals(username))
                .findFirst().orElse(null);
    }

    public boolean isPlayerAuthorized(String username, String password){
        return players.stream().anyMatch(player -> player.getUsername().equals(username) &&
                player.getPassword().equals(password));
    }


}
