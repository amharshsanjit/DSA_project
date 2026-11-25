package com.campus.navigator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates the complete result of a route query,
 * including algorithm name, path sequence, total distance, stops, and error reporting.
 */
public class NavigationResult {
    private final boolean success;
    private final String algorithmName;
    private final String source;
    private final String destination;
    private final List<String> pathLocations;
    private final List<Integer> pathVertexIds;
    private final int totalDistance;
    private final int totalStops;
    private final List<String> stepDetails;
    private final String errorMessage;

    // Successful result constructor
    public NavigationResult(String algorithmName, String source, String destination,
                            List<String> pathLocations, List<Integer> pathVertexIds,
                            int totalDistance, int totalStops, List<String> stepDetails) {
        this.success = true;
        this.algorithmName = algorithmName;
        this.source = source;
        this.destination = destination;
        this.pathLocations = pathLocations != null ? pathLocations : Collections.emptyList();
        this.pathVertexIds = pathVertexIds != null ? pathVertexIds : Collections.emptyList();
        this.totalDistance = totalDistance;
        this.totalStops = totalStops;
        this.stepDetails = stepDetails != null ? stepDetails : Collections.emptyList();
        this.errorMessage = null;
    }

    // Failure / Error result constructor
    public NavigationResult(String algorithmName, String source, String destination, String errorMessage) {
        this.success = false;
        this.algorithmName = algorithmName;
        this.source = source;
        this.destination = destination;
        this.pathLocations = Collections.emptyList();
        this.pathVertexIds = Collections.emptyList();
        this.totalDistance = -1;
        this.totalStops = -1;
        this.stepDetails = Collections.emptyList();
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public List<String> getPathLocations() {
        return pathLocations;
    }

    public List<Integer> getPathVertexIds() {
        return pathVertexIds;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    /**
     * Total number of stops/hops along the path (edges traversed).
     * For example: Gate -> Admin -> Canteen has 2 stops (edges).
     */
    public int getTotalStops() {
        return totalStops;
    }

    /**
     * Number of intermediate stops (excluding starting point and destination).
     */
    public int getIntermediateStops() {
        if (!success || pathLocations.size() <= 2) {
            return 0;
        }
        return pathLocations.size() - 2;
    }

    public List<String> getStepDetails() {
        return stepDetails;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Formats the path as: "Gate → Admin → Library → Canteen"
     */
    public String getFormattedPath() {
        if (!success) {
            return "No path available";
        }
        return String.join(" → ", pathLocations);
    }

    @Override
    public String toString() {
        if (!success) {
            return "Error: " + errorMessage;
        }
        return String.format("Algorithm: %s%nPath: %s%nDistance: %d m%nStops: %d",
                algorithmName, getFormattedPath(), totalDistance, totalStops);
    }
}
