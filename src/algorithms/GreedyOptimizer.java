package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import data.Resource;
import data.ServiceRequest;

public class GreedyOptimizer {
    public static List<ServiceRequest> dispatch(List<ServiceRequest> requests, List<Resource> resources) {
        List<ServiceRequest> remaining = new ArrayList<>(requests);
        List<ServiceRequest> fulfilled = new ArrayList<>();

        for (Resource resource : resources) {
            if (!resource.isAvailable()) {
                continue;
            }
            List<ServiceRequest> ordered = new ArrayList<>(remaining);
            ordered.sort(Comparator.comparingInt(ServiceRequest::getUrgency).reversed());
            int capacityLeft = resource.getCapacity();
            for (ServiceRequest request : ordered) {
                if (request.getVolume() <= capacityLeft) {
                    fulfilled.add(request.withFulfilled(true));
                    remaining.remove(request);
                    capacityLeft -= request.getVolume();
                }
            }
        }
        return fulfilled;
    }
}
