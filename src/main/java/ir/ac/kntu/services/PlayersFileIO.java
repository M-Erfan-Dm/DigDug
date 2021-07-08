package ir.ac.kntu.services;

import ir.ac.kntu.model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayersFileIO implements PlayersIODao {
    private File file;

    public PlayersFileIO(String filePath) {
        file = new File(filePath);
    }

    @Override
    public void save(List<Player> players) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Player> load() {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Player>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
