package ir.ac.kntu.services;

import ir.ac.kntu.model.Player;
import ir.ac.kntu.utils.PlayersHighScoreComparator;

import java.io.*;
import java.util.*;

public class PlayersService {
    private final PlayersIODao playersIODao;

    private final List<Player> players;

    public PlayersService(PlayersIODao playersIODao) {
        this.playersIODao = playersIODao;
        players = playersIODao.load();
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
        playersIODao.save(players);
    }

    public boolean remove(Player player){
        boolean result = players.remove(player);
        if (result){
            playersIODao.save(players);
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
