import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

import database.DatabaseConnector;

public class SmokeTests {
    @Test
    void mainLoadDatasetCreatesExpectedTables() throws Exception {
        Path databaseFile = Path.of("resources/waste-optimizer.db");
        Files.deleteIfExists(databaseFile);

        try {
            Main.main(new String[] {"load-dataset"});

            DatabaseConnector connector = new DatabaseConnector("jdbc:sqlite:resources/waste-optimizer.db");
            try (Connection connection = connector.connect()) {
                assertEquals(3, count(connection, "locations"));
                assertEquals(3, count(connection, "roads"));
                assertEquals(4, count(connection, "service_requests"));
                assertEquals(3, count(connection, "resources"));
            }
        } finally {
            Files.deleteIfExists(databaseFile);
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