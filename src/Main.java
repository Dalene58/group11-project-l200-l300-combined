import ui.DemoRunner;

public class Main {
    public static void main(String[] args) {
        DemoRunner demoRunner = new DemoRunner();
        if (args.length > 0 && "load-dataset".equalsIgnoreCase(args[0])) {
            demoRunner.loadDataset();
            System.out.println("Dataset loaded successfully.");
            return;
        }
        if (args.length > 0 && "run-performance-analysis".equalsIgnoreCase(args[0])) {
            demoRunner.runPerformanceAnalysis();
            return;
        }
        demoRunner.run();
    }
}
