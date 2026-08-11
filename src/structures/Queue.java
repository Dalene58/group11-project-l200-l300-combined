package structures;

public class Queue<T> {
    private final DynamicArray<T> values = new DynamicArray<>();
    private int head = 0;

    public void enqueue(T value) {
        values.add(value);
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T value = values.get(head);
        head++;
        return value;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return values.size() - head;
    }
}
