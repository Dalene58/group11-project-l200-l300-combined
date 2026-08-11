package data;

public class ServiceRequest {
    private final int requestId;
    private final int locationId;
    private final String requestType;
    private final int urgency;
    private final int volume;
    private final boolean fulfilled;

    public ServiceRequest(int requestId, int locationId, String requestType, int urgency, int volume, boolean fulfilled) {
        this.requestId = requestId;
        this.locationId = locationId;
        this.requestType = requestType;
        this.urgency = urgency;
        this.volume = volume;
        this.fulfilled = fulfilled;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getRequestType() {
        return requestType;
    }

    public int getUrgency() {
        return urgency;
    }

    public int getVolume() {
        return volume;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public ServiceRequest withFulfilled(boolean newFulfilled) {
        return new ServiceRequest(requestId, locationId, requestType, urgency, volume, newFulfilled);
    }

    @Override
    public String toString() {
        return "Request " + requestId + " [urgency=" + urgency + ", volume=" + volume + "]";
    }
}
