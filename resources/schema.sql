DROP VIEW IF EXISTS service_requests;
DROP TABLE IF EXISTS trace_steps;
DROP TABLE IF EXISTS audit_events;
DROP TABLE IF EXISTS algorithm_runs;
DROP TABLE IF EXISTS assignments;
DROP TABLE IF EXISTS collection_requests;
DROP TABLE IF EXISTS drivers;
DROP TABLE IF EXISTS resources;
DROP TABLE IF EXISTS disposal_sites;
DROP TABLE IF EXISTS waste_types;
DROP TABLE IF EXISTS roads;
DROP TABLE IF EXISTS locations;

CREATE TABLE locations (
    location_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    area VARCHAR(100) NOT NULL,
    location_type VARCHAR(50) NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL
);

CREATE TABLE roads (
    road_id VARCHAR(50) PRIMARY KEY,
    from_location_id VARCHAR(50) NOT NULL,
    to_location_id VARCHAR(50) NOT NULL,
    distance_km REAL NOT NULL,
    travel_time_min INT NOT NULL,
    condition_weight REAL NOT NULL,
    FOREIGN KEY (from_location_id) REFERENCES locations(location_id),
    FOREIGN KEY (to_location_id) REFERENCES locations(location_id)
);

CREATE TABLE waste_types (
    waste_type_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE disposal_sites (
    site_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location_id VARCHAR(50) NOT NULL,
    site_type VARCHAR(50) NOT NULL,
    capacity_tons INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (location_id) REFERENCES locations(location_id)
);

CREATE TABLE resources (
    resource_id VARCHAR(50) PRIMARY KEY,
    resource_type VARCHAR(50) NOT NULL,
    capacity_tons INT NOT NULL,
    availability_status VARCHAR(50) NOT NULL,
    home_location_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (home_location_id) REFERENCES locations(location_id)
);

CREATE TABLE drivers (
    driver_id VARCHAR(50) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    license_number VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE collection_requests (
    request_id VARCHAR(50) PRIMARY KEY,
    source_location_id VARCHAR(50) NOT NULL,
    disposal_site_id VARCHAR(50) NOT NULL,
    waste_type_id VARCHAR(50) NOT NULL,
    estimated_weight REAL NOT NULL,
    urgency VARCHAR(50) NOT NULL,
    time_submitted DATETIME NOT NULL,
    deadline DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (source_location_id) REFERENCES locations(location_id),
    FOREIGN KEY (disposal_site_id) REFERENCES disposal_sites(site_id),
    FOREIGN KEY (waste_type_id) REFERENCES waste_types(waste_type_id)
);

CREATE TABLE assignments (
    assignment_id VARCHAR(50) PRIMARY KEY,
    request_id VARCHAR(50) NOT NULL,
    resource_id VARCHAR(50) NOT NULL,
    driver_id VARCHAR(50) NOT NULL,
    assigned_time DATETIME NOT NULL,
    completion_time DATETIME,
    assignment_status VARCHAR(50) NOT NULL,
    FOREIGN KEY (request_id) REFERENCES collection_requests(request_id),
    FOREIGN KEY (resource_id) REFERENCES resources(resource_id),
    FOREIGN KEY (driver_id) REFERENCES drivers(driver_id)
);

CREATE TABLE algorithm_runs (
    run_id VARCHAR(50) PRIMARY KEY,
    algorithm_name VARCHAR(100) NOT NULL,
    input_size INT NOT NULL,
    time_ns INT NOT NULL,
    memory_kb INT NOT NULL,
    date_run DATETIME NOT NULL
);

CREATE TABLE audit_events (
    audit_id VARCHAR(50) PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    event_time DATETIME NOT NULL
);

CREATE VIEW service_requests AS
SELECT request_id, source_location_id AS location_id, waste_type_id AS request_type,
       CAST(urgency AS INTEGER) AS urgency, CAST(estimated_weight AS INTEGER) AS volume,
       CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END AS fulfilled
FROM collection_requests;
