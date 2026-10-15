package ui;

import java.io.PrintStream;
import java.util.List;

import data.AlgorithmRun;
import data.ServiceRequest;

public class ConsoleMenu {
    private final PrintStream out;

    public ConsoleMenu(PrintStream out) {
        this.out = out;
    }

    public void printMenu() {
        out.println("Ghana Smart Waste-Management Operations Optimizer");
        out.println("1. Load dataset");
        out.println("2. View requests");
        out.println("3. Run Greedy Dispatch");
        out.println("4. Run DP Optimisation");
        out.println("5. Show Trace Tables");
        out.println("6. Display Performance Graphs");
        out.println("7. Run Batch Benchmark From CSV");
        out.println("8. Exit");
    }

    public void printPrompt() {
        out.print("Select an option: ");
    }

    public void printTraceStatus() {
        out.println("Trace records are available in the audit_events database table.");
    }

    public void printBatchBenchmarkSaved(int runCount) {
        out.println("Batch benchmark complete. Saved " + runCount + " runs to algorithm_runs.");
    }

    public void printPlotDataExported() {
        out.println("Plot-ready CSV files generated in logs/runtime_vs_input.csv and logs/memory_vs_input.csv.");
    }

    public void printRequests(List<ServiceRequest> requests) {
        for (ServiceRequest request : requests) {
            out.println(request);
        }
    }

    public void printFulfilment(String title, List<ServiceRequest> fulfilled) {
        out.println(title + ". Fulfilled requests: " + fulfilled.size());
    }

    public void printPerformanceGraph(List<AlgorithmRun> runs) {
        out.println("Performance Graph");
        for (AlgorithmRun run : runs) {
            out.println(run.getAlgorithmName() + " | " + "#".repeat(Math.max(1, (int) Math.min(40, run.getRuntimeNanos() / 1_000_000L + 1))));
        }
    }
}
