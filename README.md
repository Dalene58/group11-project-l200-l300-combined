# Ghana Smart Waste-Management Operations Optimizer

A Java 25 LTS console project for modelling municipal waste-collection operations in Ghana using custom data structures, classical algorithms, database integration, and performance tracing.

## Project Purpose

Waste collection in Ghana often depends on changing request urgency, truck capacity, road access, and limited operational time. This project demonstrates how a municipal dispatch team could organise collection requests from communities such as Achimota, Kumasi, and Tamale, then choose routes and trucks using algorithmic reasoning rather than manual guesswork.

The goal is to:

- store local operational data in a database,
- load that data into manually implemented structures,
- apply search, sort, graph, greedy, and dynamic programming methods,
- record runtime and memory use,
- and present the result through a simple console menu.

## System Architecture

The project is split into five layers:

### 1. `data`
Domain objects for the municipal model.

- `Location` represents a Ghanaian community, depot, or collection point.
- `Road` represents a connection between two locations.
- `ServiceRequest` models a waste-collection request.
- `Resource` models a truck or other dispatch resource.
- `AlgorithmRun` stores runtime and memory measurements.

### 2. `structures`
Manual implementations of the core containers used by the algorithms.

- `DynamicArray`
- `LinkedList`
- `Stack`
- `Queue`
- `PriorityQueue`
- `Graph`
- `DisjointSet`

These classes avoid built-in Java collections for the core logic.

### 3. `algorithms`
The reasoning layer.

- `SearchAlgorithms`
- `SortAlgorithms`
- `GraphAlgorithms`
- `GreedyOptimizer`
- `DynamicProgrammingOptimizer`
- `PerformanceAnalyzer`

### 4. `database`
Persistence and loading utilities.

- `DatabaseConnector` opens the SQLite database.
- `SchemaInitializer` executes the schema file.
- `DataLoader` parses the CSV seed data and inserts it into the tables.

### 5. `ui`
Console interaction.

- `ConsoleMenu` displays the numbered menu.
- `DemoRunner` launches the menu from `Main`.

## Algorithms Implemented

### Search
- Linear search scans the array from left to right.
- Binary search works on sorted arrays and halves the search space each step.
- Complexity: linear search is $O(n)$, binary search is $O(\log n)$.

Correctness evidence:
- linear search checks every candidate until a match is found,
- binary search preserves the invariant that the target must stay inside the active interval.

### Sort
- Insertion sort is useful for small request lists and nearly sorted traces.
- Selection sort provides a simple baseline.
- Merge sort gives stable $O(n \log n)$ performance.

Correctness evidence:
- each algorithm preserves the multiset of items,
- and the sorted suffix/prefix invariant is maintained during each pass.

### Graph
- Breadth-first search explores reachable roads level by level.
- Depth-first search explores one route fully before backtracking.
- Dijkstra computes shortest road distance from a source location.

Complexity:
- BFS and DFS are $O(V + E)$ with adjacency matrix traversal in this scaffold,
- Dijkstra is $O(V^2)$ here because the graph uses a matrix.

### Greedy Dispatch
The greedy dispatcher prioritises the most urgent requests first, then assigns each request to the best-fit available truck.

Complexity:
- building the priority queue is $O(n \log n)$,
- each request can scan the resource list, so dispatch is $O(nr)$ in this version.

Correctness evidence:
- the queue always pops the highest-priority request next,
- and the best-fit truck choice is greedy with respect to remaining capacity.

### Dynamic Programming Optimisation
The DP optimiser uses a knapsack-style table to choose the best payload combination for a truck capacity limit.

Complexity:
- time is $O(nC)$,
- space is $O(nC)$,
where $n$ is the number of requests and $C$ is the truck capacity.

Correctness evidence:
- the table recurrence compares including versus excluding the current request,
- and reconstruction walks backward through the optimal table entries.

## Greedy vs DP Comparison

Greedy is fast and operationally intuitive, but it can miss the globally best combination.

Example counterexample:

- Capacity: 10
- Request A: urgency 10, volume 10
- Request B: urgency 8, volume 6
- Request C: urgency 7, volume 4

A greedy urgency-first rule selects A and uses the whole truck for a total score of 10.
The DP optimiser can choose B and C together for a total score of 15 while still fitting the same capacity.

That is why the project includes both approaches: greedy for dispatch speed, DP for route or payload optimisation where global optimality matters.

## Database Schema

The schema is defined in `resources/schema.sql` and uses these tables:

- `locations`
- `roads`
- `waste_types`
- `disposal_sites`
- `resources`
- `drivers`
- `collection_requests`
- `assignments`
- `algorithm_runs`
- `audit_events`

Relationships:

- `roads.from_location_id` and `roads.to_location_id` reference `locations.location_id`
- `disposal_sites.location_id` and `resources.home_location_id` reference `locations.location_id`
- `collection_requests` references its source location, disposal site, and waste type
- `assignments` references collection requests, resources, and drivers

This structure lets the application persist the Ghanaian operational model and later analyse it from the database.

`resources/seed_data.csv` uses one typed record per line. Record types are `location`, `road`,
`waste_type`, `disposal_site`, `resource`, `driver`, `request`, `assignment`, `algorithm_run`,
and `audit_event`; fields follow the corresponding table column order. Algorithm results generated
by the console are written to `logs/algorithm_runs.csv` with the exact `algorithm_runs`
columns: `run_id`, `algorithm_name`, `input_size`, `time_ns`, `memory_kb`, and `date_run`.

### Seed Data Dictionary

- `location,location_id,name,area,location_type,latitude,longitude`
- `road,road_id,from_location_id,to_location_id,distance_km,travel_time_min,condition_weight`
- `waste_type,waste_type_id,name,description`
- `disposal_site,site_id,name,location_id,site_type,capacity_tons,status`
- `resource,resource_id,resource_type,capacity_tons,availability_status,home_location_id`
- `driver,driver_id,first_name,last_name,phone,license_number,status`
- `request,request_id,source_location_id,disposal_site_id,waste_type_id,estimated_weight,urgency,time_submitted,deadline,status`
- `assignment,assignment_id,request_id,resource_id,driver_id,assigned_time,completion_time,assignment_status`
- `algorithm_run,run_id,algorithm_name,input_size,time_ns,memory_kb,date_run`
- `audit_event,audit_id,event_type,description,event_time`

## Testing & Performance

The project uses JUnit 5 tests in the `tests/` folder.

- `DataStructureTests` checks the manual containers.
- `AlgorithmTests` checks search, sort, greedy dispatch, DP optimisation, and graph traversal.
- `IntegrationTests` loads the schema into SQLite, imports the CSV seed file, and verifies the table counts.

Performance tracking:

- runtime is captured with `System.nanoTime()`,
- memory use is estimated with the JVM memory counters,
- results are written to `logs/algorithm_runs.csv`,
- completion comparison is exported to `logs/completion_vs_input.csv`,
- and the console menu prints a simple ASCII graph of recorded runs.

Audit and trace information is persisted through the `audit_events` database table.

## How to Run

### Requirements
- Java 25 LTS
- Maven 3.9+
- SQLite support through the bundled JDBC dependency

### Build
```bash
mvn compile
```

### Test
```bash
mvn test
```

### Run
```bash
mvn exec:java
```

The interactive menu accepts options 1 through 8. Load the dataset first, then choose which
request, dispatch, optimisation, trace, performance, or batch-benchmark action to run. Choosing 8 exits and writes
the performance records collected during that session to `logs/algorithm_runs.csv`.
Batch benchmark execution also exports `logs/runtime_vs_input.csv`, `logs/memory_vs_input.csv`,
and `logs/completion_vs_input.csv` for algorithm comparison plots.

### Database Notes
- The app uses a local SQLite file at `resources/waste-optimizer.db` by default.
- On first load, `SchemaInitializer` executes `resources/schema.sql`.
- Seed data is loaded from `resources/seed_data.csv`.

## Sample Console Output

```text
Ghana Smart Waste-Management Operations Optimizer
1. Load dataset
2. View requests
3. Run Greedy Dispatch
4. Run DP Optimisation
5. Show Trace Tables
6. Display Performance Graphs
7. Run Batch Benchmark From CSV
8. Exit
Select an option: 1
Dataset loaded successfully.
Locations: 20
Roads: 29
Requests: 600
Resources: 30

Select an option: 3
Greedy dispatch complete. Fulfilled requests: 2

Select an option: 4
DP optimisation complete. Fulfilled requests: 3
