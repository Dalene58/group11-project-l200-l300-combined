package models;

public class WasteType {

    private String wasteTypeId;
    private String name;
    private String description;

    public WasteType() {}

    public WasteType(String wasteTypeId, String name, String description) {
        this.wasteTypeId = wasteTypeId;
        this.name = name;
        this.description = description;
    }

    public String getWasteTypeId() { return wasteTypeId; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public void setWasteTypeId(String wasteTypeId) {
        this.wasteTypeId = wasteTypeId;
    }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) {
        this.description = description;
    }
}