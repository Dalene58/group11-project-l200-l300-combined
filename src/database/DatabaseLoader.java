package database;

import database.utils.CSVReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseLoader {

    // ==========================================
    // LOAD EVERYTHING
    // ==========================================

    public static void loadAll() {

        loadLocations();
        loadRoads();
        loadWasteTypes();
        loadDisposalSites();
        loadResources();
        loadDrivers();
        loadCollectionRequests();
        loadAssignments();
        loadAlgorithmRuns();
        loadAuditEvents();

        System.out.println("All CSV files imported successfully.");

    }

    // ==========================================
    // LOAD LOCATIONS
    // ==========================================

    public static void loadLocations() {

        String sql = """
                INSERT INTO locations
                (location_id, name, area, location_type, latitude, longitude)
                
                VALUES(?,?,?,?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("locations.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);
                ps.setString(4, row[3]);
                ps.setDouble(5, Double.parseDouble(row[4]));
                ps.setDouble(6, Double.parseDouble(row[5]));

                ps.executeUpdate();

            }

            System.out.println("Locations Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ==========================================
    // LOAD ROADS
    // ==========================================

    public static void loadRoads() {

        String sql = """
                INSERT INTO roads
                (location_id, name, area, location_type, latitude, longitude)
                
                VALUES(?,?,?,?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("roads.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);
                ps.setDouble(4, Double.parseDouble(row[3]));
                ps.setInt(5, Integer.parseInt(row[4]));
                ps.setDouble(6, Double.parseDouble(row[5]));

                ps.executeUpdate();

            }

            System.out.println("Roads Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }
    // ==========================================
    // LOAD WASTE TYPES
    // ==========================================

    public static void loadWasteTypes() {

        String sql = """
                INSERT INTO waste_types
                (location_id, name, area, location_type, latitude, longitude)
                
                VALUES(?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("waste_types.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);

                ps.executeUpdate();

            }

            System.out.println("Waste Types Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ==========================================
    // LOAD DISPOSAL SITES
    // ==========================================

    public static void loadDisposalSites() {

        String sql = """
                INSERT INTO disposal_sites
                (location_id, name, area, location_type, latitude, longitude)
                
                VALUES(?,?,?,?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("disposal_sites.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);
                ps.setString(4, row[3]);
                ps.setInt(5, Integer.parseInt(row[4]));
                ps.setString(6, row[5]);

                ps.executeUpdate();

            }

            System.out.println("Disposal Sites Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ==========================================
    // LOAD RESOURCES
    // ==========================================

    public static void loadResources() {

        String sql = """
                INSERT INTO resources
                (location_id, name, area, location_type, latitude, longitude)
                
                VALUES(?,?,?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("resources.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);
                ps.setString(2, row[1]);
                ps.setInt(3, Integer.parseInt(row[2]));
                ps.setString(4, row[3]);
                ps.setString(5, row[4]);

                ps.executeUpdate();

            }

            System.out.println("Resources Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ==========================================
    // LOAD DRIVERS
    // ==========================================

    public static void loadDrivers() {

        String sql = """
                INSERT INTO drivers
                (location_id, name, area, location_type, latitude, longitude)
                
                VALUES(?,?,?,?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("drivers.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);
                ps.setString(4, row[3]);
                ps.setString(5, row[4]);
                ps.setString(6, row[5]);

                ps.executeUpdate();

            }

            System.out.println("Drivers Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ==========================================
    // LOAD COLLECTION REQUESTS
    // ==========================================

    public static void loadCollectionRequests() {

        String sql = """
                INSERT INTO collection_requests
                (location_id, name, area, location_type, latitude, longitude)
              
                VALUES(?,?,?,?,?,?,?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("collection_requests.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);   // request_id
                ps.setString(2, row[1]);   // source_location_id
                ps.setString(3, row[2]);   // disposal_site_id
                ps.setString(4, row[3]);   // waste_type_id
                ps.setDouble(5, Double.parseDouble(row[4])); // estimated_weight
                ps.setString(6, row[5]);   // urgency
                ps.setString(7, row[6]);   // time_submitted
                ps.setString(8, row[7]);   // deadline
                ps.setString(9, row[8]);   // status

                ps.executeUpdate();

            }

            System.out.println("Collection Requests Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ==========================================
    // LOAD ASSIGNMENTS
    // ==========================================

    public static void loadAssignments() {

        String sql = """
                INSERT INTO assignments
                VALUES(?,?,?,?,?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("assignments.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);   // assignment_id
                ps.setString(2, row[1]);   // request_id
                ps.setString(3, row[2]);   // resource_id
                ps.setString(4, row[3]);   // driver_id
                ps.setString(5, row[4]);   // assigned_time
                ps.setString(6, row[5]);   // completion_time
                ps.setString(7, row[6]);   // assignment_status

                ps.executeUpdate();

            }

            System.out.println("Assignments Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ==========================================
    // LOAD ALGORITHM RUNS
    // ==========================================

    public static void loadAlgorithmRuns() {

        String sql = """
                INSERT INTO algorithm_runs
                VALUES(?,?,?,?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("algorithm_runs.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);   // run_id
                ps.setString(2, row[1]);   // algorithm_name
                ps.setInt(3, Integer.parseInt(row[2])); // input_size
                ps.setLong(4, Long.parseLong(row[3]));  // time_ns
                ps.setInt(5, Integer.parseInt(row[4])); // memory_kb
                ps.setString(6, row[5]);   // date_run

                ps.executeUpdate();

            }

            System.out.println("Algorithm Runs Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

    // ==========================================
    // LOAD AUDIT EVENTS
    // ==========================================

    public static void loadAuditEvents() {

        String sql = """
                INSERT INTO audit_events
                VALUES(?,?,?,?)
                """;

        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            List<String[]> rows =
                    CSVReader.readCSV("audit_events.csv");

            for (String[] row : rows) {

                ps.setString(1, row[0]);   // audit_id
                ps.setString(2, row[1]);   // event_type
                ps.setString(3, row[2]);   // description
                ps.setString(4, row[3]);   // event_time

                ps.executeUpdate();

            }

            System.out.println("Audit Events Loaded");

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }

}