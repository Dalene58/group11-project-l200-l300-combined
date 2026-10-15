package data;

public class AlgorithmRun {
    private final String runId;
    private final String algorithmName;
    private final int inputSize;
    private final int timeNs;
    private final int memoryKb;
    private final String dateRun;

    public AlgorithmRun(String algorithmName, long runtimeNanos, long memoryBytes) {
        this("RUN-" + System.nanoTime(), algorithmName, 0, runtimeNanos,
                Math.max(0L, memoryBytes) / 1024L, java.time.LocalDateTime.now().toString());
    }

    public AlgorithmRun(String runId, String algorithmName, int inputSize, long timeNs, long memoryKb, String dateRun) {
        this.runId = runId;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.timeNs = Math.max(0L, timeNs) > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) Math.max(0L, timeNs);
        this.memoryKb = Math.max(0L, memoryKb) > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) Math.max(0L, memoryKb);
        this.dateRun = dateRun;
    }

    public String getRunId() {
        return runId;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public long getRuntimeNanos() {
        return timeNs;
    }

    public long getMemoryBytes() {
        return memoryKb * 1024L;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getTimeNs() {
        return timeNs;
    }

    public int getMemoryKb() {
        return memoryKb;
    }

    public String getDateRun() {
        return dateRun;
    }
}
