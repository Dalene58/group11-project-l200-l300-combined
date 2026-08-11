package algorithms;

import java.util.Arrays;
import java.util.Comparator;

public class SortAlgorithms {
    public static <T extends Comparable<T>> void insertionSort(T[] values) {
        for (int i = 1; i < values.length; i++) {
            T current = values[i];
            int j = i - 1;
            while (j >= 0 && values[j].compareTo(current) > 0) {
                values[j + 1] = values[j];
                j--;
            }
            values[j + 1] = current;
        }
    }

    public static <T extends Comparable<T>> void selectionSort(T[] values) {
        for (int i = 0; i < values.length - 1; i++) {
            int smallest = i;
            for (int j = i + 1; j < values.length; j++) {
                if (values[j].compareTo(values[smallest]) < 0) {
                    smallest = j;
                }
            }
            T temp = values[i];
            values[i] = values[smallest];
            values[smallest] = temp;
        }
    }

    public static <T> void mergeSort(T[] values, Comparator<T> comparator) {
        if (values.length < 2) {
            return;
        }
        int middle = values.length / 2;
        T[] left = Arrays.copyOfRange(values, 0, middle);
        T[] right = Arrays.copyOfRange(values, middle, values.length);
        mergeSort(left, comparator);
        mergeSort(right, comparator);
        merge(values, left, right, comparator);
    }

    private static <T> void merge(T[] target, T[] left, T[] right, Comparator<T> comparator) {
        int leftIndex = 0;
        int rightIndex = 0;
        int targetIndex = 0;
        while (leftIndex < left.length && rightIndex < right.length) {
            if (comparator.compare(left[leftIndex], right[rightIndex]) <= 0) {
                target[targetIndex++] = left[leftIndex++];
            } else {
                target[targetIndex++] = right[rightIndex++];
            }
        }
        while (leftIndex < left.length) {
            target[targetIndex++] = left[leftIndex++];
        }
        while (rightIndex < right.length) {
            target[targetIndex++] = right[rightIndex++];
        }
    }
}
