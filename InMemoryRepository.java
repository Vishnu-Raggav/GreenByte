package Models;

import java.util.ArrayList;

/** Generic in-memory repository shared by the Client, Agent and Order repositories. */
public class InMemoryRepository<T extends Identifiable> {

    private final ArrayList<T> items = new ArrayList<>();
    private int counter = 0;

    /** Returns an id that has never been used (ids are not reused after removal). */
    public int nextId() {
        for (T item : items) {
            counter = Math.max(counter, item.getId());
        }
        counter++;
        return counter;
    }

    public void add(T item) {
        items.add(item);
    }

    public ArrayList<T> getAll() {
        return new ArrayList<>(items);
    }

    public T getById(int id) {
        for (T item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void remove(int id) {
        items.removeIf(item -> item.getId() == id);
    }
}
