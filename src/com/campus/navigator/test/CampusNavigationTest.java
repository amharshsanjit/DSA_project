package com.campus.navigator.test;

import com.campus.navigator.algorithm.GraphAlgorithms;
import com.campus.navigator.model.CampusGraph;
import com.campus.navigator.model.NavigationResult;

/**
 * Automated test suite verifying all 4 mandatory test cases, boundary conditions,
 * dynamic location & path additions, and disconnected node handling.
 */
public class CampusNavigationTest {

    private static int testsPassed = 0;
    private static int testsTotal = 0;

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("     CAMPUS NAVIGATION APPLICATION - AUTOMATED TEST SUITE  ");
        System.out.println("==========================================================\n");

        CampusGraph graph = new CampusGraph();

        // ----------------------------------------------------
        // Test Case 1: Normal Input (Dijkstra Shortest Distance)
        // ----------------------------------------------------
        runTestCase("Test Case 1: Normal Input (Gate to Canteen - Shortest Distance)", () -> {
            NavigationResult res = GraphAlgorithms.findShortestDistance(graph, "Gate", "Canteen");
            printResult(res);

            boolean pass = res.isSuccess() &&
                    res.getFormattedPath().equals("Gate → Admin → Library → Canteen") &&
                    res.getTotalDistance() == 150;
            assertTrue(pass, "Expected Path: 'Gate → Admin → Library → Canteen', Distance: 150 m");
        });

        // ----------------------------------------------------
        // Test Case 2: Boundary / Minimum Input (Gate to Gate)
        // ----------------------------------------------------
        runTestCase("Test Case 2: Boundary / Minimum Input (Gate to Gate)", () -> {
            NavigationResult res = GraphAlgorithms.findShortestDistance(graph, "Gate", "Gate");
            printResult(res);

            boolean pass = res.isSuccess() &&
                    res.getFormattedPath().equals("Gate") &&
                    res.getTotalDistance() == 0;
            assertTrue(pass, "Expected Path: 'Gate', Distance: 0 m");
        });

        // ----------------------------------------------------
        // Test Case 3: Invalid Input (Gate to Hostel)
        // ----------------------------------------------------
        runTestCase("Test Case 3: Invalid Input (Gate to Hostel)", () -> {
            NavigationResult res = GraphAlgorithms.findShortestDistance(graph, "Gate", "Hostel");
            System.out.println("  Output: " + res.getErrorMessage());

            boolean pass = !res.isSuccess() &&
                    res.getErrorMessage() != null &&
                    res.getErrorMessage().contains("Location Hostel not found");
            assertTrue(pass, "Expected error containing 'Location Hostel not found.'");
        });

        // ----------------------------------------------------
        // Test Case 4(a): Different Valid Scenario - Fewest Stops (Admin to Canteen)
        // ----------------------------------------------------
        runTestCase("Test Case 4(a): Fewest Stops (Admin to Canteen)", () -> {
            NavigationResult res = GraphAlgorithms.findFewestStops(graph, "Admin", "Canteen");
            printResult(res);

            boolean pass = res.isSuccess() &&
                    res.getFormattedPath().equals("Admin → Canteen") &&
                    res.getTotalStops() == 1 &&
                    res.getTotalDistance() == 200;
            assertTrue(pass, "Expected Path: 'Admin → Canteen', Stops: 1, Distance: 200 m");
        });

        // ----------------------------------------------------
        // Test Case 4(b): Different Valid Scenario - Shortest Distance (Admin to Canteen)
        // ----------------------------------------------------
        runTestCase("Test Case 4(b): Shortest Distance (Admin to Canteen)", () -> {
            NavigationResult res = GraphAlgorithms.findShortestDistance(graph, "Admin", "Canteen");
            printResult(res);

            boolean pass = res.isSuccess() &&
                    res.getFormattedPath().equals("Admin → Library → Canteen") &&
                    res.getTotalDistance() == 100;
            assertTrue(pass, "Expected Path: 'Admin → Library → Canteen', Distance: 100 m");
        });

        // ----------------------------------------------------
        // Test Case 5: Disconnected Location Handling
        // ----------------------------------------------------
        runTestCase("Test Case 5: Disconnected Location (Gate to Gate-Admin before edge added)", () -> {
            NavigationResult res = GraphAlgorithms.findShortestDistance(graph, "Gate", "Gate-Admin");
            System.out.println("  Output: " + res.getErrorMessage());

            boolean pass = !res.isSuccess() &&
                    res.getErrorMessage() != null &&
                    res.getErrorMessage().contains("disconnected");
            assertTrue(pass, "Expected disconnected error message");
        });

        // ----------------------------------------------------
        // Test Case 6: Dynamic Add Location and Add Path
        // ----------------------------------------------------
        runTestCase("Test Case 6: Dynamic Add Location & Add Path", () -> {
            String addLocMsg = graph.addLocation("Sports Complex", 750, 400, "Athletics");
            System.out.println("  Add Location result: " + addLocMsg);
            assertTrue("SUCCESS".equals(addLocMsg), "Should add 'Sports Complex'");

            String addPathMsg = graph.addPath("Canteen", "Sports Complex", 80);
            System.out.println("  Add Path result: " + addPathMsg);
            assertTrue("SUCCESS".equals(addPathMsg), "Should add edge Canteen ↔ Sports Complex (80 m)");

            // Now test Dijkstra from Gate to Sports Complex
            // Gate -> Admin (50) -> Library (40) -> Canteen (60) -> Sports Complex (80) = 230 m
            NavigationResult res = GraphAlgorithms.findShortestDistance(graph, "Gate", "Sports Complex");
            printResult(res);

            boolean pass = res.isSuccess() &&
                    res.getFormattedPath().equals("Gate → Admin → Library → Canteen → Sports Complex") &&
                    res.getTotalDistance() == 230;
            assertTrue(pass, "Expected path ending at Sports Complex with total distance 230 m");
        });

        // ----------------------------------------------------
        // Test Case 7: Validation - Negative Distance & Duplicate Location
        // ----------------------------------------------------
        runTestCase("Test Case 7: Validation - Negative Distance & Duplicate Location", () -> {
            String dupMsg = graph.addLocation("Gate");
            System.out.println("  Duplicate Location check: " + dupMsg);
            assertTrue(dupMsg.contains("already exists"), "Duplicate location should be rejected");

            String negPathMsg = graph.addPath("Gate", "Admin", -20);
            System.out.println("  Negative Distance check: " + negPathMsg);
            assertTrue(negPathMsg.contains("positive number"), "Negative distance should be rejected");
        });

        // ----------------------------------------------------
        // Summary
        // ----------------------------------------------------
        System.out.println("==========================================================");
        System.out.printf("  TEST RESULTS: %d / %d PASSED (%.1f%%)%n",
                testsPassed, testsTotal, (testsPassed * 100.0 / testsTotal));
        System.out.println("==========================================================");

        if (testsPassed != testsTotal) {
            System.exit(1);
        }
    }

    private static void runTestCase(String title, Runnable test) {
        testsTotal++;
        System.out.println("----------------------------------------------------------");
        System.out.println("RUNNING: " + title);
        try {
            test.run();
            testsPassed++;
            System.out.println("STATUS: [PASSED] ✓\n");
        } catch (AssertionError | Exception e) {
            System.err.println("STATUS: [FAILED] ✗ -> " + e.getMessage() + "\n");
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void printResult(NavigationResult res) {
        System.out.println("  Algorithm: " + res.getAlgorithmName());
        System.out.println("  Path:      " + res.getFormattedPath());
        System.out.println("  Distance:  " + res.getTotalDistance() + " m");
        System.out.println("  Stops:     " + res.getTotalStops());
    }
}
