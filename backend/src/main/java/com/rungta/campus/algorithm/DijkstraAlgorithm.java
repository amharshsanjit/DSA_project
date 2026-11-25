package com.rungta.campus.algorithm;

import com.rungta.campus.model.GraphEdge;
import com.rungta.campus.model.GraphNode;
import com.rungta.campus.model.NavigationResult;

import java.util.*;

/**
 * Dijkstra's Shortest Path Algorithm for Rungta Campus walking network.
 * Optimizes minimum distance (in metres) using PriorityQueue, Distance Map, and Parent Map.
 */
public class DijkstraAlgorithm {

    private static class NodeDist implements Comparable<NodeDist> {
        final String nodeId;
        final int dist;

        NodeDist(String nodeId, int dist) {
            this.nodeId = nodeId;
            this.dist = dist;
        }

        @Override
        public int compareTo(NodeDist o) {
            return Integer.compare(this.dist, o.dist);
        }
    }

    public static NavigationResult findShortestPath(GraphModel graph, String sourceId, String destId) {
        String algo = "Dijkstra's Algorithm (Shortest Distance)";

        GraphNode srcNode = graph.findNodeByNameOrBuildingId(sourceId);
        if (srcNode == null) {
            return NavigationResult.failure(algo, sourceId, destId, "Source location '" + sourceId + "' not found in campus map.");
        }

        GraphNode dstNode = graph.findNodeByNameOrBuildingId(destId);
        if (dstNode == null) {
            return NavigationResult.failure(algo, sourceId, destId, "Destination location '" + destId + "' not found in campus map.");
        }

        // Boundary case: Source == Destination
        if (srcNode.getId().equals(dstNode.getId())) {
            NavigationResult res = new NavigationResult();
            res.setSuccess(true);
            res.setAlgorithm(algo);
            res.setSourceNodeId(srcNode.getId());
            res.setSourceName(srcNode.getName());
            res.setDestinationNodeId(dstNode.getId());
            res.setDestinationName(dstNode.getName());
            res.setPathNodeIds(Collections.singletonList(srcNode.getId()));
            res.setPathNodes(Collections.singletonList(srcNode));
            res.setTotalDistanceMeters(0);
            res.setEstimatedWalkingTimeMinutes(0.0);
            res.setTotalStops(0);
            res.setIntermediateStops(0);
            res.setAccessible(true);
            res.setTurnByTurnInstructions(Collections.singletonList("You are already at " + srcNode.getName() + "."));
            return res;
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (GraphNode node : graph.getAllNodes()) {
            dist.put(node.getId(), Integer.MAX_VALUE);
        }
        dist.put(srcNode.getId(), 0);

        PriorityQueue<NodeDist> pq = new PriorityQueue<>();
        pq.add(new NodeDist(srcNode.getId(), 0));

        while (!pq.isEmpty()) {
            NodeDist curr = pq.poll();
            String u = curr.nodeId;

            if (visited.contains(u)) continue;
            visited.add(u);

            if (u.equals(dstNode.getId())) break;

            for (GraphEdge edge : graph.getNeighbors(u)) {
                String v = edge.getToNodeId();
                int weight = edge.getDistanceMeters();

                if (!visited.contains(v) && dist.get(u) != Integer.MAX_VALUE) {
                    int newDist = dist.get(u) + weight;
                    if (newDist < dist.get(v)) {
                        dist.put(v, newDist);
                        parent.put(v, u);
                        pq.add(new NodeDist(v, newDist));
                    }
                }
            }
        }

        if (!dist.containsKey(dstNode.getId()) || dist.get(dstNode.getId()) == Integer.MAX_VALUE || !parent.containsKey(dstNode.getId())) {
            return NavigationResult.failure(algo, srcNode.getName(), dstNode.getName(),
                    "No connected walking route found between " + srcNode.getName() + " and " + dstNode.getName() + ".");
        }

        // Reconstruct path
        List<String> pathIds = new ArrayList<>();
        String curr = dstNode.getId();
        while (curr != null) {
            pathIds.add(curr);
            curr = parent.get(curr);
        }
        Collections.reverse(pathIds);

        List<GraphNode> pathNodes = new ArrayList<>();
        for (String id : pathIds) {
            pathNodes.add(graph.getNode(id));
        }

        int totalMeters = dist.get(dstNode.getId());
        double walkingMinutes = Math.round((totalMeters / 78.0) * 10.0) / 10.0; // ~1.3 m/s walking speed
        int stops = pathIds.size() - 1;
        int intermediate = Math.max(0, pathIds.size() - 2);

        List<String> instructions = buildTurnInstructions(graph, pathNodes);
        boolean accessible = checkAccessibility(graph, pathIds);

        NavigationResult res = new NavigationResult();
        res.setSuccess(true);
        res.setAlgorithm(algo);
        res.setSourceNodeId(srcNode.getId());
        res.setSourceName(srcNode.getName());
        res.setDestinationNodeId(dstNode.getId());
        res.setDestinationName(dstNode.getName());
        res.setPathNodeIds(pathIds);
        res.setPathNodes(pathNodes);
        res.setTotalDistanceMeters(totalMeters);
        res.setEstimatedWalkingTimeMinutes(walkingMinutes);
        res.setTotalStops(stops);
        res.setIntermediateStops(intermediate);
        res.setTurnByTurnInstructions(instructions);
        res.setAccessible(accessible);
        return res;
    }

    private static List<String> buildTurnInstructions(GraphModel graph, List<GraphNode> nodes) {
        List<String> instructions = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            GraphNode from = nodes.get(i);
            GraphNode to = nodes.get(i + 1);

            int dist = 0;
            String roadType = "walkway";
            for (GraphEdge e : graph.getNeighbors(from.getId())) {
                if (e.getToNodeId().equals(to.getId())) {
                    dist = e.getDistanceMeters();
                    roadType = e.getRoadType().toLowerCase().replace('_', ' ');
                    break;
                }
            }

            if (i == 0) {
                instructions.add("Start from " + from.getName() + " and proceed along the " + roadType + " toward " + to.getName() + " (" + dist + " m)");
            } else if (i == nodes.size() - 2) {
                instructions.add("Continue past " + from.getName() + " to arrive at your destination: " + to.getName() + " (" + dist + " m)");
            } else {
                instructions.add("Walk from " + from.getName() + " toward " + to.getName() + " (" + dist + " m)");
            }
        }
        return instructions;
    }

    private static boolean checkAccessibility(GraphModel graph, List<String> pathIds) {
        for (int i = 0; i < pathIds.size() - 1; i++) {
            String u = pathIds.get(i);
            String v = pathIds.get(i + 1);
            for (GraphEdge e : graph.getNeighbors(u)) {
                if (e.getToNodeId().equals(v) && !e.isAccessible()) {
                    return false;
                }
            }
        }
        return true;
    }
}
