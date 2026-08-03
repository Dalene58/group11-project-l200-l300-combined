package models;

public class Location {

    private String locationId;
    private String name;
    private String area;
    private String locationType;
    private double latitude;
    private double longitude;

    public Location() {}

    public Location(String locationId, String name, String area,
                    String locationType, double latitude, double longitude) {
        this.locationId = locationId;
        this.name = name;
        this.area = area;
        this.locationType = locationType;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getLocationType() {
        return locationType;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return locationId + " - " + name;
    }
}