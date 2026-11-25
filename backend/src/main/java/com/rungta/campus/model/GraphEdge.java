package com.rungta.campus.model;

public class GraphEdge {
    private String id;
    private String fromNodeId;
    private String toNodeId;
    private int distanceMeters;
    private int estimatedTimeSeconds;
    private boolean isAccessible;
    private String roadType; // PAVED_WALKWAY, ROAD, INDOOR_CORRIDOR
    private boolean isVerified;

    public GraphEdge() {}

    public GraphEdge(String id, String fromNodeId, String toNodeId, int distanceMeters, int estimatedTimeSeconds, boolean isAccessible, String roadType, boolean isVerified) {
        this.id = id;
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.distanceMeters = distanceMeters;
        this.estimatedTimeSeconds = estimatedTimeSeconds;
        this.isAccessible = isAccessible;
        this.roadType = roadType;
        this.isVerified = isVerified;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFromNodeId() { return fromNodeId; }
    public void setFromNodeId(String fromNodeId) { this.fromNodeId = fromNodeId; }

    public String getToNodeId() { return toNodeId; }
    public void setToNodeId(String toNodeId) { this.toNodeId = toNodeId; }

    public int getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(int distanceMeters) { this.distanceMeters = distanceMeters; }

    public int getEstimatedTimeSeconds() { return estimatedTimeSeconds; }
    public void setEstimatedTimeSeconds(int estimatedTimeSeconds) { this.estimatedTimeSeconds = estimatedTimeSeconds; }

    public boolean isAccessible() { return isAccessible; }
    public void setAccessible(boolean accessible) { isAccessible = accessible; }

    public String getRoadType() { return roadType; }
    public void setRoadType(String roadType) { this.roadType = roadType; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}
