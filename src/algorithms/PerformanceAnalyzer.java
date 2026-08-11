package algorithms;

import data.AlgorithmRun;

public class PerformanceAnalyzer {
    public static AlgorithmRun measure(String algorithmName, Runnable action) {
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
        long start = System.nanoTime();
        action.run();
        long end = System.nanoTime();
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        return new AlgorithmRun(algorithmName, end - start, Math.max(0L, afterMemory - beforeMemory));
    }
}
