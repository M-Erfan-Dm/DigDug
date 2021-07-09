package ir.ac.kntu.services;

import ir.ac.kntu.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameSaveInstanceService {
    private final ListIODao<GameSaveInstance> dao;

    private final List<GameSaveInstance> instances;

    public GameSaveInstanceService(ListIODao<GameSaveInstance> dao) {
        this.dao = dao;
        instances = dao.load();
    }

    public List<GameSaveInstance> getInstances() {
        return new ArrayList<>(instances);
    }

    public void add(GameSaveInstance gameSaveInstance) {
        GameSaveInstance oldInstance = getInstanceByPlayer(gameSaveInstance.getPlayer());
        if (oldInstance != null) {
            instances.remove(oldInstance);
        }
        instances.add(gameSaveInstance);
        dao.save(instances);
    }

    public boolean remove(Player player) {
        GameSaveInstance oldInstance = getInstanceByPlayer(player);
        if (oldInstance == null) {
            return false;
        }
        boolean isRemoved = instances.remove(oldInstance);
        if (isRemoved) {
            dao.save(instances);
            return true;
        }
        return false;
    }

    public GameSaveInstance getInstanceByPlayer(Player player) {
        return instances.stream().filter(gameSaveInstance ->
                gameSaveInstance.getPlayer().equals(player)).findFirst().orElse(null);
    }

    public boolean containsByPlayer(Player player) {
        return getInstanceByPlayer(player) != null;
    }
}
