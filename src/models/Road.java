package models;

public class Road {

    private String roadId;
    private String fromLocationId;
    private String toLocationId;
    private double distanceKm;
    private int travelTimeMin;
    private double conditionWeight;

    public Road() {}

    public Road(String roadId, String fromLocationId,
                String toLocationId,
                double distanceKm,
                int travelTimeMin,
                double conditionWeight) {

        this.roadId = roadId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
        this.travelTimeMin = travelTimeMin;
        this.conditionWeight = conditionWeight;
    }

    public String getRoadId() { return roadId; }

    public String getFromLocationId() { return fromLocationId; }

    public String getToLocationId() { return toLocationId; }

    public double getDistanceKm() { return distanceKm; }

    public int getTravelTimeMin() { return travelTimeMin; }

    public double getConditionWeight() { return conditionWeight; }

    public void setRoadId(String roadId) { this.roadId = roadId; }

    public void setFromLocationId(String fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public void setToLocationId(String toLocationId) {
        this.toLocationId = toLocationId;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setTravelTimeMin(int travelTimeMin) {
        this.travelTimeMin = travelTimeMin;
    }

    public void setConditionWeight(double conditionWeight) {
        this.conditionWeight = conditionWeight;
    }
}