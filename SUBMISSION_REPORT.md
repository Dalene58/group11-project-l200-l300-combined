# Ghana Smart Waste-Management Optimizer - Concise Submission Report

## 1. Project Summary
The system models municipal waste collection operations in Ghana and evaluates two dispatch strategies:
- Greedy Dispatch
- DP Optimisation

It combines custom data structures, algorithm execution, SQLite persistence, batch benchmarking, and plot-ready reporting.

## 2. Technical Architecture
The codebase is organized into five layers:
- data: domain models (Location, Road, ServiceRequest, Resource, AlgorithmRun)
- structures: custom implementations (DynamicArray, LinkedList, Queue, Stack, PriorityQueue, Graph, DisjointSet)
- algorithms: search/sort/graph plus Greedy and DP optimization, performance measurement, batch benchmarking
- database: schema bootstrap, typed CSV loading, run persistence, plot-data export
- ui: interactive console menu and execution orchestration

## 3. Database and Data Model
SQLite schema includes:
- locations
- roads
- waste_types
- disposal_sites
- resources
- drivers
- collection_requests
- assignments
- algorithm_runs
- audit_events

Foreign keys enforce relationships between requests, assignments, locations, sites, resources, and drivers.

## 4. Seed Dataset Prepared
Seed file: resources/seed_data.csv

Generated dataset scale:
- 20 locations
- 29 roads
- 6 waste types
- 10 disposal sites
- 30 resources
- 40 drivers
- 600 requests
- 360 assignments
- 2 baseline algorithm runs
- 5 audit events

The dataset is large enough for benchmark checkpoints at input sizes 10, 50, 100, 200, and 500.

## 5. Performance Analysis Workflow
Implemented workflow supports:
1. Load first N requests from CSV per batch size.
2. Run Greedy Dispatch and DP Optimisation on the same batch.
3. Capture runtime via System.nanoTime().
4. Capture memory as totalMemory - freeMemory after execution.
5. Save each run to algorithm_runs with:
   run_id, algorithm_name, input_size, time_ns, memory_kb, date_run.

## 6. Benchmark and Reporting Outputs
Primary outputs:
- logs/algorithm_runs.csv
- logs/runtime_vs_input.csv
- logs/memory_vs_input.csv
- logs/completion_vs_input.csv

These exports are generated from database query results and are ready for Python, Excel, or BI plotting.

## 7. Execution Modes
Interactive:
- mvn exec:java
- Menu option 7 runs batch benchmark and exports plot-ready CSV files.

Automated one-shot:
- mvn exec:java "-Dexec.args=run-performance-analysis"

This one-shot mode performs dataset load, batch benchmarking, result persistence, and export in one run.

## 8. Verification Status
Validation performed:
- Unit and integration tests via mvn test
- Database load verification with foreign-key-enforced schema
- Batch benchmark persistence checks for all required input sizes

Current status: passing and submission-ready.

## 9. One-Page Grading Rubric Mapping
| Rubric Criterion | Requirement Coverage | Evidence |
|---|---|---|
| System design and modularity | Clear layered architecture with separation of concerns | data/structures/algorithms/database/ui package layout in [README.md](README.md) |
| Data modeling and schema quality | Normalized entities with foreign-key constraints | [resources/schema.sql](resources/schema.sql) |
| Dataset realism and scale | Multi-region Ghana context, varied statuses/urgencies, 600 requests | [resources/seed_data.csv](resources/seed_data.csv) |
| Algorithm implementation | Greedy and DP implemented and executable on identical batches | [src/algorithms/GreedyOptimizer.java](src/algorithms/GreedyOptimizer.java), [src/algorithms/DynamicProgrammingOptimizer.java](src/algorithms/DynamicProgrammingOptimizer.java) |
| Performance instrumentation | Runtime in ns and memory in KB captured per run | [src/algorithms/PerformanceAnalyzer.java](src/algorithms/PerformanceAnalyzer.java) |
| Batch benchmarking workflow | Batch checkpoints 10, 50, 100, 200, 500 processed automatically | [src/algorithms/BatchBenchmarkProcessor.java](src/algorithms/BatchBenchmarkProcessor.java) |
| Persistence of benchmark results | algorithm_runs table populated with run records | [src/database/AlgorithmRunRepository.java](src/database/AlgorithmRunRepository.java) |
| Reporting and visualization readiness | Runtime/memory exports prepared for plotting tools | [src/database/AlgorithmPlotDataExporter.java](src/database/AlgorithmPlotDataExporter.java), [logs/runtime_vs_input.csv](logs/runtime_vs_input.csv), [logs/memory_vs_input.csv](logs/memory_vs_input.csv) |
| Usability and execution | Interactive menu + one-shot automated run mode | [src/ui/DemoRunner.java](src/ui/DemoRunner.java), [src/Main.java](src/Main.java) |
| Testing and verification | Unit + integration coverage for load and benchmark persistence | [tests/AlgorithmTests.java](tests/AlgorithmTests.java), [tests/IntegrationTests.java](tests/IntegrationTests.java), [tests/SmokeTests.java](tests/SmokeTests.java) |
