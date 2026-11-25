package com.rungta.campus.model;

public class Facility {
    private String id;
    private String name;
    private String category; // CANTEEN, PARKING, ATM, SPORTS, MEDICAL, HOSTEL, SECURITY, ADMISSION
    private String buildingId;
    private double latitude;
    private double longitude;
    private String description;
    private String openingHours;
    private boolean isVerified;

    public Facility() {}

    public Facility(String id, String name, String category, String buildingId, double latitude, double longitude, String description, String openingHours, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.buildingId = buildingId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.openingHours = openingHours;
        this.isVerified = isVerified;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBuildingId() { return buildingId; }
    public void setBuildingId(String buildingId) { this.buildingId = buildingId; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}
