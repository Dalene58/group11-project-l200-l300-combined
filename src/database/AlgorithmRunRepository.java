package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import data.AlgorithmRun;

public class AlgorithmRunRepository {
    public void saveAll(Connection connection, List<AlgorithmRun> runs) throws SQLException {
        if (runs.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO algorithm_runs(run_id, algorithm_name, input_size, time_ns, memory_kb, date_run) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        boolean originalAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (AlgorithmRun run : runs) {
                statement.setString(1, run.getRunId());
                statement.setString(2, run.getAlgorithmName());
                statement.setInt(3, run.getInputSize());
                statement.setInt(4, run.getTimeNs());
                statement.setInt(5, run.getMemoryKb());
                statement.setString(6, run.getDateRun());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException exception) {
            connection.rollback();
            throw exception;
        } finally {
            connection.setAutoCommit(originalAutoCommit);
        }
    }

    public List<AlgorithmRun> findAllForAlgorithms(Connection connection, String firstAlgorithm, String secondAlgorithm)
            throws SQLException {
        String sql = "SELECT run_id, algorithm_name, input_size, time_ns, memory_kb, date_run "
                + "FROM algorithm_runs WHERE algorithm_name IN (?, ?) ORDER BY input_size ASC, date_run ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstAlgorithm);
            statement.setString(2, secondAlgorithm);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<AlgorithmRun> runs = new ArrayList<>();
                while (resultSet.next()) {
                    runs.add(new AlgorithmRun(
                            resultSet.getString("run_id"),
                            resultSet.getString("algorithm_name"),
                            resultSet.getInt("input_size"),
                            resultSet.getInt("time_ns"),
                            resultSet.getInt("memory_kb"),
                            resultSet.getString("date_run")));
                }
                return runs;
            }
        }
    }
}