package structures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriorityQueue<T> {
    private final List<T> values = new ArrayList<>();
    private final Comparator<T> comparator;

    public PriorityQueue(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public void add(T value) {
        values.add(value);
        values.sort(comparator);
    }

    public T poll() {
        if (values.isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return values.remove(0);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public int size() {
        return values.size();
    }
}
