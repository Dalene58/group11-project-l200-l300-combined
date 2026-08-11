package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        PreparedStatement locationStatement = null;
        PreparedStatement roadStatement = null;
        PreparedStatement requestStatement = null;
        PreparedStatement resourceStatement = null;
        try {
            locationStatement = connection.prepareStatement(
                "INSERT INTO locations(location_id, name, region, depot) VALUES (?, ?, ?, ?)");
            roadStatement = connection.prepareStatement(
                "INSERT INTO roads(road_id, from_location_id, to_location_id, distance_km) VALUES (?, ?, ?, ?)");
            requestStatement = connection.prepareStatement(
                "INSERT INTO service_requests(request_id, location_id, request_type, urgency, volume, fulfilled) VALUES (?, ?, ?, ?, ?, ?)");
            resourceStatement = connection.prepareStatement(
                "INSERT INTO resources(resource_id, name, capacity, current_location_id, available) VALUES (?, ?, ?, ?, ?)");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 0 || parts[0].trim().isEmpty() || parts[0].startsWith("#")) {
                    continue;
                }

                String recordType = parts[0].trim();
                if ("location".equals(recordType)) {
                    locationStatement.setInt(1, Integer.parseInt(parts[1].trim()));
                    locationStatement.setString(2, parts[2].trim());
                    locationStatement.setString(3, parts[3].trim());
                    locationStatement.setBoolean(4, Boolean.parseBoolean(parts[4].trim()));
                    locationStatement.executeUpdate();
                } else if ("road".equals(recordType)) {
                    roadStatement.setInt(1, Integer.parseInt(parts[1].trim()));
                    roadStatement.setInt(2, Integer.parseInt(parts[2].trim()));
                    roadStatement.setInt(3, Integer.parseInt(parts[3].trim()));
                    roadStatement.setDouble(4, Double.parseDouble(parts[4].trim()));
                    roadStatement.executeUpdate();
                } else if ("request".equals(recordType)) {
                    requestStatement.setInt(1, Integer.parseInt(parts[1].trim()));
                    requestStatement.setInt(2, Integer.parseInt(parts[2].trim()));
                    requestStatement.setString(3, parts[3].trim());
                    requestStatement.setInt(4, Integer.parseInt(parts[4].trim()));
                    requestStatement.setInt(5, Integer.parseInt(parts[5].trim()));
                    requestStatement.setBoolean(6, Boolean.parseBoolean(parts[6].trim()));
                    requestStatement.executeUpdate();
                } else if ("resource".equals(recordType)) {
                    resourceStatement.setInt(1, Integer.parseInt(parts[1].trim()));
                    resourceStatement.setString(2, parts[2].trim());
                    resourceStatement.setInt(3, Integer.parseInt(parts[3].trim()));
                    resourceStatement.setInt(4, Integer.parseInt(parts[4].trim()));
                    resourceStatement.setBoolean(5, Boolean.parseBoolean(parts[5].trim()));
                    resourceStatement.executeUpdate();
                }
            }
        } finally {
            if (resourceStatement != null) {
                resourceStatement.close();
            }
            if (requestStatement != null) {
                requestStatement.close();
            }
            if (roadStatement != null) {
                roadStatement.close();
            }
            if (locationStatement != null) {
                locationStatement.close();
            }
        }
    }
}
