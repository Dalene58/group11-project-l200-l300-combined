package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class DataLoader {
    public void load(Connection connection, Path csvPath) throws SQLException, IOException {
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            load(connection, reader);
        }
    }

    public void load(Connection connection, InputStream csvStream) throws SQLException, IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8))) {
            load(connection, reader);
        }
    }

    private void load(Connection connection, BufferedReader reader) throws SQLException, IOException {
        boolean originalAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        int lineNumber = 0;
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }
                String[] values = parseCsv(line);
                String type = values[0].trim().toLowerCase();
                String sql = switch (type) {
                    case "location" -> "INSERT INTO locations VALUES (?, ?, ?, ?, ?, ?)";
                    case "road" -> "INSERT INTO roads VALUES (?, ?, ?, ?, ?, ?)";
                    case "waste_type" -> "INSERT INTO waste_types VALUES (?, ?, ?)";
                    case "disposal_site" -> "INSERT INTO disposal_sites VALUES (?, ?, ?, ?, ?, ?)";
                    case "resource" -> "INSERT INTO resources VALUES (?, ?, ?, ?, ?)";
                    case "driver" -> "INSERT INTO drivers VALUES (?, ?, ?, ?, ?, ?)";
                    case "request" -> "INSERT INTO collection_requests VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    case "assignment" -> "INSERT INTO assignments VALUES (?, ?, ?, ?, ?, ?, ?)";
                    case "algorithm_run" -> "INSERT INTO algorithm_runs VALUES (?, ?, ?, ?, ?, ?)";
                    case "audit_event" -> "INSERT INTO audit_events VALUES (?, ?, ?, ?)";
                    default -> throw new IllegalArgumentException("Unknown CSV record type: " + type);
                };
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    if (values.length != sqlParameterCount(sql) + 1) {
                        throw new IllegalArgumentException("Wrong field count");
                    }
                    for (int index = 1; index < values.length; index++) {
                        statement.setString(index, values[index].trim());
                    }
                    statement.executeUpdate();
                }
            }
            connection.commit();
        } catch (RuntimeException | SQLException exception) {
            connection.rollback();
            throw new IllegalArgumentException("Invalid CSV record at line " + lineNumber, exception);
        } finally {
            connection.setAutoCommit(originalAutoCommit);
        }
    }

    private int sqlParameterCount(String sql) {
        return (int) sql.chars().filter(character -> character == '?').count();
    }

    private String[] parseCsv(String line) {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (character == '"') {
                if (quoted && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    value.append('"');
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(character);
            }
        }
        values.add(value.toString());
        return values.toArray(String[]::new);
    }
}
