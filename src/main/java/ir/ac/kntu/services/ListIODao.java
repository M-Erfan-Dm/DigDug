package ir.ac.kntu.services;

import ir.ac.kntu.model.Player;

import java.util.List;

public interface ListIODao<T> {
    void save(List<T> objects);
    List<T> load();
}
