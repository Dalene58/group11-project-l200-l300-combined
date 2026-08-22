package ui;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import algorithms.DynamicProgrammingOptimizer;
import algorithms.GreedyOptimizer;
import algorithms.BatchBenchmarkProcessor;
import data.AlgorithmCompletion;
import data.AlgorithmRun;
import data.Resource;
import data.ServiceRequest;
import database.DataLoader;
import database.AlgorithmRunCsvWriter;
import database.AlgorithmRunRepository;
import database.AlgorithmPlotDataExporter;
import database.CompletionComparisonCsvWriter;
import database.DatabaseConnector;
import database.SchemaInitializer;

public class DemoRunner {
    private final DatabaseConnector connector;
    private final SchemaInitializer schemaInitializer;
    private final DataLoader dataLoader;
    private final ConsoleMenu menu;
    private final Scanner input;

    public DemoRunner() {
        this(new DatabaseConnector(), new SchemaInitializer(), new DataLoader(),
                new ConsoleMenu(System.out), new Scanner(System.in));
    }

    public DemoRunner(DatabaseConnector connector, SchemaInitializer schemaInitializer, DataLoader dataLoader, ConsoleMenu menu) {
        this(connector, schemaInitializer, dataLoader, menu, new Scanner(System.in));
    }

    public DemoRunner(DatabaseConnector connector, SchemaInitializer schemaInitializer, DataLoader dataLoader,
            ConsoleMenu menu, Scanner input) {
        this.connector = connector;
        this.schemaInitializer = schemaInitializer;
        this.dataLoader = dataLoader;
        this.menu = menu;
        this.input = input;
    }

    public void run() {
        List<ServiceRequest> requests = sampleRequests();
        List<Resource> resources = sampleResources();
        List<AlgorithmRun> runs = new ArrayList<>();
        boolean loaded = false;
        boolean running = true;
        while (running) {
            menu.printMenu();
            menu.printPrompt();
            if (!input.hasNextLine()) {
                break;
            }
            switch (input.nextLine().trim()) {
                case "1" -> {
                    loadDataset();
                    loaded = true;
                    System.out.println("Dataset loaded successfully.");
                }
                case "2" -> {
                    if (requireLoaded(loaded)) {
                        menu.printRequests(requests);
                    }
                }
                case "3" -> {
                    if (requireLoaded(loaded)) {
                        runs.add(runGreedy(requests, resources));
                    }
                }
                case "4" -> {
                    if (requireLoaded(loaded)) {
                        runs.add(runDynamicProgramming(requests));
                    }
                }
                case "5" -> menu.printTraceStatus();
                case "6" -> menu.printPerformanceGraph(runs);
                case "7" -> {
                    if (requireLoaded(loaded)) {
                        List<AlgorithmRun> batchRuns = runBatchBenchmarks();
                        runs.addAll(batchRuns);
                        menu.printBatchBenchmarkSaved(batchRuns.size());
                        exportPlotData();
                        exportCompletionData();
                        menu.printPlotDataExported();
                    }
                }
                case "8" -> running = false;
                default -> System.out.println("Please choose an option from 1 to 8.");
            }
        }
        writeRuns(runs);
    }

    private boolean requireLoaded(boolean loaded) {
        if (!loaded) {
            System.out.println("Load the dataset first (option 1).");
        }
        return loaded;
    }

    private AlgorithmRun runGreedy(List<ServiceRequest> requests, List<Resource> resources) {
        AlgorithmRun run = algorithms.PerformanceAnalyzer.measure("Greedy Dispatch",
                () -> menu.printFulfilment("Greedy dispatch complete", GreedyOptimizer.dispatch(requests, resources)));
        return run;
    }

    private AlgorithmRun runDynamicProgramming(List<ServiceRequest> requests) {
        return algorithms.PerformanceAnalyzer.measure("DP Optimisation",
                () -> menu.printFulfilment("DP optimisation complete", DynamicProgrammingOptimizer.optimise(requests, 10)));
    }

    private List<AlgorithmRun> runBatchBenchmarks() {
        BatchBenchmarkProcessor processor = new BatchBenchmarkProcessor();
        List<AlgorithmRun> batchRuns = processor.runBatches(Path.of("resources/seed_data.csv"));
        try (Connection connection = connector.connect()) {
            new AlgorithmRunRepository().saveAll(connection, batchRuns);
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to persist batch benchmark runs", exception);
        }
        return batchRuns;
    }

    public void runPerformanceAnalysis() {
        loadDataset();
        List<AlgorithmRun> runs = runBatchBenchmarks();
        writeRuns(runs);
        exportPlotData();
        exportCompletionData();
        System.out.println("Performance analysis complete. Runs saved and plot CSVs exported.");
    }

    private void exportPlotData() {
        try (Connection connection = connector.connect()) {
            new AlgorithmPlotDataExporter().export(
                    connection,
                    Path.of("logs/runtime_vs_input.csv"),
                    Path.of("logs/memory_vs_input.csv"));
        } catch (SQLException | IOException exception) {
            throw new IllegalStateException("Failed to export plot data", exception);
        }
    }

    private void exportCompletionData() {
        try {
            BatchBenchmarkProcessor processor = new BatchBenchmarkProcessor();
            List<AlgorithmCompletion> rows = processor.runCompletionComparison(Path.of("resources/seed_data.csv"));
            new CompletionComparisonCsvWriter().write(Path.of("logs/completion_vs_input.csv"), rows);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to export completion comparison data", exception);
        }
    }

    private void writeRuns(List<AlgorithmRun> runs) {
        try {
            new AlgorithmRunCsvWriter().write(Path.of("logs/algorithm_runs.csv"), runs);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write algorithm logs", exception);
        }
    }

    public void loadDataset() {
        try (Connection connection = connector.connect()) {
            schemaInitializer.initialize(connection, Path.of("resources/schema.sql"));
            dataLoader.load(connection, Path.of("resources/seed_data.csv"));
        } catch (SQLException | IOException exception) {
            throw new IllegalStateException("Failed to load dataset", exception);
        }
    }

    private List<ServiceRequest> sampleRequests() {
        List<ServiceRequest> requests = new ArrayList<>();
        requests.add(new ServiceRequest(1, 1, "general", 10, 4, false));
        requests.add(new ServiceRequest(2, 2, "general", 8, 3, false));
        requests.add(new ServiceRequest(3, 3, "general", 7, 6, false));
        requests.add(new ServiceRequest(4, 2, "general", 5, 2, false));
        return requests;
    }

    private List<Resource> sampleResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource(1, "Truck 1", 5, 1, true));
        resources.add(new Resource(2, "Truck 2", 5, 1, true));
        resources.add(new Resource(3, "Truck 3", 5, 1, true));
        return resources;
    }
}
