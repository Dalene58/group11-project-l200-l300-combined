# Ghana Smart Waste-Management Optimizer
## Final Submission Report

### Project Summary
This project was created to explore how a waste-collection system in Ghana could be modelled and improved using structured data, custom data structures, and algorithmic decision-making. The goal was to build a complete system that reflects the real challenges of municipal waste operations, including urgent collection requests, limited truck capacity, and the need to compare different planning strategies.

The final application brings these ideas together in one practical project. It loads realistic data, stores it in a database, applies search and optimisation methods, tracks performance, and produces results that can be reviewed and compared.

### Problem Being Addressed
Municipal waste collection is not just about moving waste from one place to another. It also depends on timing, demand, resource availability, and route planning. Some requests are more urgent than others, some areas are harder to reach, and collection resources are limited. Without a structured method, decisions can become inconsistent and inefficient.

This project addresses that issue by creating a system that can:
- store waste-operation data in a meaningful way,
- organise the information into reusable structures,
- compare different decision approaches,
- and measure how well each approach performs.

### What the System Does
The project follows a clear and practical workflow:

1. It loads a realistic dataset from CSV files.
2. It creates and manages a SQLite database for the project data.
3. It stores locations, roads, waste types, disposal sites, drivers, resources, requests, and assignments.
4. It uses manually implemented data structures to represent and process that information.
5. It applies searching, sorting, graph-based operations, greedy dispatch, and dynamic programming techniques.
6. It records runtime and memory usage for different input sizes.
7. It exports the results so they can be reviewed, compared, and analysed.

This makes the system more than a simple Java exercise. It behaves like a working decision-support tool for waste management planning.

### Main Project Components
The application is divided into a few main sections:

- Data layer: holds the core project objects such as locations, roads, service requests, resources, and algorithm results.
- Structures layer: contains custom implementations of linked lists, stacks, queues, priority queues, graphs, and related data containers.
- Algorithms layer: includes searching, sorting, graph traversal, greedy optimisation, and dynamic programming logic.
- Database layer: builds the schema, connects to SQLite, loads the dataset, and stores performance records.
- User interface layer: provides a simple console-based menu for running the project and viewing results.

This organisation keeps the project easy to understand and makes it easier to test and extend.

### Algorithms and Decision Logic
The project compares different ways of making decisions under operational constraints.

Search and sorting algorithms are included to support data processing and ordering. The graph-related methods help represent connections between places and compute accessibility or route-related ideas. The greedy strategy gives a fast and practical way to choose requests and resources based on urgency and fit. The dynamic programming approach is used to look for stronger overall combinations when a global optimum is important.

This comparison is valuable because a fast method is not always the most efficient method. In real operations, a decision strategy needs to balance speed, quality, and available resources.

### Database and Data Model
The project includes a structured database schema that stores the main entities of the system. These include:

- locations
- roads
- waste types
- disposal sites
- resources
- drivers
- collection requests
- assignments
- algorithm runs
- audit events

The data model is designed to reflect the relationships between collection points, vehicles, drivers, and service requests. This gives the project a realistic operational structure and supports the storage of results from performance testing.

The seed dataset is large enough to represent a realistic municipal workload and is used to test the application under different dataset sizes.

### Performance Testing and Evaluation
One of the strongest parts of this project is the way it measures performance. The application records runtime and memory use for different input sizes and stores the results in CSV files. These outputs can then be used to compare how the algorithms behave as the workload grows.

This allows the project to move beyond implementation and into evaluation. It shows not only that the system works, but also how efficiently it behaves under realistic conditions.

### Testing and Verification
The project includes automated tests for the main areas of the application. These cover the custom data structures, the algorithms, the database loading process, and the end-to-end dataset workflow. This helps confirm that the system is functioning as intended and that the key components work together correctly.

The validation process is important because it gives the project stronger academic and practical credibility. It shows that the work is not just theoretical, but tested and repeatable.

### Performance Graphs
The benchmark results were plotted directly from the generated log files. The three comparison graphs below show the most relevant performance differences between the greedy and dynamic programming approaches:

- [Runtime comparison](plots/runtime_comparison.png)
- [Memory comparison](plots/memory_comparison.png)
- [Completion comparison](plots/completion_comparison.png)

These charts are the key comparison visuals for the project and provide a clear summary of how the two algorithms behave across increasing input sizes.

### Final Outcome
This submission demonstrates a complete waste-management optimisation project that combines data modelling, algorithm design, database integration, and performance analysis. It reflects a realistic municipal scenario and applies practical software engineering principles to solve an operational problem.

The project successfully shows that a route and dispatch system can be represented using Java, custom structures, and algorithmic reasoning. It also shows that performance can be measured and compared in a meaningful way, which adds strong value to the overall outcome.

### Conclusion
In summary, this project is a well-structured and practical solution for waste collection planning in a Ghanaian context. It brings together relevant technical skills, real-world problem framing, and a clear implementation strategy. The final result is a complete project that is suitable for academic submission and demonstrates both technical understanding and applied problem-solving.
