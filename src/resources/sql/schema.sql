-- ==========================================================
-- Accra Municipal Smart Waste Management System
-- SQLite Database Schema
-- ==========================================================

PRAGMA foreign_keys = ON;

-- ==========================================================
-- LOCATIONS
-- ==========================================================

CREATE TABLE IF NOT EXISTS locations (

                                         location_id TEXT PRIMARY KEY,

                                         name TEXT NOT NULL,

                                         area TEXT NOT NULL,

                                         location_type TEXT NOT NULL,

                                         latitude REAL NOT NULL,

                                         longitude REAL NOT NULL

);

-- ==========================================================
-- ROADS
-- ==========================================================

CREATE TABLE IF NOT EXISTS roads (

                                     road_id TEXT PRIMARY KEY,

                                     from_location_id TEXT NOT NULL,

                                     to_location_id TEXT NOT NULL,

                                     distance_km REAL NOT NULL,

                                     travel_time_min INTEGER NOT NULL,

                                     condition_weight REAL NOT NULL,

                                     FOREIGN KEY (from_location_id)
    REFERENCES locations(location_id),

    FOREIGN KEY (to_location_id)
    REFERENCES locations(location_id)

    );

-- ==========================================================
-- WASTE TYPES
-- ==========================================================

CREATE TABLE IF NOT EXISTS waste_types (

                                           waste_type_id TEXT PRIMARY KEY,

                                           name TEXT NOT NULL UNIQUE,

                                           description TEXT

);

-- ==========================================================
-- DISPOSAL SITES
-- ==========================================================

CREATE TABLE IF NOT EXISTS disposal_sites (

                                              site_id TEXT PRIMARY KEY,

                                              name TEXT NOT NULL,

                                              location_id TEXT NOT NULL,

                                              site_type TEXT NOT NULL,

                                              capacity_tons INTEGER NOT NULL,

                                              status TEXT NOT NULL
                                              CHECK(status IN ('Open','Closed','Maintenance')),

    FOREIGN KEY (location_id)
    REFERENCES locations(location_id)

    );

-- ==========================================================
-- RESOURCES
-- ==========================================================

CREATE TABLE IF NOT EXISTS resources (

                                         resource_id TEXT PRIMARY KEY,

                                         resource_type TEXT NOT NULL,

                                         capacity_tons INTEGER NOT NULL,

                                         availability_status TEXT NOT NULL
                                         CHECK(availability_status IN
('Available','Busy','Maintenance')),

    home_location_id TEXT NOT NULL,

    FOREIGN KEY (home_location_id)
    REFERENCES locations(location_id)

    );

-- ==========================================================
-- DRIVERS
-- ==========================================================

CREATE TABLE IF NOT EXISTS drivers (

                                       driver_id TEXT PRIMARY KEY,

                                       first_name TEXT NOT NULL,

                                       last_name TEXT NOT NULL,

                                       phone TEXT NOT NULL,

                                       license_number TEXT NOT NULL UNIQUE,

                                       status TEXT NOT NULL
                                       CHECK(status IN ('Active','Inactive','On Leave'))

    );

-- ==========================================================
-- COLLECTION REQUESTS
-- ==========================================================

CREATE TABLE IF NOT EXISTS collection_requests (

                                                   request_id TEXT PRIMARY KEY,

                                                   source_location_id TEXT NOT NULL,

                                                   disposal_site_id TEXT NOT NULL,

                                                   waste_type_id TEXT NOT NULL,

                                                   estimated_weight REAL NOT NULL,

                                                   urgency TEXT NOT NULL
                                                   CHECK(urgency IN ('Low','Medium','High')),

    time_submitted DATETIME NOT NULL,

    deadline DATETIME NOT NULL,

    status TEXT NOT NULL
    CHECK(status IN
('Pending','Assigned','Completed')),

    FOREIGN KEY (source_location_id)
    REFERENCES locations(location_id),

    FOREIGN KEY (disposal_site_id)
    REFERENCES disposal_sites(site_id),

    FOREIGN KEY (waste_type_id)
    REFERENCES waste_types(waste_type_id)

    );

-- ==========================================================
-- ASSIGNMENTS
-- ==========================================================

CREATE TABLE IF NOT EXISTS assignments (

                                           assignment_id TEXT PRIMARY KEY,

                                           request_id TEXT NOT NULL,

                                           resource_id TEXT NOT NULL,

                                           driver_id TEXT NOT NULL,

                                           assigned_time DATETIME NOT NULL,

                                           completion_time DATETIME,

                                           assignment_status TEXT NOT NULL
                                           CHECK(assignment_status IN
('Assigned','In Progress','Completed')),

    FOREIGN KEY (request_id)
    REFERENCES collection_requests(request_id),

    FOREIGN KEY (resource_id)
    REFERENCES resources(resource_id),

    FOREIGN KEY (driver_id)
    REFERENCES drivers(driver_id)

    );

-- ==========================================================
-- ALGORITHM RUNS
-- ==========================================================

CREATE TABLE IF NOT EXISTS algorithm_runs (

                                              run_id TEXT PRIMARY KEY,

                                              algorithm_name TEXT NOT NULL,

                                              input_size INTEGER NOT NULL,

                                              time_ns INTEGER NOT NULL,

                                              memory_kb INTEGER NOT NULL,

                                              date_run DATETIME NOT NULL

);

-- ==========================================================
-- AUDIT EVENTS
-- ==========================================================

CREATE TABLE IF NOT EXISTS audit_events (

                                            audit_id TEXT PRIMARY KEY,

                                            event_type TEXT NOT NULL,

                                            description TEXT NOT NULL,

                                            event_time DATETIME NOT NULL

);

-- ==========================================================
-- INDEXES
-- ==========================================================

CREATE INDEX IF NOT EXISTS idx_location_name
    ON locations(name);

CREATE INDEX IF NOT EXISTS idx_location_area
    ON locations(area);

CREATE INDEX IF NOT EXISTS idx_road_from
    ON roads(from_location_id);

CREATE INDEX IF NOT EXISTS idx_road_to
    ON roads(to_location_id);

CREATE INDEX IF NOT EXISTS idx_request_status
    ON collection_requests(status);

CREATE INDEX IF NOT EXISTS idx_request_urgency
    ON collection_requests(urgency);

CREATE INDEX IF NOT EXISTS idx_request_location
    ON collection_requests(source_location_id);

CREATE INDEX IF NOT EXISTS idx_resource_status
    ON resources(availability_status);

CREATE INDEX IF NOT EXISTS idx_driver_status
    ON drivers(status);

CREATE INDEX IF NOT EXISTS idx_assignment_status
    ON assignments(assignment_status);

CREATE INDEX IF NOT EXISTS idx_algorithm_name
    ON algorithm_runs(algorithm_name);

CREATE INDEX IF NOT EXISTS idx_audit_event
    ON audit_events(event_type);

-- ==========================================================
-- END OF SCHEMA
-- ==========================================================