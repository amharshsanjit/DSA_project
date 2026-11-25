package com.rungta.campus.model;

public class Floor {
    private String id;
    private String blockId;
    private int floorNumber; // 0 = Ground Floor, 1 = 1st Floor, etc.
    private String floorName;
    private String floorPlanSvg; // SVG representation or placeholder
    private boolean isVerified;

    public Floor() {}

    public Floor(String id, String blockId, int floorNumber, String floorName, String floorPlanSvg, boolean isVerified) {
        this.id = id;
        this.blockId = blockId;
        this.floorNumber = floorNumber;
        this.floorName = floorName;
        this.floorPlanSvg = floorPlanSvg;
        this.isVerified = isVerified;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBlockId() { return blockId; }
    public void setBlockId(String blockId) { this.blockId = blockId; }

    public int getFloorNumber() { return floorNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }

    public String getFloorName() { return floorName; }
    public void setFloorName(String floorName) { this.floorName = floorName; }

    public String getFloorPlanSvg() { return floorPlanSvg; }
    public void setFloorPlanSvg(String floorPlanSvg) { this.floorPlanSvg = floorPlanSvg; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}
