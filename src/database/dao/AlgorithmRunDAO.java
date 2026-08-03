package database.dao;

import database.DatabaseConnection;
import models.AlgorithmRun;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmRunDAO {

    // ==========================================
    // INSERT
    // ==========================================
    public boolean insert(AlgorithmRun run) {

        String sql = """
                INSERT INTO algorithm_runs
                (run_id,
                 algorithm_name,
                 input_size,
                 time_ns,
                 memory_kb,
                 date_run)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, run.getRunId());
            ps.setString(2, run.getAlgorithmName());
            ps.setInt(3, run.getInputSize());
            ps.setLong(4, run.getTimeNs());
            ps.setInt(5, run.getMemoryKb());
            ps.setString(6, run.getDateRun().toString());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // GET BY ID
    // ==========================================
    public AlgorithmRun getById(String runId) {

        String sql = """
                SELECT *
                FROM algorithm_runs
                WHERE run_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, runId);

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
    public List<AlgorithmRun> getAll() {

        List<AlgorithmRun> runs = new ArrayList<>();

        String sql = """
                SELECT *
                FROM algorithm_runs
                ORDER BY date_run DESC
                """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                runs.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return runs;
    }

    // ==========================================
    // GET BY ALGORITHM
    // ==========================================
    public List<AlgorithmRun> getByAlgorithm(String algorithmName) {

        List<AlgorithmRun> runs = new ArrayList<>();

        String sql = """
                SELECT *
                FROM algorithm_runs
                WHERE algorithm_name = ?
                ORDER BY input_size
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, algorithmName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                runs.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return runs;
    }

    // ==========================================
    // UPDATE
    // ==========================================
    public boolean update(AlgorithmRun run) {

        String sql = """
                UPDATE algorithm_runs
                SET
                    algorithm_name = ?,
                    input_size = ?,
                    time_ns = ?,
                    memory_kb = ?,
                    date_run = ?
                WHERE run_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, run.getAlgorithmName());
            ps.setInt(2, run.getInputSize());
            ps.setLong(3, run.getTimeNs());
            ps.setInt(4, run.getMemoryKb());
            ps.setString(5, run.getDateRun().toString());
            ps.setString(6, run.getRunId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // DELETE
    // ==========================================
    public boolean delete(String runId) {

        String sql = """
                DELETE FROM algorithm_runs
                WHERE run_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, runId);

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

        String sql = "SELECT COUNT(*) AS total FROM algorithm_runs";

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
    private AlgorithmRun mapResultSet(ResultSet rs)
            throws SQLException {

        return new AlgorithmRun(

                rs.getString("run_id"),

                rs.getString("algorithm_name"),

                rs.getInt("input_size"),

                rs.getLong("time_ns"),

                rs.getInt("memory_kb"),

                LocalDateTime.parse(
                        rs.getString("date_run"))

        );
    }

}