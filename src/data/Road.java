package data;

public class Road {
    private final int roadId;
    private final int fromLocationId;
    private final int toLocationId;
    private final double distanceKm;

    public Road(int roadId, int fromLocationId, int toLocationId, double distanceKm) {
        this.roadId = roadId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distanceKm = distanceKm;
    }

    public int getRoadId() {
        return roadId;
    }

    public int getFromLocationId() {
        return fromLocationId;
    }

    public int getToLocationId() {
        return toLocationId;
    }

    public double getDistanceKm() {
        return distanceKm;
    }
}
