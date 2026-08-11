package algorithms;

import java.util.ArrayList;
import java.util.List;

import data.ServiceRequest;

public class DynamicProgrammingOptimizer {
    public static List<ServiceRequest> optimise(List<ServiceRequest> requests, int capacity) {
        int n = requests.size();
        int[][] scores = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++) {
            ServiceRequest request = requests.get(i - 1);
            for (int currentCapacity = 0; currentCapacity <= capacity; currentCapacity++) {
                scores[i][currentCapacity] = scores[i - 1][currentCapacity];
                if (request.getVolume() <= currentCapacity) {
                    scores[i][currentCapacity] = Math.max(
                            scores[i][currentCapacity],
                            scores[i - 1][currentCapacity - request.getVolume()] + request.getUrgency());
                }
            }
        }

        List<ServiceRequest> selected = new ArrayList<>();
        int currentCapacity = capacity;
        for (int i = n; i > 0; i--) {
            if (scores[i][currentCapacity] != scores[i - 1][currentCapacity]) {
                ServiceRequest request = requests.get(i - 1);
                selected.add(request.withFulfilled(true));
                currentCapacity -= request.getVolume();
            }
        }
        return selected;
    }
}
