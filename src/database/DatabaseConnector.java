package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private final String url;

    public DatabaseConnector() {
        this("jdbc:sqlite:resources/waste-optimizer.db");
    }

    public DatabaseConnector(String url) {
        this.url = url;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
