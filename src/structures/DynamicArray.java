package structures;

import java.util.Arrays;

public class DynamicArray<T> {
    private Object[] elements;
    private int size;

    public DynamicArray() {
        this.elements = new Object[10];
        this.size = 0;
    }

    public void add(T value) {
        ensureCapacity(size + 1);
        elements[size++] = value;
    }

    public T get(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        T value = (T) elements[index];
        return value;
    }

    public void set(int index, T value) {
        checkIndex(index);
        elements[index] = value;
    }

    public T removeLast() {
        if (size == 0) {
            throw new IllegalStateException("Array is empty");
        }
        @SuppressWarnings("unchecked")
        T value = (T) elements[--size];
        elements[size] = null;
        return value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            int newCapacity = Math.max(capacity, elements.length * 2);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
