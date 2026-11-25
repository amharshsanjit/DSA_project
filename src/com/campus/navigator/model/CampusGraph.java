package com.campus.navigator.model;

import java.util.*;

/**
 * CampusGraph manages campus vertices (locations) and edges (roads/walkways).
 * Core concepts utilized:
 * - HashMap for fast lookup from location name to vertex ID.
 * - Adjacency List (List of Lists of Edges) for storing graph connections.
 * - Bidirectional edge management.
 */
public class CampusGraph {
    // HashMap mapping canonical location names to their unique vertex ID
    private final Map<String, Integer> locationToId;
    
    // Case-insensitive lookup map to original name
    private final Map<String, String> lookupToCanonical;

    // Reverse mapping from vertex ID to canonical location name
    private final Map<Integer, String> idToLocation;

    // Node metadata including coordinates for map visualization
    private final Map<Integer, CampusNode> nodes;

    // Adjacency list storing outgoing weighted edges for each vertex ID
    private final List<List<Edge>> adjacencyList;

    // Listeners for graph topology changes (for UI updates)
    private final List<Runnable> changeListeners;

    public CampusGraph() {
        this.locationToId = new HashMap<>();
        this.lookupToCanonical = new HashMap<>();
        this.idToLocation = new HashMap<>();
        this.nodes = new HashMap<>();
        this.adjacencyList = new ArrayList<>();
        this.changeListeners = new ArrayList<>();
        
        initializeDefaultCampus();
    }

    /**
     * Initializes the predefined 8 campus locations and 5 bidirectional connections
     * as specified in the project requirements.
     */
    public final void initializeDefaultCampus() {
        // Clear existing data
        locationToId.clear();
        lookupToCanonical.clear();
        idToLocation.clear();
        nodes.clear();
        adjacencyList.clear();

        // 1. Add the 8 predefined campus locations with visual coordinates for map layout
        // Coordinates are configured to give a natural, spacious campus layout:
        // Gate (bottom-left), Admin (bottom-center), Library (top-center), Canteen (top-right/east)
        // Intermediate walkways/plazas positioned along their respective corridors
        addLocationInternal("Gate", 120, 380, "Main Entrance");
        addLocationInternal("Admin", 340, 420, "Administrative Block");
        addLocationInternal("Library", 380, 140, "Central Library");
        addLocationInternal("Canteen", 680, 260, "Campus Canteen");
        
        // Named walkway / junction spots on campus
        addLocationInternal("Admin-Library", 360, 280, "Walkway / Courtyard");
        addLocationInternal("Gate-Admin", 230, 400, "Campus Avenue");
        addLocationInternal("Library-Canteen", 530, 200, "Garden Pathway");
        addLocationInternal("Admin-Canteen", 510, 360, "South Promenade");

        // 2. Add the bidirectional connections with specified distances in metres
        // Gate <-> Library = 100m
        addEdgeInternal("Gate", "Library", 100);

        // Gate <-> Admin = 50m
        addEdgeInternal("Gate", "Admin", 50);

        // Admin <-> Library = 40m
        addEdgeInternal("Admin", "Library", 40);

        // Library <-> Canteen = 60m
        addEdgeInternal("Library", "Canteen", 60);

        // Admin <-> Canteen = 200m
        addEdgeInternal("Admin", "Canteen", 200);

        notifyChangeListeners();
    }

    /**
     * Internal helper to register a location without triggering change listeners prematurely.
     */
    private int addLocationInternal(String name, int x, int y, String category) {
        String trimmed = name.trim();
        String lookupKey = trimmed.toLowerCase();

        if (lookupToCanonical.containsKey(lookupKey)) {
            return locationToId.get(lookupToCanonical.get(lookupKey));
        }

        int newId = adjacencyList.size();
        locationToId.put(trimmed, newId);
        lookupToCanonical.put(lookupKey, trimmed);
        idToLocation.put(newId, trimmed);

        CampusNode node = new CampusNode(newId, trimmed, x, y, category);
        nodes.put(newId, node);
        adjacencyList.add(new ArrayList<>());

        return newId;
    }

    /**
     * Dynamically adds a new campus location.
     * Validates that the name is not empty and does not already exist.
     *
     * @param name Name of the new location
     * @param x X coordinate for map rendering
     * @param y Y coordinate for map rendering
     * @param category Description or category
     * @return Result message indicating success or failure
     */
    public synchronized String addLocation(String name, int x, int y, String category) {
        if (name == null || name.trim().isEmpty()) {
            return "Location name cannot be empty.";
        }

        String trimmed = name.trim();
        String lookupKey = trimmed.toLowerCase();

        if (lookupToCanonical.containsKey(lookupKey)) {
            return "Location '" + trimmed + "' already exists.";
        }

        addLocationInternal(trimmed, x, y, (category == null || category.trim().isEmpty()) ? "New Building" : category.trim());
        notifyChangeListeners();
        return "SUCCESS";
    }

    /**
     * Overloaded method to add a location with automatic coordinate positioning.
     */
    public synchronized String addLocation(String name) {
        // Place new node in an available visual spot
        int count = getVertexCount();
        int x = 200 + (count * 67) % 500;
        int y = 150 + (count * 53) % 320;
        return addLocation(name, x, y, "Campus Facility");
    }

    /**
     * Internal helper to add bidirectional edge.
     */
    private void addEdgeInternal(String fromLoc, String toLoc, int distance) {
        Integer u = getVertexId(fromLoc);
        Integer v = getVertexId(toLoc);

        if (u == null || v == null) {
            throw new IllegalArgumentException("Unknown location name: " + fromLoc + " or " + toLoc);
        }

        // Add u -> v
        addEdgeDirected(u, v, distance);
        // Add v -> u (bidirectional requirement)
        addEdgeDirected(v, u, distance);
    }

    private void addEdgeDirected(int from, int to, int distance) {
        List<Edge> edges = adjacencyList.get(from);
        // Remove existing edge to the same target if present, then add updated
        edges.removeIf(edge -> edge.getTo() == to);
        edges.add(new Edge(to, distance));
    }

    /**
     * Dynamically adds a bidirectional road/path between two existing locations.
     *
     * @param loc1 First location name
     * @param loc2 Second location name
     * @param distance Distance in metres (must be positive)
     * @return Result message indicating success or failure
     */
    public synchronized String addPath(String loc1, String loc2, int distance) {
        if (loc1 == null || loc1.trim().isEmpty() || loc2 == null || loc2.trim().isEmpty()) {
            return "Locations cannot be empty.";
        }

        String canonical1 = resolveCanonicalName(loc1);
        if (canonical1 == null) {
            return "Location '" + loc1 + "' not found.";
        }

        String canonical2 = resolveCanonicalName(loc2);
        if (canonical2 == null) {
            return "Location '" + loc2 + "' not found.";
        }

        if (canonical1.equalsIgnoreCase(canonical2)) {
            return "Source and destination locations cannot be identical.";
        }

        if (distance <= 0) {
            return "Distance must be a positive number greater than 0.";
        }

        int u = locationToId.get(canonical1);
        int v = locationToId.get(canonical2);

        addEdgeDirected(u, v, distance);
        addEdgeDirected(v, u, distance);

        notifyChangeListeners();
        return "SUCCESS";
    }

    /**
     * Resolves a case-insensitive name query to its canonical name.
     */
    public String resolveCanonicalName(String name) {
        if (name == null) return null;
        return lookupToCanonical.get(name.trim().toLowerCase());
    }

    public boolean hasLocation(String name) {
        if (name == null) return false;
        return lookupToCanonical.containsKey(name.trim().toLowerCase());
    }

    public Integer getVertexId(String name) {
        String canonical = resolveCanonicalName(name);
        return canonical != null ? locationToId.get(canonical) : null;
    }

    public String getLocationName(int vertexId) {
        return idToLocation.get(vertexId);
    }

    public CampusNode getNode(int vertexId) {
        return nodes.get(vertexId);
    }

    public CampusNode getNode(String name) {
        Integer id = getVertexId(name);
        return id != null ? nodes.get(id) : null;
    }

    public List<CampusNode> getAllNodes() {
        return new ArrayList<>(nodes.values());
    }

    public List<String> getAllLocationNames() {
        List<String> list = new ArrayList<>(locationToId.keySet());
        Collections.sort(list);
        return list;
    }

    public int getVertexCount() {
        return locationToId.size();
    }

    public List<Edge> getNeighbors(int vertexId) {
        if (vertexId < 0 || vertexId >= adjacencyList.size()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(adjacencyList.get(vertexId));
    }

    public Integer getEdgeDistance(int u, int v) {
        if (u < 0 || u >= adjacencyList.size()) return null;
        for (Edge edge : adjacencyList.get(u)) {
            if (edge.getTo() == v) {
                return edge.getDistance();
            }
        }
        return null;
    }

    public void addChangeListener(Runnable listener) {
        if (listener != null) {
            changeListeners.add(listener);
        }
    }

    private void notifyChangeListeners() {
        for (Runnable listener : changeListeners) {
            try {
                listener.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
