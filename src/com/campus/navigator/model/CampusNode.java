package com.campus.navigator.model;

/**
 * Represents a location/vertex on the campus map.
 * Contains geometric coordinates for visualization and category info.
 */
public class CampusNode {
    private final int id;
    private final String name;
    private int x;
    private int y;
    private String category;

    public CampusNode(int id, String name, int x, int y, String category) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.category = category;
    }

    public CampusNode(int id, String name, int x, int y) {
        this(id, name, x, y, "Campus Spot");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return name + " (#" + id + ")";
    }
}
