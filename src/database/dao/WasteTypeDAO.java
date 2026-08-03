package database.dao;

import database.DatabaseConnection;
import models.WasteType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WasteTypeDAO {

    // ==========================
    // INSERT
    // ==========================
    public boolean insert(WasteType wasteType) {

        String sql = """
                INSERT INTO waste_types
                (waste_type_id, name, description)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, wasteType.getWasteTypeId());
            ps.setString(2, wasteType.getName());
            ps.setString(3, wasteType.getDescription());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // GET BY ID
    // ==========================
    public WasteType getById(String wasteTypeId) {

        String sql = """
                SELECT *
                FROM waste_types
                WHERE waste_type_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, wasteTypeId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new WasteType(
                        rs.getString("waste_type_id"),
                        rs.getString("name"),
                        rs.getString("description")
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
    public List<WasteType> getAll() {

        List<WasteType> wasteTypes = new ArrayList<>();

        String sql = """
                SELECT *
                FROM waste_types
                ORDER BY name
                """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                wasteTypes.add(new WasteType(
                        rs.getString("waste_type_id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wasteTypes;
    }

    // ==========================
    // UPDATE
    // ==========================
    public boolean update(WasteType wasteType) {

        String sql = """
                UPDATE waste_types
                SET
                    name = ?,
                    description = ?
                WHERE waste_type_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, wasteType.getName());
            ps.setString(2, wasteType.getDescription());
            ps.setString(3, wasteType.getWasteTypeId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // DELETE
    // ==========================
    public boolean delete(String wasteTypeId) {

        String sql = """
                DELETE FROM waste_types
                WHERE waste_type_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, wasteTypeId);

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
                FROM waste_types
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