package com.rungta.campus.model;

import java.util.ArrayList;
import java.util.List;

public class Building {
    private String id;
    private String name;
    private String code;
    private String institutionId;
    private String category;
    private double latitude;
    private double longitude;
    private int floorsCount;
    private boolean isVerified;
    private String sourceUrl;
    private String verificationDate;
    private String description;
    private List<String> departments = new ArrayList<>();
    private List<String> facilities = new ArrayList<>();
    private List<String> nearbyBuildingIds = new ArrayList<>();

    public Building() {}

    public Building(String id, String name, String code, String institutionId, String category,
                    double latitude, double longitude, int floorsCount, boolean isVerified,
                    String sourceUrl, String verificationDate, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.institutionId = institutionId;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.floorsCount = floorsCount;
        this.isVerified = isVerified;
        this.sourceUrl = sourceUrl;
        this.verificationDate = verificationDate;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getInstitutionId() { return institutionId; }
    public void setInstitutionId(String institutionId) { this.institutionId = institutionId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getFloorsCount() { return floorsCount; }
    public void setFloorsCount(int floorsCount) { this.floorsCount = floorsCount; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }

    public String getVerificationDate() { return verificationDate; }
    public void setVerificationDate(String verificationDate) { this.verificationDate = verificationDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getDepartments() { return departments; }
    public void setDepartments(List<String> departments) { this.departments = departments; }

    public List<String> getFacilities() { return facilities; }
    public void setFacilities(List<String> facilities) { this.facilities = facilities; }

    public List<String> getNearbyBuildingIds() { return nearbyBuildingIds; }
    public void setNearbyBuildingIds(List<String> nearbyBuildingIds) { this.nearbyBuildingIds = nearbyBuildingIds; }
}
