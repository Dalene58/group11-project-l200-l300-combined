package database.dao;

import database.DatabaseConnection;
import models.CollectionRequest;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CollectionRequestDAO {

    // ==========================
    // INSERT
    // ==========================
    public boolean insert(CollectionRequest request) {

        String sql = """
                INSERT INTO collection_requests
                (request_id,
                 source_location_id,
                 disposal_site_id,
                 waste_type_id,
                 estimated_weight,
                 urgency,
                 time_submitted,
                 deadline,
                 status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, request.getRequestId());
            ps.setString(2, request.getSourceLocationId());
            ps.setString(3, request.getDisposalSiteId());
            ps.setString(4, request.getWasteTypeId());
            ps.setDouble(5, request.getEstimatedWeight());
            ps.setString(6, request.getUrgency());
            ps.setString(7, request.getTimeSubmitted().toString());
            ps.setString(8, request.getDeadline().toString());
            ps.setString(9, request.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // GET BY ID
    // ==========================
    public CollectionRequest getById(String requestId) {

        String sql = """
                SELECT *
                FROM collection_requests
                WHERE request_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, requestId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return mapResultSet(rs);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

    // ==========================
    // GET ALL
    // ==========================
    public List<CollectionRequest> getAll() {

        List<CollectionRequest> requests = new ArrayList<>();

        String sql = """
                SELECT *
                FROM collection_requests
                ORDER BY time_submitted
                """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                requests.add(mapResultSet(rs));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return requests;

    }

    // ==========================
    // GET PENDING REQUESTS
    // ==========================
    public List<CollectionRequest> getPendingRequests() {

        return getRequestsByStatus("Pending");

    }

    // ==========================
    // GET ASSIGNED REQUESTS
    // ==========================
    public List<CollectionRequest> getAssignedRequests() {

        return getRequestsByStatus("Assigned");

    }

    // ==========================
    // GET COMPLETED REQUESTS
    // ==========================
    public List<CollectionRequest> getCompletedRequests() {

        return getRequestsByStatus("Completed");

    }

    // ==========================
    // GET BY STATUS
    // ==========================
    public List<CollectionRequest> getRequestsByStatus(String status) {

        List<CollectionRequest> requests = new ArrayList<>();

        String sql = """
                SELECT *
                FROM collection_requests
                WHERE status = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                requests.add(mapResultSet(rs));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return requests;

    }

    // ==========================
    // GET BY URGENCY
    // ==========================
    public List<CollectionRequest> getRequestsByUrgency(String urgency) {

        List<CollectionRequest> requests = new ArrayList<>();

        String sql = """
                SELECT *
                FROM collection_requests
                WHERE urgency = ?
                ORDER BY deadline
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, urgency);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                requests.add(mapResultSet(rs));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return requests;

    }

    // ==========================
    // GET BY LOCATION
    // ==========================
    public List<CollectionRequest> getRequestsByLocation(String locationId) {

        List<CollectionRequest> requests = new ArrayList<>();

        String sql = """
                SELECT *
                FROM collection_requests
                WHERE source_location_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, locationId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                requests.add(mapResultSet(rs));

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return requests;

    }

    // ==========================
    // UPDATE STATUS
    // ==========================
    public boolean updateStatus(String requestId, String status) {

        String sql = """
                UPDATE collection_requests
                SET status = ?
                WHERE request_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, requestId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // UPDATE
    // ==========================
    public boolean update(CollectionRequest request) {

        String sql = """
                UPDATE collection_requests
                SET
                    source_location_id=?,
                    disposal_site_id=?,
                    waste_type_id=?,
                    estimated_weight=?,
                    urgency=?,
                    time_submitted=?,
                    deadline=?,
                    status=?
                WHERE request_id=?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, request.getSourceLocationId());
            ps.setString(2, request.getDisposalSiteId());
            ps.setString(3, request.getWasteTypeId());
            ps.setDouble(4, request.getEstimatedWeight());
            ps.setString(5, request.getUrgency());
            ps.setString(6, request.getTimeSubmitted().toString());
            ps.setString(7, request.getDeadline().toString());
            ps.setString(8, request.getStatus());
            ps.setString(9, request.getRequestId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

    // ==========================
    // DELETE
    // ==========================
    public boolean delete(String requestId) {

        String sql = """
                DELETE FROM collection_requests
                WHERE request_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, requestId);

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

        String sql = "SELECT COUNT(*) total FROM collection_requests";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next())
                return rs.getInt("total");

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;

    }

    // ==========================
    // PRIVATE MAPPER
    // ==========================
    private CollectionRequest mapResultSet(ResultSet rs)
            throws SQLException {

        return new CollectionRequest(

                rs.getString("request_id"),

                rs.getString("source_location_id"),

                rs.getString("disposal_site_id"),

                rs.getString("waste_type_id"),

                rs.getDouble("estimated_weight"),

                rs.getString("urgency"),

                LocalDateTime.parse(
                        rs.getString("time_submitted")),

                LocalDateTime.parse(
                        rs.getString("deadline")),

                rs.getString("status")

        );

    }

}