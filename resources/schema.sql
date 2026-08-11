CREATE TABLE IF NOT EXISTS locations (
    location_id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    region TEXT NOT NULL,
    depot INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS roads (
    road_id INTEGER PRIMARY KEY,
    from_location_id INTEGER NOT NULL,
    to_location_id INTEGER NOT NULL,
    distance_km REAL NOT NULL,
    FOREIGN KEY (from_location_id) REFERENCES locations(location_id),
    FOREIGN KEY (to_location_id) REFERENCES locations(location_id)
);

CREATE TABLE IF NOT EXISTS service_requests (
    request_id INTEGER PRIMARY KEY,
    location_id INTEGER NOT NULL,
    request_type TEXT NOT NULL,
    urgency INTEGER NOT NULL,
    volume INTEGER NOT NULL,
    fulfilled INTEGER NOT NULL,
    FOREIGN KEY (location_id) REFERENCES locations(location_id)
);

CREATE TABLE IF NOT EXISTS resources (
    resource_id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    capacity INTEGER NOT NULL,
    current_location_id INTEGER NOT NULL,
    available INTEGER NOT NULL,
    FOREIGN KEY (current_location_id) REFERENCES locations(location_id)
);

CREATE TABLE IF NOT EXISTS algorithm_runs (
    run_id INTEGER PRIMARY KEY AUTOINCREMENT,
    algorithm_name TEXT NOT NULL,
    runtime_nanos INTEGER NOT NULL,
    memory_bytes INTEGER NOT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trace_steps (
    step_id INTEGER PRIMARY KEY AUTOINCREMENT,
    run_id INTEGER NOT NULL,
    step_name TEXT NOT NULL,
    detail TEXT NOT NULL,
    FOREIGN KEY (run_id) REFERENCES algorithm_runs(run_id)
);
