package com.rungta.campus.model;

public class Room {
    private String id;
    private String floorId;
    private String roomNumber;
    private String name;
    private String type; // CLASSROOM, LABORATORY, FACULTY_OFFICE, AUDITORIUM, LIBRARY_SECTION, RESTROOM
    private String department;
    private int capacity;
    private boolean isVerified;
    private String coordinatesSvg; // x,y,w,h for SVG floor map

    public Room() {}

    public Room(String id, String floorId, String roomNumber, String name, String type, String department, int capacity, boolean isVerified, String coordinatesSvg) {
        this.id = id;
        this.floorId = floorId;
        this.roomNumber = roomNumber;
        this.name = name;
        this.type = type;
        this.department = department;
        this.capacity = capacity;
        this.isVerified = isVerified;
        this.coordinatesSvg = coordinatesSvg;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFloorId() { return floorId; }
    public void setFloorId(String floorId) { this.floorId = floorId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getCoordinatesSvg() { return coordinatesSvg; }
    public void setCoordinatesSvg(String coordinatesSvg) { this.coordinatesSvg = coordinatesSvg; }
}
