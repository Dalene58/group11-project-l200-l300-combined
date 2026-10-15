package database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import data.AlgorithmRun;

public class AlgorithmPlotDataExporter {
    private final AlgorithmRunRepository repository;

    public AlgorithmPlotDataExporter() {
        this(new AlgorithmRunRepository());
    }

    public AlgorithmPlotDataExporter(AlgorithmRunRepository repository) {
        this.repository = repository;
    }

    public void export(Connection connection, Path runtimeCsvPath, Path memoryCsvPath) throws SQLException, IOException {
        List<AlgorithmRun> runs = repository.findAllForAlgorithms(connection, "Greedy Dispatch", "DP Optimisation");
        writeRuntimeCsv(runtimeCsvPath, runs);
        writeMemoryCsv(memoryCsvPath, runs);
    }

    private void writeRuntimeCsv(Path path, List<AlgorithmRun> runs) throws IOException {
        ensureParent(path);
        StringBuilder csv = new StringBuilder("algorithm_name,input_size,time_ns,date_run\n");
        for (AlgorithmRun run : runs) {
            csv.append(quote(run.getAlgorithmName())).append(',')
                    .append(run.getInputSize()).append(',')
                    .append(run.getTimeNs()).append(',')
                    .append(quote(run.getDateRun())).append('\n');
        }
        Files.writeString(path, csv.toString(), StandardCharsets.UTF_8);
    }

    private void writeMemoryCsv(Path path, List<AlgorithmRun> runs) throws IOException {
        ensureParent(path);
        StringBuilder csv = new StringBuilder("algorithm_name,input_size,memory_kb,date_run\n");
        for (AlgorithmRun run : runs) {
            csv.append(quote(run.getAlgorithmName())).append(',')
                    .append(run.getInputSize()).append(',')
                    .append(run.getMemoryKb()).append(',')
                    .append(quote(run.getDateRun())).append('\n');
        }
        Files.writeString(path, csv.toString(), StandardCharsets.UTF_8);
    }

    private void ensureParent(Path path) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
    }

    private String quote(String value) {
        return '"' + value.replace("\"", "\"\"") + '"';
    }
}