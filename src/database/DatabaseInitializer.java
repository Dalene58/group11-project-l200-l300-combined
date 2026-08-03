package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {

        try (
                Connection connection = DatabaseConnection.connect();
                Statement statement = connection.createStatement()
        ) {

            InputStream inputStream =
                    DatabaseInitializer.class.getClassLoader()
                            .getResourceAsStream("sql/schema.sql");

            if (inputStream == null) {
                System.out.println("schema.sql not found!");
                return;
            }

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("--") || line.isEmpty()) {
                    continue;
                }

                sql.append(line).append(" ");

                if (line.endsWith(";")) {

                    statement.execute(sql.toString());

                    sql.setLength(0);

                }
            }

            System.out.println("Database initialized successfully.");

        }

        catch (SQLException | IOException e) {

            e.printStackTrace();

        }

    }

}