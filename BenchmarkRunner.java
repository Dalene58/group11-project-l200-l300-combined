import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {

    // Sizes used for the fast O(n log n) algorithms
    static final int[] FAST_SIZES = {1000, 5000, 10000, 50000, 100000, 500000};
    // Bubble sort is O(n^2); capped lower so it finishes in reasonable time
    static final int[] BUBBLE_SIZES = {1000, 5000, 10000, 20000};

    static final int TRIALS = 3;
    static final long SEED = 42L;

    public static void main(String[] args) throws IOException {
        String outPath = args.length > 0 ? args[0] : "results.csv";
        PrintWriter csv = new PrintWriter(new FileWriter(outPath));
        csv.println("Algorithm,InputSize,Trial,TimeMs,MemoryKB");

        // Union of all sizes actually tested, mapped to which algorithms run at that size
        int[] allSizes = {1000, 5000, 10000, 20000, 50000, 100000, 500000};

        Random master = new Random(SEED);

        for (int size : allSizes) {
            boolean runBubble = contains(BUBBLE_SIZES, size);
            boolean runFast = contains(FAST_SIZES, size);
            if (!runBubble && !runFast) continue;

            for (int trial = 1; trial <= TRIALS; trial++) {
                // Same base data for every algorithm at this size+trial, for a fair comparison
                Integer[] base = randomArray(size, master.nextLong());

                if (runBubble) {
                    Integer[] a = base.clone();
                    Result r = timeAndMemory(() -> bubbleSort(a));
                    csv.println("BubbleSort," + size + "," + trial + "," + r.timeMs + "," + r.memoryKb);
                    System.out.println("BubbleSort  size=" + size + " trial=" + trial + " time=" + r.timeMs + "ms mem=" + r.memoryKb + "KB");
                }
                if (runFast) {
                    Integer[] a1 = base.clone();
                    Result r1 = timeAndMemory(() -> mergeSort(a1, new Integer[a1.length], 0, a1.length - 1));
                    csv.println("MergeSort," + size + "," + trial + "," + r1.timeMs + "," + r1.memoryKb);
                    System.out.println("MergeSort   size=" + size + " trial=" + trial + " time=" + r1.timeMs + "ms mem=" + r1.memoryKb + "KB");

                    Integer[] a2 = base.clone();
                    Result r2 = timeAndMemory(() -> quickSort(a2, 0, a2.length - 1));
                    csv.println("QuickSort," + size + "," + trial + "," + r2.timeMs + "," + r2.memoryKb);
                    System.out.println("QuickSort   size=" + size + " trial=" + trial + " time=" + r2.timeMs + "ms mem=" + r2.memoryKb + "KB");

                    Integer[] a3 = base.clone();
                    Result r3 = timeAndMemory(() -> Arrays.sort(a3)); // built-in Timsort for Object[]
                    csv.println("Timsort(builtin)," + size + "," + trial + "," + r3.timeMs + "," + r3.memoryKb);
                    System.out.println("Timsort     size=" + size + " trial=" + trial + " time=" + r3.timeMs + "ms mem=" + r3.memoryKb + "KB");
                }
            }
        }

        csv.close();
        System.out.println("\nDone. Results written to " + outPath);
    }

    static boolean contains(int[] arr, int v) {
        for (int x : arr) if (x == v) return true;
        return false;
    }

    static Integer[] randomArray(int size, long seed) {
        Random r = new Random(seed);
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) arr[i] = r.nextInt(size * 10 + 1);
        return arr;
    }

    interface SortTask { void run(); }

    static class Result { long timeMs; long memoryKb; }

    static Result timeAndMemory(SortTask task) {
        Runtime rt = Runtime.getRuntime();
        System.gc();
        sleepQuiet();
        long memBefore = rt.totalMemory() - rt.freeMemory();

        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();

        long memAfter = rt.totalMemory() - rt.freeMemory();

        Result res = new Result();
        res.timeMs = (end - start) / 1_000_000;
        long deltaBytes = memAfter - memBefore;
        res.memoryKb = Math.max(deltaBytes, 0) / 1024;
        return res;
    }

    static void sleepQuiet() {
        try { Thread.sleep(20); } catch (InterruptedException ignored) {}
    }

    // ---- Bubble Sort: O(n^2) time, O(1) extra space ----
    static void bubbleSort(Integer[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    Integer tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    // ---- Merge Sort: O(n log n) time, O(n) extra space ----
    static void mergeSort(Integer[] a, Integer[] buffer, int lo, int hi) {
        if (lo >= hi) return;
        int mid = (lo + hi) >>> 1;
        mergeSort(a, buffer, lo, mid);
        mergeSort(a, buffer, mid + 1, hi);
        merge(a, buffer, lo, mid, hi);
    }

    static void merge(Integer[] a, Integer[] buffer, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) buffer[k] = a[k];
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = buffer[j++];
            else if (j > hi) a[k] = buffer[i++];
            else if (buffer[i] <= buffer[j]) a[k] = buffer[i++];
            else a[k] = buffer[j++];
        }
    }

    // ---- Quick Sort: O(n log n) avg time, O(log n) extra space ----
    static void quickSort(Integer[] a, int lo, int hi) {
        if (lo >= hi) return;
        int p = partition(a, lo, hi);
        quickSort(a, lo, p - 1);
        quickSort(a, p + 1, hi);
    }

    static int partition(Integer[] a, int lo, int hi) {
        // median-of-three pivot selection (placed at hi) to avoid worst case on sorted/reverse data
        int mid = lo + (hi - lo) / 2;
        if (a[mid] < a[lo]) swap(a, lo, mid);
        if (a[hi] < a[lo]) swap(a, lo, hi);
        if (a[hi] < a[mid]) swap(a, mid, hi);
        swap(a, mid, hi);
        Integer pivot = a[hi];

        // Lomuto partition scheme - simple and robust with duplicate values
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (a[j] <= pivot) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, hi);
        return i + 1;
    }

    static void swap(Integer[] a, int i, int j) {
        Integer tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}

