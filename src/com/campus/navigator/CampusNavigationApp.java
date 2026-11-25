package com.campus.navigator;

import com.campus.navigator.algorithm.GraphAlgorithms;
import com.campus.navigator.model.CampusGraph;
import com.campus.navigator.model.NavigationResult;
import com.campus.navigator.test.CampusNavigationTest;
import com.campus.navigator.ui.NavigationDashboard;

import javax.swing.*;
import java.util.Scanner;

/**
 * CampusNavigationApp - Main Entry Point for Problem Statement 82.
 * 
 * Features:
 * 1. Default mode: Launches the modern graphical Java Swing Campus Navigation Dashboard.
 * 2. CLI mode (--cli): Interactive console interface for terminal environments.
 * 3. Test mode (--test): Automated test suite verifying all required test cases.
 */
public class CampusNavigationApp {

    public static void main(String[] args) {
        // Crisp font rendering properties for Java Swing
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Check command-line flags
        if (args.length > 0) {
            String flag = args[0].trim().toLowerCase();
            if ("--test".equals(flag) || "-t".equals(flag)) {
                CampusNavigationTest.main(args);
                return;
            } else if ("--cli".equals(flag) || "-c".equals(flag)) {
                runCliMenu();
                return;
            } else if ("--help".equals(flag) || "-h".equals(flag)) {
                printHelp();
                return;
            }
        }

        // Default: Launch Modern Java Swing GUI Dashboard
        launchGui();
    }

    /**
     * Launches the Java Swing GUI Dashboard.
     */
    public static void launchGui() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Try system look and feel for native OS integration
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fallback to cross-platform Look and Feel
            }

            CampusGraph graph = new CampusGraph();
            NavigationDashboard dashboard = new NavigationDashboard(graph);
            dashboard.setVisible(true);
        });
    }

    /**
     * Interactive Command-Line Interface (CLI) menu.
     * Useful for terminal environments and automated scripts.
     */
    public static void runCliMenu() {
        CampusGraph graph = new CampusGraph();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==========================================================");
        System.out.println("           CAMPUS NAVIGATION APPLICATION (CLI MODE)        ");
        System.out.println("==========================================================");

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("  1. Add Location");
            System.out.println("  2. Add Path / Road");
            System.out.println("  3. Find Shortest Distance (Dijkstra)");
            System.out.println("  4. Find Fewest Stops (BFS)");
            System.out.println("  5. Display All Campus Locations");
            System.out.println("  6. Run Automated Test Cases");
            System.out.println("  7. Launch GUI Dashboard");
            System.out.println("  8. Exit");
            System.out.print("Enter choice (1-8): ");

            String input = scanner.nextLine().trim();
            if ("8".equals(input) || "exit".equalsIgnoreCase(input)) {
                System.out.println("Thank you for using Campus Navigator. Goodbye!");
                break;
            }

            switch (input) {
                case "1": {
                    System.out.print("Enter location name: ");
                    String name = scanner.nextLine().trim();
                    String res = graph.addLocation(name);
                    if ("SUCCESS".equals(res)) {
                        System.out.println("✓ Location '" + name + "' added successfully.");
                    } else {
                        System.out.println("✗ " + res);
                    }
                    break;
                }
                case "2": {
                    System.out.print("Enter first location: ");
                    String loc1 = scanner.nextLine().trim();
                    System.out.print("Enter second location: ");
                    String loc2 = scanner.nextLine().trim();
                    System.out.print("Enter distance in metres: ");
                    try {
                        int dist = Integer.parseInt(scanner.nextLine().trim());
                        String res = graph.addPath(loc1, loc2, dist);
                        if ("SUCCESS".equals(res)) {
                            System.out.println("✓ Road added between " + loc1 + " and " + loc2 + " (" + dist + " m).");
                        } else {
                            System.out.println("✗ " + res);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("✗ Distance must be a valid positive integer.");
                    }
                    break;
                }
                case "3": {
                    System.out.print("From: ");
                    String src = scanner.nextLine().trim();
                    System.out.print("To: ");
                    String dst = scanner.nextLine().trim();
                    NavigationResult res = GraphAlgorithms.findShortestDistance(graph, src, dst);
                    if (res.isSuccess()) {
                        System.out.println("\nExpected Output:");
                        System.out.println("Path: " + res.getFormattedPath());
                        System.out.println("Distance: " + res.getTotalDistance() + " m");
                        System.out.println("Stops: " + res.getTotalStops());
                    } else {
                        System.out.println("\n" + res.getErrorMessage());
                    }
                    break;
                }
                case "4": {
                    System.out.print("From: ");
                    String src = scanner.nextLine().trim();
                    System.out.print("To: ");
                    String dst = scanner.nextLine().trim();
                    NavigationResult res = GraphAlgorithms.findFewestStops(graph, src, dst);
                    if (res.isSuccess()) {
                        System.out.println("\nExpected Output:");
                        System.out.println("Path: " + res.getFormattedPath());
                        System.out.println("Stops: " + res.getTotalStops());
                        System.out.println("Distance: " + res.getTotalDistance() + " m");
                    } else {
                        System.out.println("\n" + res.getErrorMessage());
                    }
                    break;
                }
                case "5": {
                    System.out.println("\nCampus Locations (" + graph.getVertexCount() + "):");
                    for (String loc : graph.getAllLocationNames()) {
                        System.out.println("  • " + loc);
                    }
                    break;
                }
                case "6": {
                    CampusNavigationTest.main(new String[]{});
                    break;
                }
                case "7": {
                    System.out.println("Launching GUI Dashboard...");
                    launchGui();
                    break;
                }
                default:
                    System.out.println("Invalid option. Please choose 1-8.");
            }
        }
    }

    private static void printHelp() {
        System.out.println("Campus Navigator - Usage Options:");
        System.out.println("  java -cp bin com.campus.navigator.CampusNavigationApp          (Launches GUI Dashboard)");
        System.out.println("  java -cp bin com.campus.navigator.CampusNavigationApp --cli    (Runs interactive CLI)");
        System.out.println("  java -cp bin com.campus.navigator.CampusNavigationApp --test   (Runs automated test suite)");
        System.out.println("  java -cp bin com.campus.navigator.CampusNavigationApp --help   (Displays this help message)");
    }
}
