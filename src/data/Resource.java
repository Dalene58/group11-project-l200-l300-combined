package data;

public class Resource {
    private final int resourceId;
    private final String name;
    private final int capacity;
    private final int currentLocationId;
    private final boolean available;

    public Resource(int resourceId, String name, int capacity, int currentLocationId, boolean available) {
        this.resourceId = resourceId;
        this.name = name;
        this.capacity = capacity;
        this.currentLocationId = currentLocationId;
        this.available = available;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentLocationId() {
        return currentLocationId;
    }

    public boolean isAvailable() {
        return available;
    }
}
