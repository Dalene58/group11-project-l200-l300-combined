package algorithms;

public class SearchAlgorithms {
    public static <T> int linearSearch(T[] values, T target) {
        for (int i = 0; i < values.length; i++) {
            if (target == null ? values[i] == null : target.equals(values[i])) {
                return i;
            }
        }
        return -1;
    }

    public static <T extends Comparable<T>> int binarySearch(T[] values, T target) {
        int left = 0;
        int right = values.length - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2;
            int comparison = values[middle].compareTo(target);
            if (comparison == 0) {
                return middle;
            }
            if (comparison < 0) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return -1;
    }
}
