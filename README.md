# Ghana Smart Waste-Management Optimizer

This project is a Java-based waste collection planning system designed for a Ghanaian municipal context. It combines custom data structures, algorithm design, database storage, and performance analysis to model a realistic waste operations problem.

## Project Overview

Waste collection is affected by several practical challenges, including urgent service requests, limited truck capacity, uneven road conditions, and time-sensitive scheduling. This project addresses those issues by creating a system that stores operational data, processes it using algorithmic methods, and compares the performance of different planning strategies.

The main aim is to show how a municipal waste system can be represented as a working software solution, rather than simply as an abstract algorithm exercise.

## What the Project Includes

- A realistic dataset representing locations, roads, waste types, disposal points, resources, drivers, and service requests
- Custom implementations of data structures such as linked lists, queues, stacks, graphs, and priority queues
- Search, sorting, and graph algorithms used to process and analyse the data
- A greedy dispatch approach for fast decision-making
- A dynamic programming optimisation approach for stronger overall planning decisions
- SQLite database integration for storing and loading project data
- Performance tracking for runtime and memory usage
- A simple console menu for running the application and reviewing results

## System Architecture

The project is organised into five main layers:

### 1. Data layer
This contains the domain objects used throughout the system, including locations, roads, service requests, resources, and performance records.

### 2. Structures layer
This contains the custom container classes used by the project, including:

- DynamicArray
- LinkedList
- Stack
- Queue
- PriorityQueue
- Graph
- DisjointSet

These structures are implemented manually instead of relying on Java collection classes for the core logic.

### 3. Algorithms layer
This layer contains the logic used for processing and optimisation, including:

- SearchAlgorithms
- SortAlgorithms
- GraphAlgorithms
- GreedyOptimizer
- DynamicProgrammingOptimizer
- PerformanceAnalyzer

### 4. Database layer
This section handles SQLite connectivity, schema creation, CSV data loading, and result persistence. It ensures that operational data is stored in a structured and usable form.

### 5. User interface layer
The console interface allows the user to load data, inspect records, run dispatch algorithms, and generate performance outputs.

## Algorithms Used

### Search and sorting
The project includes standard search and sorting methods to organise and retrieve information efficiently. These are useful when the request list or data record set needs to be processed in a consistent order.

### Graph operations
Graph-based methods are used to represent routes and connectivity between locations. This allows the project to explore how locations relate to one another and how movement through the network can be interpreted.

### Greedy dispatch
The greedy strategy focuses on high-priority requests first and selects the most suitable resource available. This approach is fast and practical, but it may not always produce the globally best result.

### Dynamic programming optimisation
The dynamic programming method looks for a stronger overall solution by evaluating combinations within capacity constraints. It is particularly useful when a broader optimal plan is preferable to a quick local choice.

## Database and Data Model

The application stores its core operational information in a SQLite database. The main tables include:

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

This structure reflects the real relationship between collection points, vehicles, drivers, and requests. It also allows the project to store benchmark results and performance output in a traceable way.

The seed data in resources/seed_data.csv is large enough to simulate a realistic municipal scenario and supports different benchmark sizes for testing.

## Performance Evaluation

The application measures both runtime and memory usage for different input sizes. This makes it possible to compare how each algorithm behaves as the dataset grows.

Results are stored in CSV files and can be reviewed as part of the project’s benchmarking workflow. This adds a practical evaluation component, showing not only whether the system works, but also how efficiently it works.

## Testing

The project includes automated tests covering major parts of the system:

- Data structure checks
- Algorithm correctness
- Data loading and database integration
- End-to-end dataset workflow validation

This makes the solution more reliable and shows that the project has been checked beyond a simple manual run.

## Requirements

To run the project, you need:

- Java 25 LTS
- Maven 3.9 or newer
- SQLite support through the bundled JDBC dependency

## How to Run

### Build
```bash
mvn compile
```

### Run tests
```bash
mvn test
```

### Launch the application
```bash
mvn exec:java
```

The console menu allows the user to load the dataset, view records, run greedy or dynamic programming optimisation, and generate performance outputs. The project also supports batch benchmarking for comparing results across different input sizes.

## Project Output

The application generates performance logs and comparison files in the logs directory, including:

- algorithm_runs.csv
- runtime_vs_input.csv
- memory_vs_input.csv
- completion_vs_input.csv

These files support analysis and make the project easier to evaluate in a reporting or academic context.

## Final Note

This project demonstrates how a practical waste-management system can be built using Java, custom data structures, algorithmic reasoning, and database-backed analysis. It combines technical implementation with a real-world operational problem, which makes it suitable for academic submission and review.
