package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import data.Resource;
import data.ServiceRequest;

public class GreedyOptimizer {
    public static List<ServiceRequest> dispatch(List<ServiceRequest> requests, List<Resource> resources) {
        List<ServiceRequest> ordered = new ArrayList<>(requests);
        ordered.sort(Comparator.comparingInt(ServiceRequest::getUrgency).reversed());
        List<ServiceRequest> fulfilled = new ArrayList<>();
        int capacityLeft = resources.stream().filter(Resource::isAvailable).mapToInt(Resource::getCapacity).sum();
        for (ServiceRequest request : ordered) {
            if (request.getVolume() <= capacityLeft) {
                fulfilled.add(request.withFulfilled(true));
                capacityLeft -= request.getVolume();
            }
        }
        return fulfilled;
    }
}
