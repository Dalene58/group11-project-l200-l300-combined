package structures;

public class Stack<T> {
    private final DynamicArray<T> values = new DynamicArray<>();

    public void push(T value) {
        values.add(value);
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return values.removeLast();
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return values.get(values.size() - 1);
    }

    public boolean isEmpty() {
        return values.size() == 0;
    }

    public int size() {
        return values.size();
    }
}
