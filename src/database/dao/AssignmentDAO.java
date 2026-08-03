package database.dao;

import database.DatabaseConnection;
import models.Assignment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {

    // ==========================================
    // INSERT
    // ==========================================
    public boolean insert(Assignment assignment) {

        String sql = """
                INSERT INTO assignments
                (assignment_id,
                 request_id,
                 resource_id,
                 driver_id,
                 assigned_time,
                 completion_time,
                 assignment_status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, assignment.getAssignmentId());
            ps.setString(2, assignment.getRequestId());
            ps.setString(3, assignment.getResourceId());
            ps.setString(4, assignment.getDriverId());
            ps.setString(5, assignment.getAssignedTime().toString());

            if (assignment.getCompletionTime() == null) {
                ps.setNull(6, Types.VARCHAR);
            } else {
                ps.setString(6, assignment.getCompletionTime().toString());
            }

            ps.setString(7, assignment.getAssignmentStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // GET BY ID
    // ==========================================
    public Assignment getById(String assignmentId) {

        String sql = """
                SELECT *
                FROM assignments
                WHERE assignment_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, assignmentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==========================================
    // GET ALL
    // ==========================================
    public List<Assignment> getAll() {

        List<Assignment> assignments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM assignments
                ORDER BY assigned_time DESC
                """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                assignments.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }

    // ==========================================
    // GET ASSIGNMENTS BY DRIVER
    // ==========================================
    public List<Assignment> getAssignmentsByDriver(String driverId) {

        List<Assignment> assignments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM assignments
                WHERE driver_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, driverId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                assignments.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }

    // ==========================================
    // GET ASSIGNMENTS BY RESOURCE
    // ==========================================
    public List<Assignment> getAssignmentsByResource(String resourceId) {

        List<Assignment> assignments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM assignments
                WHERE resource_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, resourceId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                assignments.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }

    // ==========================================
    // UPDATE STATUS
    // ==========================================
    public boolean updateStatus(String assignmentId, String status) {

        String sql = """
                UPDATE assignments
                SET assignment_status = ?
                WHERE assignment_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, assignmentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // COMPLETE ASSIGNMENT
    // ==========================================
    public boolean completeAssignment(String assignmentId) {

        String sql = """
                UPDATE assignments
                SET completion_time = ?,
                    assignment_status = 'Completed'
                WHERE assignment_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, LocalDateTime.now().toString());
            ps.setString(2, assignmentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // DELETE
    // ==========================================
    public boolean delete(String assignmentId) {

        String sql = """
                DELETE FROM assignments
                WHERE assignment_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, assignmentId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // COUNT
    // ==========================================
    public int count() {

        String sql = "SELECT COUNT(*) AS total FROM assignments";

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

    // ==========================================
    // RESULTSET MAPPER
    // ==========================================
    private Assignment mapResultSet(ResultSet rs)
            throws SQLException {

        String completion = rs.getString("completion_time");

        return new Assignment(

                rs.getString("assignment_id"),

                rs.getString("request_id"),

                rs.getString("resource_id"),

                rs.getString("driver_id"),

                LocalDateTime.parse(
                        rs.getString("assigned_time")),

                completion == null ?
                        null :
                        LocalDateTime.parse(completion),

                rs.getString("assignment_status")
        );
    }

}