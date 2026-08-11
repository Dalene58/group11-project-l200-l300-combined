import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

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
            assertEquals(3, count(connection, "locations"));
            assertEquals(3, count(connection, "roads"));
            assertEquals(4, count(connection, "service_requests"));
            assertEquals(3, count(connection, "resources"));
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
