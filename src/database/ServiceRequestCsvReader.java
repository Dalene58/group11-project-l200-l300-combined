package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import data.ServiceRequest;

public class ServiceRequestCsvReader {
    public List<ServiceRequest> readRequests(Path csvPath) throws IOException {
        return readFirstNRequests(csvPath, Integer.MAX_VALUE);
    }

    public List<ServiceRequest> readFirstNRequests(Path csvPath, int limit) throws IOException {
        List<ServiceRequest> requests = new ArrayList<>();
        if (limit <= 0) {
            return requests;
        }
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                String[] parts = parseCsv(line);
                if (parts.length == 0 || !"request".equalsIgnoreCase(parts[0].trim())) {
                    continue;
                }
                requests.add(toServiceRequest(parts));
                if (requests.size() >= limit) {
                    break;
                }
            }
        }
        return requests;
    }

    private ServiceRequest toServiceRequest(String[] parts) {
        if (parts.length < 10) {
            throw new IllegalArgumentException("Invalid request record: expected 10 CSV fields");
        }

        int requestId = extractNumericId(parts[1]);
        int locationId = extractNumericId(parts[2]);
        String requestType = parts[4].trim();
        int volume = Math.max(1, (int) Math.ceil(Double.parseDouble(parts[5].trim())));
        int urgency = urgencyScore(parts[6]);
        boolean fulfilled = "COMPLETED".equalsIgnoreCase(parts[9].trim());

        return new ServiceRequest(requestId, locationId, requestType, urgency, volume, fulfilled);
    }

    private int extractNumericId(String rawValue) {
        String digits = rawValue.replaceAll("[^0-9]", "");
        if (!digits.isEmpty()) {
            return Integer.parseInt(digits);
        }
        return Math.abs(rawValue.hashCode());
    }

    private int urgencyScore(String urgencyText) {
        return switch (urgencyText.trim().toUpperCase()) {
            case "CRITICAL" -> 10;
            case "URGENT" -> 9;
            case "HIGH" -> 8;
            case "MEDIUM" -> 5;
            case "LOW" -> 3;
            default -> 1;
        };
    }

    private String[] parseCsv(String line) {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;
        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (character == '"') {
                if (quoted && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    value.append('"');
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                values.add(value.toString());
                value.setLength(0);
            } else {
                value.append(character);
            }
        }
        values.add(value.toString());
        return values.toArray(String[]::new);
    }
}