package algorithms;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import data.AlgorithmCompletion;
import data.AlgorithmRun;
import data.Resource;
import data.ServiceRequest;
import database.ServiceRequestCsvReader;

public class BatchBenchmarkProcessor {
    private final ServiceRequestCsvReader csvReader;

    public BatchBenchmarkProcessor() {
        this(new ServiceRequestCsvReader());
    }

    public BatchBenchmarkProcessor(ServiceRequestCsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public List<AlgorithmRun> runBatches(Path csvPath) {
        try {
            List<ServiceRequest> requests = csvReader.readRequests(csvPath);
            List<Integer> configuredBatchSizes = resolveBatchSizes(requests.size());
            List<AlgorithmRun> runs = new ArrayList<>();

            for (int configuredSize : configuredBatchSizes) {
                int batchSize = Math.min(configuredSize, requests.size());
                if (batchSize <= 0) {
                    continue;
                }
                List<ServiceRequest> batch = csvReader.readFirstNRequests(csvPath, batchSize);
                runs.add(PerformanceAnalyzer.measure("Greedy Dispatch", batch.size(),
                        () -> GreedyOptimizer.dispatch(batch, benchmarkResources(batchSize))));
                runs.add(PerformanceAnalyzer.measure("DP Optimisation", batch.size(),
                        () -> DynamicProgrammingOptimizer.optimise(batch, 10)));
            }
            return runs;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to run batch benchmarks from CSV", exception);
        }
    }

    public List<AlgorithmCompletion> runCompletionComparison(Path csvPath) {
        try {
            List<ServiceRequest> requests = csvReader.readRequests(csvPath);
            List<Integer> batchSizes = resolveBatchSizes(requests.size());
            List<AlgorithmCompletion> rows = new ArrayList<>();

            for (int batchSize : batchSizes) {
                List<ServiceRequest> batch = csvReader.readFirstNRequests(csvPath, batchSize);
                String dateRun = LocalDateTime.now().toString();

                int greedyCompleted = GreedyOptimizer.dispatch(batch, benchmarkResources(batchSize)).size();
                rows.add(new AlgorithmCompletion("Greedy Dispatch", batch.size(), greedyCompleted, dateRun));

                int dpCompleted = DynamicProgrammingOptimizer.optimise(batch, 10).size();
                rows.add(new AlgorithmCompletion("DP Optimisation", batch.size(), dpCompleted, dateRun));
            }
            return rows;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to run completion comparison from CSV", exception);
        }
    }

    private List<Resource> benchmarkResources(int batchSize) {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource(1, "Benchmark Truck", Math.max(10, batchSize), 1, true));
        return resources;
    }

    private List<Integer> configuredBatchSizes() {
        List<Integer> sizes = new ArrayList<>();
        sizes.add(10);
        sizes.add(50);
        sizes.add(100);
        sizes.add(200);
        sizes.add(500);
        return sizes;
    }

    private List<Integer> resolveBatchSizes(int totalRequests) {
        List<Integer> sizes = new ArrayList<>();
        if (totalRequests <= 0) {
            return sizes;
        }
        for (int batchSize : configuredBatchSizes()) {
            if (batchSize <= totalRequests) {
                sizes.add(batchSize);
            }
        }
        if (sizes.isEmpty()) {
            sizes.add(totalRequests);
        }
        return sizes;
    }
}