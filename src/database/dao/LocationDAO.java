package database.dao;

import database.DatabaseConnection;
import models.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    // ==========================
    // INSERT
    // ==========================
    public boolean insert(Location location) {

        String sql = """
                INSERT INTO locations
                (location_id, name, area, location_type, latitude, longitude)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, location.getLocationId());
            ps.setString(2, location.getName());
            ps.setString(3, location.getArea());
            ps.setString(4, location.getLocationType());
            ps.setDouble(5, location.getLatitude());
            ps.setDouble(6, location.getLongitude());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // GET BY ID
    // ==========================
    public Location getById(String locationId) {

        String sql = """
                SELECT *
                FROM locations
                WHERE location_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, locationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Location(
                        rs.getString("location_id"),
                        rs.getString("name"),
                        rs.getString("area"),
                        rs.getString("location_type"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==========================
    // GET ALL
    // ==========================
    public List<Location> getAll() {

        List<Location> locations = new ArrayList<>();

        String sql = """
                SELECT *
                FROM locations
                ORDER BY name
                """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                locations.add(new Location(

                        rs.getString("location_id"),

                        rs.getString("name"),

                        rs.getString("area"),

                        rs.getString("location_type"),

                        rs.getDouble("latitude"),

                        rs.getDouble("longitude")

                ));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return locations;

    }

    // ==========================
    // UPDATE
    // ==========================
    public boolean update(Location location) {

        String sql = """
                UPDATE locations
                SET
                    name = ?,
                    area = ?,
                    location_type = ?,
                    latitude = ?,
                    longitude = ?
                WHERE location_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, location.getName());
            ps.setString(2, location.getArea());
            ps.setString(3, location.getLocationType());
            ps.setDouble(4, location.getLatitude());
            ps.setDouble(5, location.getLongitude());
            ps.setString(6, location.getLocationId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // DELETE
    // ==========================
    public boolean delete(String locationId) {

        String sql = """
                DELETE FROM locations
                WHERE location_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, locationId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // SEARCH BY AREA
    // ==========================
    public List<Location> getByArea(String area) {

        List<Location> locations = new ArrayList<>();

        String sql = """
                SELECT *
                FROM locations
                WHERE area = ?
                ORDER BY name
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, area);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                locations.add(new Location(

                        rs.getString("location_id"),
                        rs.getString("name"),
                        rs.getString("area"),
                        rs.getString("location_type"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")

                ));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return locations;

    }

    // ==========================
    // COUNT
    // ==========================
    public int count() {

        String sql = """
                SELECT COUNT(*) AS total
                FROM locations
                """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {

                return rs.getInt("total");

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;

    }

}