package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import structures.Graph;
import structures.Queue;

public class GraphAlgorithms {
    public static List<Integer> breadthFirstSearch(Graph graph, int start) {
        boolean[] visited = new boolean[graph.size()];
        Queue<Integer> queue = new Queue<>();
        List<Integer> order = new ArrayList<>();
        queue.enqueue(start);
        visited[start] = true;
        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            order.add(current);
            for (int next = 0; next < graph.size(); next++) {
                if (graph.weight(current, next) > 0 && !visited[next]) {
                    visited[next] = true;
                    queue.enqueue(next);
                }
            }
        }
        return order;
    }

    public static List<Integer> depthFirstSearch(Graph graph, int start) {
        boolean[] visited = new boolean[graph.size()];
        List<Integer> order = new ArrayList<>();
        dfs(graph, start, visited, order);
        return order;
    }

    private static void dfs(Graph graph, int current, boolean[] visited, List<Integer> order) {
        visited[current] = true;
        order.add(current);
        for (int next = 0; next < graph.size(); next++) {
            if (graph.weight(current, next) > 0 && !visited[next]) {
                dfs(graph, next, visited, order);
            }
        }
    }

    public static double[] dijkstra(Graph graph, int source) {
        double[] distance = new double[graph.size()];
        boolean[] visited = new boolean[graph.size()];
        Arrays.fill(distance, Double.POSITIVE_INFINITY);
        distance[source] = 0.0;
        for (int i = 0; i < graph.size(); i++) {
            int current = -1;
            double best = Double.POSITIVE_INFINITY;
            for (int j = 0; j < graph.size(); j++) {
                if (!visited[j] && distance[j] < best) {
                    best = distance[j];
                    current = j;
                }
            }
            if (current == -1) {
                break;
            }
            visited[current] = true;
            for (int next = 0; next < graph.size(); next++) {
                double weight = graph.weight(current, next);
                if (weight > 0 && distance[current] + weight < distance[next]) {
                    distance[next] = distance[current] + weight;
                }
            }
        }
        return distance;
    }
}
