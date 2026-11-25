package com.rungta.campus.model;

public class SearchResult {
    private String id;
    private String title;
    private String subtitle;
    private String category;
    private String type; // BUILDING, BLOCK, FLOOR, DEPARTMENT, ROOM, FACILITY
    private String buildingId;
    private String blockId;
    private String floorId;
    private double latitude;
    private double longitude;
    private boolean isVerified;
    private String sourceUrl;

    public SearchResult() {}

    public SearchResult(String id, String title, String subtitle, String category, String type,
                        String buildingId, String blockId, String floorId,
                        double latitude, double longitude, boolean isVerified, String sourceUrl) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.type = type;
        this.buildingId = buildingId;
        this.blockId = blockId;
        this.floorId = floorId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isVerified = isVerified;
        this.sourceUrl = sourceUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBuildingId() { return buildingId; }
    public void setBuildingId(String buildingId) { this.buildingId = buildingId; }

    public String getBlockId() { return blockId; }
    public void setBlockId(String blockId) { this.blockId = blockId; }

    public String getFloorId() { return floorId; }
    public void setFloorId(String floorId) { this.floorId = floorId; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
}
