package com.rungta.campus.model;

public class GraphNode {
    private String id;
    private String name;
    private String category; // ENTRANCE, BUILDING, JUNCTION, PARKING, FACILITY
    private double latitude;
    private double longitude;
    private String buildingId;

    public GraphNode() {}

    public GraphNode(String id, String name, String category, double latitude, double longitude, String buildingId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.buildingId = buildingId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getBuildingId() { return buildingId; }
    public void setBuildingId(String buildingId) { this.buildingId = buildingId; }
}
