package ir.ac.kntu.services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ListFileIO<T> implements ListIODao<T> {
    private final File file;

    public ListFileIO(String filePath) {
        file = new File(filePath);
    }

    @Override
    public void save(List<T> objects) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(objects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> load() {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
