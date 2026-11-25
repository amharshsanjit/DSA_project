package com.rungta.campus.model;

public class Block {
    private String id;
    private String buildingId;
    private String name;
    private String identifier; // e.g. "Block A", "Block B"
    private String description;
    private boolean isVerified;

    public Block() {}

    public Block(String id, String buildingId, String name, String identifier, String description, boolean isVerified) {
        this.id = id;
        this.buildingId = buildingId;
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.isVerified = isVerified;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBuildingId() { return buildingId; }
    public void setBuildingId(String buildingId) { this.buildingId = buildingId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}
