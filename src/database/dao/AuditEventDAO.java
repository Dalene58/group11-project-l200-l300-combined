package database.dao;

import database.DatabaseConnection;
import models.AuditEvent;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditEventDAO {

    // ==========================================
    // INSERT
    // ==========================================
    public boolean insert(AuditEvent event) {

        String sql = """
                INSERT INTO audit_events
                (audit_id,
                 event_type,
                 description,
                 event_time)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, event.getAuditId());
            ps.setString(2, event.getEventType());
            ps.setString(3, event.getDescription());
            ps.setString(4, event.getEventTime().toString());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;
    }

    // ==========================================
    // GET BY ID
    // ==========================================
    public AuditEvent getById(String auditId) {

        String sql = """
                SELECT *
                FROM audit_events
                WHERE audit_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, auditId);

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
    public List<AuditEvent> getAll() {

        List<AuditEvent> events = new ArrayList<>();

        String sql = """
                SELECT *
                FROM audit_events
                ORDER BY event_time DESC
                """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                events.add(mapResultSet(rs));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return events;

    }

    // ==========================================
    // GET BY EVENT TYPE
    // ==========================================
    public List<AuditEvent> getByEventType(String eventType) {

        List<AuditEvent> events = new ArrayList<>();

        String sql = """
                SELECT *
                FROM audit_events
                WHERE event_type = ?
                ORDER BY event_time DESC
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, eventType);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                events.add(mapResultSet(rs));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return events;

    }

    // ==========================================
    // UPDATE
    // ==========================================
    public boolean update(AuditEvent event) {

        String sql = """
                UPDATE audit_events
                SET
                    event_type = ?,
                    description = ?,
                    event_time = ?
                WHERE audit_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, event.getEventType());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getEventTime().toString());
            ps.setString(4, event.getAuditId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================================
    // DELETE
    // ==========================================
    public boolean delete(String auditId) {

        String sql = """
                DELETE FROM audit_events
                WHERE audit_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, auditId);

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

        String sql = """
                SELECT COUNT(*) AS total
                FROM audit_events
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

    // ==========================================
    // RESULTSET MAPPER
    // ==========================================
    private AuditEvent mapResultSet(ResultSet rs)
            throws SQLException {

        return new AuditEvent(

                rs.getString("audit_id"),

                rs.getString("event_type"),

                rs.getString("description"),

                LocalDateTime.parse(
                        rs.getString("event_time"))

        );

    }

}