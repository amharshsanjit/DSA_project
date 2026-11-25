package com.rungta.campus.algorithm;

import com.rungta.campus.model.GraphEdge;
import com.rungta.campus.model.GraphNode;
import com.rungta.campus.model.NavigationResult;

import java.util.*;

/**
 * Breadth-First Search (BFS) Algorithm for Rungta Campus walking network.
 * Optimizes minimum number of stops/turns (edges traversed) using Queue, Visited Set, and Parent Map.
 */
public class BfsAlgorithm {

    public static NavigationResult findFewestStopsPath(GraphModel graph, String sourceId, String destId) {
        String algo = "BFS (Fewest Stops)";

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

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.add(srcNode.getId());
        visited.add(srcNode.getId());
        parent.put(srcNode.getId(), null);

        boolean reached = false;

        while (!queue.isEmpty()) {
            String u = queue.poll();

            if (u.equals(dstNode.getId())) {
                reached = true;
                break;
            }

            for (GraphEdge edge : graph.getNeighbors(u)) {
                String v = edge.getToNodeId();
                if (!visited.contains(v)) {
                    visited.add(v);
                    parent.put(v, u);
                    queue.add(v);

                    if (v.equals(dstNode.getId())) {
                        reached = true;
                        break;
                    }
                }
            }
            if (reached) break;
        }

        if (!reached || !parent.containsKey(dstNode.getId())) {
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

        // Calculate total distance along the BFS path by summing edge weights
        int totalDistance = 0;
        for (int i = 0; i < pathIds.size() - 1; i++) {
            String u = pathIds.get(i);
            String v = pathIds.get(i + 1);
            for (GraphEdge e : graph.getNeighbors(u)) {
                if (e.getToNodeId().equals(v)) {
                    totalDistance += e.getDistanceMeters();
                    break;
                }
            }
        }

        double walkingMinutes = Math.round((totalDistance / 78.0) * 10.0) / 10.0;
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
        res.setTotalDistanceMeters(totalDistance);
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
                instructions.add("Depart from " + from.getName() + " taking the " + roadType + " to " + to.getName() + " (" + dist + " m)");
            } else if (i == nodes.size() - 2) {
                instructions.add("Continue straight through " + from.getName() + " to arrive at " + to.getName() + " (" + dist + " m)");
            } else {
                instructions.add("Turn toward " + to.getName() + " (" + dist + " m)");
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
