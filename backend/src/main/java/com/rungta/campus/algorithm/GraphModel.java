package com.rungta.campus.algorithm;

import com.rungta.campus.model.GraphEdge;
import com.rungta.campus.model.GraphNode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe Graph representation of the Rungta Campus walking network.
 * Uses HashMap for O(1) node lookup and Adjacency List for space-efficient edge storage.
 */
public class GraphModel {
    private final Map<String, GraphNode> nodes = new ConcurrentHashMap<>();
    private final Map<String, List<GraphEdge>> adjacencyList = new ConcurrentHashMap<>();

    public synchronized void addNode(GraphNode node) {
        nodes.put(node.getId(), node);
        adjacencyList.putIfAbsent(node.getId(), new ArrayList<>());
    }

    public synchronized void removeNode(String nodeId) {
        nodes.remove(nodeId);
        adjacencyList.remove(nodeId);
        for (List<GraphEdge> edges : adjacencyList.values()) {
            edges.removeIf(e -> e.getToNodeId().equals(nodeId) || e.getFromNodeId().equals(nodeId));
        }
    }

    public synchronized void addBidirectionalEdge(GraphEdge edge) {
        adjacencyList.putIfAbsent(edge.getFromNodeId(), new ArrayList<>());
        adjacencyList.putIfAbsent(edge.getToNodeId(), new ArrayList<>());

        // Remove existing forward edge if present, then add
        List<GraphEdge> fromList = adjacencyList.get(edge.getFromNodeId());
        fromList.removeIf(e -> e.getToNodeId().equals(edge.getToNodeId()));
        fromList.add(edge);

        // Add reverse edge
        GraphEdge reverseEdge = new GraphEdge(
                edge.getId() + "_rev",
                edge.getToNodeId(),
                edge.getFromNodeId(),
                edge.getDistanceMeters(),
                edge.getEstimatedTimeSeconds(),
                edge.isAccessible(),
                edge.getRoadType(),
                edge.isVerified()
        );
        List<GraphEdge> toList = adjacencyList.get(edge.getToNodeId());
        toList.removeIf(e -> e.getToNodeId().equals(edge.getFromNodeId()));
        toList.add(reverseEdge);
    }

    public synchronized void removeEdge(String edgeId) {
        for (List<GraphEdge> edges : adjacencyList.values()) {
            edges.removeIf(e -> e.getId().equals(edgeId) || e.getId().equals(edgeId + "_rev"));
        }
    }

    public GraphNode getNode(String id) {
        return nodes.get(id);
    }

    public GraphNode findNodeByNameOrBuildingId(String query) {
        if (query == null) return null;
        String q = query.trim().toLowerCase();
        for (GraphNode node : nodes.values()) {
            if (node.getId().equalsIgnoreCase(q) ||
                node.getName().toLowerCase().equals(q) ||
                (node.getBuildingId() != null && node.getBuildingId().equalsIgnoreCase(q))) {
                return node;
            }
        }
        for (GraphNode node : nodes.values()) {
            if (node.getName().toLowerCase().contains(q)) {
                return node;
            }
        }
        return null;
    }

    public List<GraphNode> getAllNodes() {
        return new ArrayList<>(nodes.values());
    }

    public List<GraphEdge> getNeighbors(String nodeId) {
        return adjacencyList.getOrDefault(nodeId, Collections.emptyList());
    }

    public List<GraphEdge> getAllUniqueEdges() {
        Set<String> seen = new HashSet<>();
        List<GraphEdge> unique = new ArrayList<>();
        for (List<GraphEdge> edges : adjacencyList.values()) {
            for (GraphEdge e : edges) {
                String key = e.getFromNodeId().compareTo(e.getToNodeId()) < 0 
                        ? e.getFromNodeId() + "->" + e.getToNodeId() 
                        : e.getToNodeId() + "->" + e.getFromNodeId();
                if (!seen.contains(key)) {
                    seen.add(key);
                    unique.add(e);
                }
            }
        }
        return unique;
    }

    public int getNodeCount() {
        return nodes.size();
    }
}
