package ui;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import algorithms.DynamicProgrammingOptimizer;
import algorithms.GreedyOptimizer;
import data.AlgorithmRun;
import data.Resource;
import data.ServiceRequest;
import database.DataLoader;
import database.DatabaseConnector;
import database.SchemaInitializer;

public class DemoRunner {
    private final DatabaseConnector connector;
    private final SchemaInitializer schemaInitializer;
    private final DataLoader dataLoader;
    private final ConsoleMenu menu;

    public DemoRunner() {
        this(new DatabaseConnector(), new SchemaInitializer(), new DataLoader(), new ConsoleMenu(System.out));
    }

    public DemoRunner(DatabaseConnector connector, SchemaInitializer schemaInitializer, DataLoader dataLoader, ConsoleMenu menu) {
        this.connector = connector;
        this.schemaInitializer = schemaInitializer;
        this.dataLoader = dataLoader;
        this.menu = menu;
    }

    public void run() {
        menu.printMenu();
        List<ServiceRequest> requests = sampleRequests();
        List<Resource> resources = sampleResources();
        menu.printRequests(requests);
        menu.printFulfilment("Greedy dispatch complete", GreedyOptimizer.dispatch(requests, resources));
        menu.printFulfilment("DP optimisation complete", DynamicProgrammingOptimizer.optimise(requests, 10));
        menu.printPerformanceGraph(List.of(
                new AlgorithmRun("Greedy Dispatch", 2_000_000L, 1024L),
                new AlgorithmRun("DP Optimisation", 4_000_000L, 2048L)));
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
