package com.campus.navigator.model;

/**
 * Represents a directed weighted connection from a vertex to another.
 * Used within the adjacency list to represent campus roads/paths.
 */
public class Edge {
    private final int to;
    private final int distance;

    public Edge(int to, int distance) {
        this.to = to;
        this.distance = distance;
    }

    public int getTo() {
        return to;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Edge{to=" + to + ", distance=" + distance + "m}";
    }
}
