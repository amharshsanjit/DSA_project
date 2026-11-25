package com.campus.navigator.algorithm;

import com.campus.navigator.model.CampusGraph;
import com.campus.navigator.model.Edge;
import com.campus.navigator.model.NavigationResult;

import java.util.*;

/**
 * Implementation of core graph pathfinding algorithms:
 * 1. Dijkstra's Algorithm: Computes the route minimizing total distance (in metres).
 * 2. BFS (Breadth-First Search): Computes the route minimizing number of stops (edges).
 * Both algorithms strictly reconstruct paths using a parent array and handle edge/boundary cases.
 */
public class GraphAlgorithms {

    /**
     * Helper record/class for Dijkstra's Priority Queue.
     */
    private static class NodeDistance implements Comparable<NodeDistance> {
        final int vertexId;
        final int distance;

        NodeDistance(int vertexId, int distance) {
            this.vertexId = vertexId;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    /**
     * Finds the shortest path based on total distance using Dijkstra's Algorithm.
     *
     * @param graph Campus graph containing locations and roads
     * @param startLoc Starting location name
     * @param destLoc Destination location name
     * @return NavigationResult containing the shortest path details or error
     */
    public static NavigationResult findShortestDistance(CampusGraph graph, String startLoc, String destLoc) {
        String algoName = "Dijkstra's Algorithm (Shortest Distance)";

        // 1. Validate starting location
        if (startLoc == null || startLoc.trim().isEmpty()) {
            return new NavigationResult(algoName, startLoc, destLoc, "Starting location cannot be empty.");
        }
        String canonicalStart = graph.resolveCanonicalName(startLoc);
        if (canonicalStart == null) {
            return new NavigationResult(algoName, startLoc, destLoc, "Location " + startLoc.trim() + " not found.");
        }

        // 2. Validate destination location
        if (destLoc == null || destLoc.trim().isEmpty()) {
            return new NavigationResult(algoName, startLoc, destLoc, "Destination location cannot be empty.");
        }
        String canonicalDest = graph.resolveCanonicalName(destLoc);
        if (canonicalDest == null) {
            return new NavigationResult(algoName, startLoc, destLoc, "Location " + destLoc.trim() + " not found.");
        }

        int startId = graph.getVertexId(canonicalStart);
        int destId = graph.getVertexId(canonicalDest);

        // 3. Boundary case: Identical source and destination
        if (startId == destId) {
            List<String> pathNames = Collections.singletonList(canonicalStart);
            List<Integer> pathIds = Collections.singletonList(startId);
            List<String> steps = Collections.singletonList("Already at destination: " + canonicalStart + " (0 m)");
            return new NavigationResult(algoName, canonicalStart, canonicalDest, pathNames, pathIds, 0, 0, steps);
        }

        int n = graph.getVertexCount();
        int[] dist = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];

        // Initialize distance array to infinity and parent array to -1
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        dist[startId] = 0;

        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        pq.add(new NodeDistance(startId, 0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            int u = current.vertexId;

            if (visited[u]) {
                continue;
            }
            visited[u] = true;

            // Early exit if destination reached
            if (u == destId) {
                break;
            }

            // Iterate over all outgoing connections of u
            for (Edge edge : graph.getNeighbors(u)) {
                int v = edge.getTo();
                int weight = edge.getDistance();

                if (!visited[v] && dist[u] != Integer.MAX_VALUE) {
                    int newDist = dist[u] + weight;
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        parent[v] = u;
                        pq.add(new NodeDistance(v, newDist));
                    }
                }
            }
        }

        // 4. Check if destination is reachable
        if (dist[destId] == Integer.MAX_VALUE || parent[destId] == -1) {
            return new NavigationResult(algoName, canonicalStart, canonicalDest,
                    "No route found between " + canonicalStart + " and " + canonicalDest + ". The locations are disconnected.");
        }

        // 5. Reconstruct complete path using parent array
        List<Integer> pathIds = reconstructPath(parent, startId, destId);
        List<String> pathNames = new ArrayList<>();
        for (int id : pathIds) {
            pathNames.add(graph.getLocationName(id));
        }

        // Build step-by-step navigation breakdown
        List<String> steps = buildStepDetails(graph, pathIds);
        int stops = pathIds.size() - 1; // Number of edges traversed

        return new NavigationResult(algoName, canonicalStart, canonicalDest, pathNames, pathIds, dist[destId], stops, steps);
    }

    /**
     * Finds the path with the fewest stops (edges) using Breadth-First Search (BFS).
     *
     * @param graph Campus graph containing locations and roads
     * @param startLoc Starting location name
     * @param destLoc Destination location name
     * @return NavigationResult containing the fewest stops route details or error
     */
    public static NavigationResult findFewestStops(CampusGraph graph, String startLoc, String destLoc) {
        String algoName = "BFS (Fewest Stops)";

        // 1. Validate starting location
        if (startLoc == null || startLoc.trim().isEmpty()) {
            return new NavigationResult(algoName, startLoc, destLoc, "Starting location cannot be empty.");
        }
        String canonicalStart = graph.resolveCanonicalName(startLoc);
        if (canonicalStart == null) {
            return new NavigationResult(algoName, startLoc, destLoc, "Location " + startLoc.trim() + " not found.");
        }

        // 2. Validate destination location
        if (destLoc == null || destLoc.trim().isEmpty()) {
            return new NavigationResult(algoName, startLoc, destLoc, "Destination location cannot be empty.");
        }
        String canonicalDest = graph.resolveCanonicalName(destLoc);
        if (canonicalDest == null) {
            return new NavigationResult(algoName, startLoc, destLoc, "Location " + destLoc.trim() + " not found.");
        }

        int startId = graph.getVertexId(canonicalStart);
        int destId = graph.getVertexId(canonicalDest);

        // 3. Boundary case: Identical source and destination
        if (startId == destId) {
            List<String> pathNames = Collections.singletonList(canonicalStart);
            List<Integer> pathIds = Collections.singletonList(startId);
            List<String> steps = Collections.singletonList("Already at destination: " + canonicalStart + " (0 m)");
            return new NavigationResult(algoName, canonicalStart, canonicalDest, pathNames, pathIds, 0, 0, steps);
        }

        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        Queue<Integer> queue = new ArrayDeque<>();

        visited[startId] = true;
        queue.add(startId);

        boolean reached = false;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            if (u == destId) {
                reached = true;
                break;
            }

            for (Edge edge : graph.getNeighbors(u)) {
                int v = edge.getTo();
                if (!visited[v]) {
                    visited[v] = true;
                    parent[v] = u;
                    queue.add(v);

                    if (v == destId) {
                        reached = true;
                        break;
                    }
                }
            }

            if (reached) {
                break;
            }
        }

        // 4. Check if destination is reachable
        if (!reached || parent[destId] == -1) {
            return new NavigationResult(algoName, canonicalStart, canonicalDest,
                    "No route found between " + canonicalStart + " and " + canonicalDest + ". The locations are disconnected.");
        }

        // 5. Reconstruct complete path using parent array
        List<Integer> pathIds = reconstructPath(parent, startId, destId);
        List<String> pathNames = new ArrayList<>();
        for (int id : pathIds) {
            pathNames.add(graph.getLocationName(id));
        }

        // Calculate total distance along the BFS path by summing edge weights
        int totalDistance = 0;
        for (int i = 0; i < pathIds.size() - 1; i++) {
            int u = pathIds.get(i);
            int v = pathIds.get(i + 1);
            Integer legDistance = graph.getEdgeDistance(u, v);
            if (legDistance != null) {
                totalDistance += legDistance;
            }
        }

        List<String> steps = buildStepDetails(graph, pathIds);
        int stops = pathIds.size() - 1; // Number of edges traversed

        return new NavigationResult(algoName, canonicalStart, canonicalDest, pathNames, pathIds, totalDistance, stops, steps);
    }

    /**
     * Reconstructs the path from startId to destId using the parent array.
     */
    private static List<Integer> reconstructPath(int[] parent, int startId, int destId) {
        List<Integer> path = new ArrayList<>();
        int curr = destId;
        while (curr != -1) {
            path.add(curr);
            if (curr == startId) {
                break;
            }
            curr = parent[curr];
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Builds human-readable step-by-step directions for the path.
     */
    private static List<String> buildStepDetails(CampusGraph graph, List<Integer> pathIds) {
        List<String> steps = new ArrayList<>();
        for (int i = 0; i < pathIds.size() - 1; i++) {
            int u = pathIds.get(i);
            int v = pathIds.get(i + 1);
            String nameU = graph.getLocationName(u);
            String nameV = graph.getLocationName(v);
            Integer d = graph.getEdgeDistance(u, v);
            int distance = (d != null) ? d : 0;
            steps.add(String.format("Step %d: %s ➔ %s (%d m)", (i + 1), nameU, nameV, distance));
        }
        return steps;
    }
}
