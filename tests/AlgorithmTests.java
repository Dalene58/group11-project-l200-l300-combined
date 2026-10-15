import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import algorithms.DynamicProgrammingOptimizer;
import algorithms.GraphAlgorithms;
import algorithms.GreedyOptimizer;
import algorithms.SearchAlgorithms;
import algorithms.SortAlgorithms;
import data.Resource;
import data.ServiceRequest;
import structures.Graph;

public class AlgorithmTests {
    @Test
    void searchAlgorithmsWork() {
        Integer[] values = {1, 3, 5, 7, 9};
        assertEquals(2, SearchAlgorithms.linearSearch(values, 5));
        assertEquals(3, SearchAlgorithms.binarySearch(values, 7));
    }

    @Test
    void sortAlgorithmsWork() {
        Integer[] values = {5, 1, 4, 2};
        SortAlgorithms.insertionSort(values);
        assertArrayEquals(new Integer[] {1, 2, 4, 5}, values);
    }

    @Test
    void graphAlgorithmsWork() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);
        List<Integer> bfs = GraphAlgorithms.breadthFirstSearch(graph, 0);
        assertEquals(List.of(0, 1, 2), bfs);
        assertArrayEquals(new double[] {0.0, 2.0, 5.0}, GraphAlgorithms.dijkstra(graph, 0));
    }

    @Test
    void greedyAndDpUseRequests() {
        List<ServiceRequest> requests = List.of(
                new ServiceRequest(1, 1, "a", 10, 4, false),
                new ServiceRequest(2, 1, "b", 8, 6, false),
                new ServiceRequest(3, 1, "c", 7, 3, false));
        List<Resource> resources = List.of(new Resource(1, "Truck", 10, 1, true));
        List<ServiceRequest> greedy = GreedyOptimizer.dispatch(requests, resources);
        List<ServiceRequest> dp = DynamicProgrammingOptimizer.optimise(requests, 10);
        assertTrue(greedy.size() >= 1);
        assertEquals(2, dp.size());
    }

        @Test
        void optimizersUseEachAvailableTruckIndependently() {
        List<ServiceRequest> requests = List.of(
            new ServiceRequest(1, 1, "large", 10, 6, false),
            new ServiceRequest(2, 1, "small", 8, 4, false),
            new ServiceRequest(3, 1, "small", 7, 4, false));
        List<Resource> resources = List.of(
            new Resource(1, "Small truck", 6, 1, true),
            new Resource(2, "Unavailable truck", 100, 1, false),
            new Resource(3, "Second truck", 4, 1, true));

        assertEquals(List.of(1, 2), GreedyOptimizer.dispatch(requests, resources).stream()
            .map(ServiceRequest::getRequestId).toList());
        assertEquals(List.of(1, 2), DynamicProgrammingOptimizer.optimise(requests, resources).stream()
            .map(ServiceRequest::getRequestId).toList());
        }
}
