package database.dao;

import database.DatabaseConnection;
import models.Resource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {

    // ==========================
    // INSERT
    // ==========================
    public boolean insert(Resource resource) {

        String sql = """
                INSERT INTO resources
                (resource_id, resource_type,
                 capacity_tons, availability_status,
                 home_location_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resource.getResourceId());
            ps.setString(2, resource.getResourceType());
            ps.setInt(3, resource.getCapacityTons());
            ps.setString(4, resource.getAvailabilityStatus());
            ps.setString(5, resource.getHomeLocationId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // GET BY ID
    // ==========================
    public Resource getById(String resourceId) {

        String sql = """
                SELECT *
                FROM resources
                WHERE resource_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resourceId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Resource(
                        rs.getString("resource_id"),
                        rs.getString("resource_type"),
                        rs.getInt("capacity_tons"),
                        rs.getString("availability_status"),
                        rs.getString("home_location_id")
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
    public List<Resource> getAll() {

        List<Resource> resources = new ArrayList<>();

        String sql = "SELECT * FROM resources";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                resources.add(new Resource(
                        rs.getString("resource_id"),
                        rs.getString("resource_type"),
                        rs.getInt("capacity_tons"),
                        rs.getString("availability_status"),
                        rs.getString("home_location_id")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resources;
    }

    // ==========================
    // GET AVAILABLE RESOURCES
    // ==========================
    public List<Resource> getAvailableResources() {

        List<Resource> resources = new ArrayList<>();

        String sql = """
                SELECT *
                FROM resources
                WHERE availability_status = 'Available'
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                resources.add(new Resource(
                        rs.getString("resource_id"),
                        rs.getString("resource_type"),
                        rs.getInt("capacity_tons"),
                        rs.getString("availability_status"),
                        rs.getString("home_location_id")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resources;
    }

    // ==========================
    // GET BY TYPE
    // ==========================
    public List<Resource> getByType(String resourceType) {

        List<Resource> resources = new ArrayList<>();

        String sql = """
                SELECT *
                FROM resources
                WHERE resource_type = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resourceType);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                resources.add(new Resource(
                        rs.getString("resource_id"),
                        rs.getString("resource_type"),
                        rs.getInt("capacity_tons"),
                        rs.getString("availability_status"),
                        rs.getString("home_location_id")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resources;
    }

    // ==========================
    // GET BY LOCATION
    // ==========================
    public List<Resource> getByLocation(String locationId) {

        List<Resource> resources = new ArrayList<>();

        String sql = """
                SELECT *
                FROM resources
                WHERE home_location_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, locationId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                resources.add(new Resource(
                        rs.getString("resource_id"),
                        rs.getString("resource_type"),
                        rs.getInt("capacity_tons"),
                        rs.getString("availability_status"),
                        rs.getString("home_location_id")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resources;
    }

    // ==========================
    // UPDATE AVAILABILITY
    // ==========================
    public boolean updateAvailability(String resourceId, String status) {

        String sql = """
                UPDATE resources
                SET availability_status = ?
                WHERE resource_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, resourceId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // UPDATE
    // ==========================
    public boolean update(Resource resource) {

        String sql = """
                UPDATE resources
                SET
                    resource_type = ?,
                    capacity_tons = ?,
                    availability_status = ?,
                    home_location_id = ?
                WHERE resource_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resource.getResourceType());
            ps.setInt(2, resource.getCapacityTons());
            ps.setString(3, resource.getAvailabilityStatus());
            ps.setString(4, resource.getHomeLocationId());
            ps.setString(5, resource.getResourceId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // DELETE
    // ==========================
    public boolean delete(String resourceId) {

        String sql = """
                DELETE FROM resources
                WHERE resource_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resourceId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // COUNT
    // ==========================
    public int count() {

        String sql = "SELECT COUNT(*) AS total FROM resources";

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

    // ==========================
    // COUNT AVAILABLE
    // ==========================
    public int countAvailableResources() {

        String sql = """
                SELECT COUNT(*) AS total
                FROM resources
                WHERE availability_status='Available'
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