package data;

public class Location {
    private final int locationId;
    private final String name;
    private final String region;
    private final boolean depot;

    public Location(int locationId, String name, String region, boolean depot) {
        this.locationId = locationId;
        this.name = name;
        this.region = region;
        this.depot = depot;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public boolean isDepot() {
        return depot;
    }

    @Override
    public String toString() {
        return name + " (" + region + ")";
    }
}
