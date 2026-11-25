package com.rungta.campus.model;

import java.util.ArrayList;
import java.util.List;

public class NavigationResult {
    private boolean success;
    private String algorithm;
    private String sourceNodeId;
    private String sourceName;
    private String destinationNodeId;
    private String destinationName;
    private List<String> pathNodeIds = new ArrayList<>();
    private List<GraphNode> pathNodes = new ArrayList<>();
    private int totalDistanceMeters;
    private double estimatedWalkingTimeMinutes;
    private int totalStops;
    private int intermediateStops;
    private List<String> turnByTurnInstructions = new ArrayList<>();
    private boolean isAccessible;
    private String errorMessage;

    public NavigationResult() {}

    public static NavigationResult failure(String algorithm, String sourceName, String destName, String message) {
        NavigationResult res = new NavigationResult();
        res.setSuccess(false);
        res.setAlgorithm(algorithm);
        res.setSourceName(sourceName);
        res.setDestinationName(destName);
        res.setErrorMessage(message);
        return res;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getSourceNodeId() { return sourceNodeId; }
    public void setSourceNodeId(String sourceNodeId) { this.sourceNodeId = sourceNodeId; }

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }

    public String getDestinationNodeId() { return destinationNodeId; }
    public void setDestinationNodeId(String destinationNodeId) { this.destinationNodeId = destinationNodeId; }

    public String getDestinationName() { return destinationName; }
    public void setDestinationName(String destinationName) { this.destinationName = destinationName; }

    public List<String> getPathNodeIds() { return pathNodeIds; }
    public void setPathNodeIds(List<String> pathNodeIds) { this.pathNodeIds = pathNodeIds; }

    public List<GraphNode> getPathNodes() { return pathNodes; }
    public void setPathNodes(List<GraphNode> pathNodes) { this.pathNodes = pathNodes; }

    public int getTotalDistanceMeters() { return totalDistanceMeters; }
    public void setTotalDistanceMeters(int totalDistanceMeters) { this.totalDistanceMeters = totalDistanceMeters; }

    public double getEstimatedWalkingTimeMinutes() { return estimatedWalkingTimeMinutes; }
    public void setEstimatedWalkingTimeMinutes(double estimatedWalkingTimeMinutes) { this.estimatedWalkingTimeMinutes = estimatedWalkingTimeMinutes; }

    public int getTotalStops() { return totalStops; }
    public void setTotalStops(int totalStops) { this.totalStops = totalStops; }

    public int getIntermediateStops() { return intermediateStops; }
    public void setIntermediateStops(int intermediateStops) { this.intermediateStops = intermediateStops; }

    public List<String> getTurnByTurnInstructions() { return turnByTurnInstructions; }
    public void setTurnByTurnInstructions(List<String> turnByTurnInstructions) { this.turnByTurnInstructions = turnByTurnInstructions; }

    public boolean isAccessible() { return isAccessible; }
    public void setAccessible(boolean accessible) { isAccessible = accessible; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
