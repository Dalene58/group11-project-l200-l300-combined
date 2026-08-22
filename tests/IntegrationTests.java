import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;
import java.util.List;

import org.junit.jupiter.api.Test;

import algorithms.BatchBenchmarkProcessor;
import data.AlgorithmRun;
import database.AlgorithmRunRepository;
import database.DataLoader;
import database.DatabaseConnector;
import database.SchemaInitializer;

public class IntegrationTests {
    @Test
    void schemaAndSeedLoadIntoSqlite() throws Exception {
        DatabaseConnector connector = new DatabaseConnector("jdbc:sqlite::memory:");
        SchemaInitializer initializer = new SchemaInitializer();
        DataLoader loader = new DataLoader();
        try (Connection connection = connector.connect()) {
            initializer.initialize(connection, Path.of("resources/schema.sql"));
            loader.load(connection, Path.of("resources/seed_data.csv"));
            assertEquals(20, count(connection, "locations"));
            assertEquals(29, count(connection, "roads"));
            assertEquals(600, count(connection, "service_requests"));
            assertEquals(30, count(connection, "resources"));
        }
    }

    @Test
    void batchBenchmarksPersistToAlgorithmRunsTable() throws Exception {
        DatabaseConnector connector = new DatabaseConnector("jdbc:sqlite::memory:");
        SchemaInitializer initializer = new SchemaInitializer();
        DataLoader loader = new DataLoader();
        BatchBenchmarkProcessor benchmarkProcessor = new BatchBenchmarkProcessor();
        AlgorithmRunRepository repository = new AlgorithmRunRepository();

        try (Connection connection = connector.connect()) {
            initializer.initialize(connection, Path.of("resources/schema.sql"));
            loader.load(connection, Path.of("resources/seed_data.csv"));

            int baselineCount = count(connection, "algorithm_runs");

            List<AlgorithmRun> runs = benchmarkProcessor.runBatches(Path.of("resources/seed_data.csv"));
            repository.saveAll(connection, runs);

            assertEquals(baselineCount + runs.size(), count(connection, "algorithm_runs"));
            assertEquals(10, runs.size());
            Set<Integer> sizes = runs.stream().map(AlgorithmRun::getInputSize).collect(java.util.stream.Collectors.toSet());
            assertEquals(Set.of(10, 50, 100, 200, 500), sizes);
            assertTrue(runs.stream().allMatch(run -> "Greedy Dispatch".equals(run.getAlgorithmName())
                    || "DP Optimisation".equals(run.getAlgorithmName())));
        }
    }

    private int count(Connection connection, String table) throws Exception {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + table)) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }
}
