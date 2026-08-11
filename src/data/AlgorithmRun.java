package data;

public class AlgorithmRun {
    private final String algorithmName;
    private final long runtimeNanos;
    private final long memoryBytes;

    public AlgorithmRun(String algorithmName, long runtimeNanos, long memoryBytes) {
        this.algorithmName = algorithmName;
        this.runtimeNanos = runtimeNanos;
        this.memoryBytes = memoryBytes;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public long getRuntimeNanos() {
        return runtimeNanos;
    }

    public long getMemoryBytes() {
        return memoryBytes;
    }
}
