package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Path to the SQLite database
    private static final String DB_URL = "jdbc:sqlite:database/waste_management.db";

    /**
     * Establishes and returns a connection to the SQLite database.
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection connect() throws SQLException {

        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Safely closes a database connection.
     *
     * @param connection the connection to close
     */
    public static void close(Connection connection) {

        if (connection != null) {

            try {

                connection.close();

            } catch (SQLException e) {

                e.printStackTrace();

            }

        }

    }
}