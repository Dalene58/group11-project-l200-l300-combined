package database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import data.AlgorithmRun;

public class AlgorithmRunCsvWriter {
    public void write(Path path, List<AlgorithmRun> runs) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        StringBuilder csv = new StringBuilder("run_id,algorithm_name,input_size,time_ns,memory_kb,date_run\n");
        for (AlgorithmRun run : runs) {
            csv.append(field(run.getRunId())).append(',')
                    .append(field(run.getAlgorithmName())).append(',')
                    .append(run.getInputSize()).append(',')
                    .append(run.getTimeNs()).append(',')
                    .append(run.getMemoryKb()).append(',')
                    .append(field(run.getDateRun())).append('\n');
        }
        Files.writeString(path, csv.toString(), StandardCharsets.UTF_8);
    }

    private String field(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}