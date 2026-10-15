package database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import data.AlgorithmCompletion;

public class CompletionComparisonCsvWriter {
    public void write(Path path, List<AlgorithmCompletion> rows) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        StringBuilder csv = new StringBuilder("algorithm_name,input_size,completed_requests,date_run\n");
        for (AlgorithmCompletion row : rows) {
            csv.append(quote(row.getAlgorithmName())).append(',')
                    .append(row.getInputSize()).append(',')
                    .append(row.getCompletedRequests()).append(',')
                    .append(quote(row.getDateRun())).append('\n');
        }
        Files.writeString(path, csv.toString(), StandardCharsets.UTF_8);
    }

    private String quote(String value) {
        return '"' + value.replace("\"", "\"\"") + '"';
    }
}