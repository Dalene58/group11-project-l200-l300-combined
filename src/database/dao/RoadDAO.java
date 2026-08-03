package database.dao;

import database.DatabaseConnection;
import models.Road;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoadDAO {

    // ==========================
    // INSERT
    // ==========================
    public boolean insert(Road road) {

        String sql = """
                INSERT INTO roads
                (road_id, from_location_id, to_location_id,
                 distance_km, travel_time_min, condition_weight)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, road.getRoadId());
            ps.setString(2, road.getFromLocationId());
            ps.setString(3, road.getToLocationId());
            ps.setDouble(4, road.getDistanceKm());
            ps.setInt(5, road.getTravelTimeMin());
            ps.setDouble(6, road.getConditionWeight());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // GET BY ID
    // ==========================
    public Road getById(String roadId) {

        String sql = """
                SELECT *
                FROM roads
                WHERE road_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roadId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Road(
                        rs.getString("road_id"),
                        rs.getString("from_location_id"),
                        rs.getString("to_location_id"),
                        rs.getDouble("distance_km"),
                        rs.getInt("travel_time_min"),
                        rs.getDouble("condition_weight")
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
    public List<Road> getAll() {

        List<Road> roads = new ArrayList<>();

        String sql = "SELECT * FROM roads";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                roads.add(new Road(
                        rs.getString("road_id"),
                        rs.getString("from_location_id"),
                        rs.getString("to_location_id"),
                        rs.getDouble("distance_km"),
                        rs.getInt("travel_time_min"),
                        rs.getDouble("condition_weight")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roads;
    }

    // ==========================
    // GET ROADS FROM A LOCATION
    // ==========================
    public List<Road> getRoadsFromLocation(String locationId) {

        List<Road> roads = new ArrayList<>();

        String sql = """
                SELECT *
                FROM roads
                WHERE from_location_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, locationId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                roads.add(new Road(
                        rs.getString("road_id"),
                        rs.getString("from_location_id"),
                        rs.getString("to_location_id"),
                        rs.getDouble("distance_km"),
                        rs.getInt("travel_time_min"),
                        rs.getDouble("condition_weight")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roads;
    }

    // ==========================
    // UPDATE
    // ==========================
    public boolean update(Road road) {

        String sql = """
                UPDATE roads
                SET
                    from_location_id = ?,
                    to_location_id = ?,
                    distance_km = ?,
                    travel_time_min = ?,
                    condition_weight = ?
                WHERE road_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, road.getFromLocationId());
            ps.setString(2, road.getToLocationId());
            ps.setDouble(3, road.getDistanceKm());
            ps.setInt(4, road.getTravelTimeMin());
            ps.setDouble(5, road.getConditionWeight());
            ps.setString(6, road.getRoadId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // DELETE
    // ==========================
    public boolean delete(String roadId) {

        String sql = """
                DELETE FROM roads
                WHERE road_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roadId);

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
                FROM roads
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