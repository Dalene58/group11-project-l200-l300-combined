package algorithms;

import data.AlgorithmRun;

public class PerformanceAnalyzer {
    public static AlgorithmRun measure(String algorithmName, Runnable action) {
        return measure(algorithmName, 0, action);
    }

    public static AlgorithmRun measure(String algorithmName, int inputSize, Runnable action) {
        Runtime runtime = Runtime.getRuntime();
        long start = System.nanoTime();
        action.run();
        long end = System.nanoTime();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        return new AlgorithmRun("RUN-" + System.nanoTime(), algorithmName, inputSize,
            end - start, Math.max(0L, memoryUsed) / 1024L,
                java.time.LocalDateTime.now().toString());
    }
}
