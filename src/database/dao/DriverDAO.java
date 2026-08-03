package database.dao;

import database.DatabaseConnection;
import models.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {

    // ==========================
    // INSERT
    // ==========================
    public boolean insert(Driver driver) {

        String sql = """
                INSERT INTO drivers
                (driver_id, first_name, last_name,
                 phone, license_number, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driver.getDriverId());
            ps.setString(2, driver.getFirstName());
            ps.setString(3, driver.getLastName());
            ps.setString(4, driver.getPhone());
            ps.setString(5, driver.getLicenseNumber());
            ps.setString(6, driver.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // GET BY ID
    // ==========================
    public Driver getById(String driverId) {

        String sql = """
                SELECT *
                FROM drivers
                WHERE driver_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driverId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Driver(
                        rs.getString("driver_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("license_number"),
                        rs.getString("status")
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
    public List<Driver> getAll() {

        List<Driver> drivers = new ArrayList<>();

        String sql = "SELECT * FROM drivers ORDER BY last_name";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                drivers.add(new Driver(
                        rs.getString("driver_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("license_number"),
                        rs.getString("status")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return drivers;
    }

    // ==========================
    // GET ACTIVE DRIVERS
    // ==========================
    public List<Driver> getActiveDrivers() {

        List<Driver> drivers = new ArrayList<>();

        String sql = """
                SELECT *
                FROM drivers
                WHERE status = 'Active'
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                drivers.add(new Driver(
                        rs.getString("driver_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("license_number"),
                        rs.getString("status")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return drivers;
    }

    // ==========================
    // UPDATE STATUS
    // ==========================
    public boolean updateStatus(String driverId, String status) {

        String sql = """
                UPDATE drivers
                SET status = ?
                WHERE driver_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, driverId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // UPDATE
    // ==========================
    public boolean update(Driver driver) {

        String sql = """
                UPDATE drivers
                SET
                    first_name = ?,
                    last_name = ?,
                    phone = ?,
                    license_number = ?,
                    status = ?
                WHERE driver_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driver.getFirstName());
            ps.setString(2, driver.getLastName());
            ps.setString(3, driver.getPhone());
            ps.setString(4, driver.getLicenseNumber());
            ps.setString(5, driver.getStatus());
            ps.setString(6, driver.getDriverId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // DELETE
    // ==========================
    public boolean delete(String driverId) {

        String sql = """
                DELETE FROM drivers
                WHERE driver_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driverId);

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

        String sql = """
                SELECT COUNT(*) AS total
                FROM drivers
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