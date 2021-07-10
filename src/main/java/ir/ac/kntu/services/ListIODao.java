package ir.ac.kntu.services;

import java.util.List;

public interface ListIODao<T> {
    void save(List<T> objects);

    List<T> load();
}
