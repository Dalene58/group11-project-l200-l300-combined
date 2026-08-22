package data;

public class AlgorithmCompletion {
    private final String algorithmName;
    private final int inputSize;
    private final int completedRequests;
    private final String dateRun;

    public AlgorithmCompletion(String algorithmName, int inputSize, int completedRequests, String dateRun) {
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.completedRequests = completedRequests;
        this.dateRun = dateRun;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getCompletedRequests() {
        return completedRequests;
    }

    public String getDateRun() {
        return dateRun;
    }
}