public class InsertionSort {

    public static void insertionSort(int[] arr) {

        for (int i = 1; i < arr.length; i++) {

            int key = arr[i];
            int j = i - 1;

            // Move elements greater than key one position ahead
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }

            // Insert key into its correct position
            arr[j + 1] = key;
        }
    }

    public static void main(String[] args) {

        int[] numbers = {5, 2, 4, 6, 1, 3};

        insertionSort(numbers);

        System.out.println("Sorted array:");

        for (int number : numbers) {
            System.out.print(number + " ");
        }
    }
}