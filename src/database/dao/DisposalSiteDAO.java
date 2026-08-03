package database.dao;

import database.DatabaseConnection;
import models.DisposalSite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisposalSiteDAO {

    // ==========================
    // INSERT
    // ==========================
    public boolean insert(DisposalSite site) {

        String sql = """
                INSERT INTO disposal_sites
                (site_id, name, location_id,
                 site_type, capacity_tons, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, site.getSiteId());
            ps.setString(2, site.getName());
            ps.setString(3, site.getLocationId());
            ps.setString(4, site.getSiteType());
            ps.setInt(5, site.getCapacityTons());
            ps.setString(6, site.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // GET BY ID
    // ==========================
    public DisposalSite getById(String siteId) {

        String sql = """
                SELECT *
                FROM disposal_sites
                WHERE site_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, siteId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new DisposalSite(
                        rs.getString("site_id"),
                        rs.getString("name"),
                        rs.getString("location_id"),
                        rs.getString("site_type"),
                        rs.getInt("capacity_tons"),
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
    public List<DisposalSite> getAll() {

        List<DisposalSite> sites = new ArrayList<>();

        String sql = "SELECT * FROM disposal_sites ORDER BY name";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                sites.add(new DisposalSite(
                        rs.getString("site_id"),
                        rs.getString("name"),
                        rs.getString("location_id"),
                        rs.getString("site_type"),
                        rs.getInt("capacity_tons"),
                        rs.getString("status")
                ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sites;
    }

    // ==========================
    // UPDATE
    // ==========================
    public boolean update(DisposalSite site) {

        String sql = """
                UPDATE disposal_sites
                SET
                    name = ?,
                    location_id = ?,
                    site_type = ?,
                    capacity_tons = ?,
                    status = ?
                WHERE site_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, site.getName());
            ps.setString(2, site.getLocationId());
            ps.setString(3, site.getSiteType());
            ps.setInt(4, site.getCapacityTons());
            ps.setString(5, site.getStatus());
            ps.setString(6, site.getSiteId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // DELETE
    // ==========================
    public boolean delete(String siteId) {

        String sql = "DELETE FROM disposal_sites WHERE site_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, siteId);

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

        String sql = "SELECT COUNT(*) AS total FROM disposal_sites";

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