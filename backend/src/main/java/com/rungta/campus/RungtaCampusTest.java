package com.rungta.campus;

import com.rungta.campus.algorithm.BfsAlgorithm;
import com.rungta.campus.algorithm.DijkstraAlgorithm;
import com.rungta.campus.model.*;
import com.rungta.campus.repository.CampusDataStore;

import java.util.List;

/**
 * Automated test suite for Rungta R1 Smart Campus Navigation System.
 * Tests all 10 scenarios outlined in Section 14 of the requirements.
 */
public class RungtaCampusTest {

    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   RUNGTA R1 CAMPUS NAVIGATION - AUTOMATED TEST SUITE     ");
        System.out.println("   Campus: Kohka-Kurud Road, Bhilai, Chhattisgarh, India  ");
        System.out.println("==========================================================\n");

        CampusDataStore dataStore = new CampusDataStore();

        // 1. Search for Central Library
        test("Scenario 1: Search for Central Library", () -> {
            List<SearchResult> results = dataStore.search("Central Library", null);
            assertTrue(!results.isEmpty(), "Central Library must be found");
            boolean found = results.stream().anyMatch(r -> r.getTitle().contains("Library"));
            assertTrue(found, "Results must contain Central Library");
            System.out.println("  ✓ Found: " + results.get(0).getTitle() + " at " + results.get(0).getLatitude() + ", " + results.get(0).getLongitude());
        });

        // 2. Search for Auditorium (Santosh Rungta B-Block)
        test("Scenario 2: Search for Auditorium (B-Block)", () -> {
            List<SearchResult> results = dataStore.search("Auditorium", null);
            assertTrue(!results.isEmpty(), "Auditorium must be found");
            boolean bBlockFound = results.stream().anyMatch(r -> r.getTitle().contains("B-Block") || r.getTitle().contains("Auditorium"));
            assertTrue(bBlockFound, "Results must reference B-Block Auditorium");
            System.out.println("  ✓ Found: " + results.get(0).getTitle() + " (" + results.get(0).getSubtitle() + ")");
        });

        // 3. Search for Engineering College
        test("Scenario 3: Search for Engineering College", () -> {
            List<SearchResult> results = dataStore.search("Engineering", null);
            assertTrue(!results.isEmpty(), "Engineering College must be found");
            System.out.println("  ✓ Found: " + results.get(0).getTitle());
        });

        // 4. Navigate from Main Gate to Central Library
        test("Scenario 4: Navigate from Main Gate to Central Library", () -> {
            NavigationResult res = DijkstraAlgorithm.findShortestPath(dataStore.getGraph(), "node_gate", "node_library");
            assertTrue(res.isSuccess(), "Route calculation must succeed");
            assertTrue(res.getTotalDistanceMeters() > 0, "Distance must be positive");
            System.out.println("  ✓ Shortest Path: " + String.join(" → ", res.getPathNodeIds()));
            System.out.println("  ✓ Distance: " + res.getTotalDistanceMeters() + " m (" + res.getEstimatedWalkingTimeMinutes() + " mins)");
            System.out.println("  ✓ Stops: " + res.getTotalStops());
        });

        // 5. Navigate between two buildings (Block A Administration to Block C Computing)
        test("Scenario 5: Navigate between Block A and Block C", () -> {
            NavigationResult dijkstraRes = DijkstraAlgorithm.findShortestPath(dataStore.getGraph(), "node_admin", "node_cs");
            NavigationResult bfsRes = BfsAlgorithm.findFewestStopsPath(dataStore.getGraph(), "node_admin", "node_cs");
            assertTrue(dijkstraRes.isSuccess() && bfsRes.isSuccess(), "Both algorithms must find routes");
            System.out.println("  ✓ Dijkstra: " + dijkstraRes.getTotalDistanceMeters() + " m, " + dijkstraRes.getTotalStops() + " stops");
            System.out.println("  ✓ BFS:      " + bfsRes.getTotalDistanceMeters() + " m, " + bfsRes.getTotalStops() + " stops");
        });

        // 6. Search for non-existing classroom
        test("Scenario 6: Search for non-existing classroom", () -> {
            List<SearchResult> results = dataStore.search("Room XYZ-999-NonExistent", null);
            assertTrue(results.isEmpty(), "No results should be returned for non-existing room");
            System.out.println("  ✓ Handled non-existing classroom cleanly with 0 results");
        });

        // 7. Select an unverified vs verified floor
        test("Scenario 7: Select verified floor data", () -> {
            Floor fl = dataStore.getFloor("fl_b_g");
            assertTrue(fl != null, "Ground floor of Block B must exist");
            assertTrue(fl.isVerified(), "Verified status must be explicitly recorded");
            System.out.println("  ✓ Floor: " + fl.getFloorName() + " (Verified: " + fl.isVerified() + ")");
        });

        // 8. Add a new building through admin
        test("Scenario 8: Add a new building dynamically", () -> {
            Building newBuilding = new Building("b_robotics", "Robotics & Innovation Pavilion", "ROBOTICS", "inst_rcet",
                    "LABORATORY", 21.2361, 81.3466, 2, false, "Internal Plan", "2024-03-01", "New experimental robotics workshop.");
            dataStore.addBuilding(newBuilding);
            Building retrieved = dataStore.getBuilding("b_robotics");
            assertTrue(retrieved != null && !retrieved.isVerified(), "Newly added unverified building must be recorded");
            System.out.println("  ✓ Added dynamic building: " + retrieved.getName() + " (Verified: " + retrieved.isVerified() + ")");
        });

        // 9. Add a new walking path
        test("Scenario 9: Add a new walking path dynamically", () -> {
            GraphNode newNode = new GraphNode("node_robotics", "Robotics Pavilion", "BUILDING", 21.2361, 81.3466, "b_robotics");
            dataStore.getGraph().addNode(newNode);
            GraphEdge newEdge = new GraphEdge("e_cs_robotics", "node_cs", "node_robotics", 55, 42, true, "PAVED_WALKWAY", true);
            dataStore.getGraph().addBidirectionalEdge(newEdge);

            NavigationResult res = DijkstraAlgorithm.findShortestPath(dataStore.getGraph(), "node_gate", "node_robotics");
            assertTrue(res.isSuccess(), "Route must reach newly connected node");
            System.out.println("  ✓ Path to new building: " + String.join(" → ", res.getPathNodeIds()) + " (" + res.getTotalDistanceMeters() + " m)");
        });

        // 10. Verify Graph and Map update correctly
        test("Scenario 10: Graph topology integrity", () -> {
            assertTrue(dataStore.getGraph().getNodeCount() >= 13, "Graph must contain all campus nodes");
            assertTrue(!dataStore.getGraph().getAllUniqueEdges().isEmpty(), "Graph must have connected walking network");
            System.out.println("  ✓ Total Graph Nodes: " + dataStore.getGraph().getNodeCount());
            System.out.println("  ✓ Total Walking Edges: " + dataStore.getGraph().getAllUniqueEdges().size());
        });

        System.out.println("\n==========================================================");
        System.out.printf("  RESULTS: %d / %d TESTS PASSED (%.1f%%)%n", testsPassed, testsTotal, (testsPassed * 100.0 / testsTotal));
        System.out.println("==========================================================");
    }

    private static void test(String name, Runnable runnable) {
        testsTotal++;
        System.out.println("----------------------------------------------------------");
        System.out.println("RUNNING: " + name);
        try {
            runnable.run();
            testsPassed++;
            System.out.println("STATUS: [PASSED] ✓\n");
        } catch (AssertionError | Exception e) {
            System.err.println("STATUS: [FAILED] ✗ -> " + e.getMessage() + "\n");
        }
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) throw new AssertionError(msg);
    }
}
