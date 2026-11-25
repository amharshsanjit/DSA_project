package com.rungta.campus;

/**
 * Spring Boot Main Application entry point.
 */
public class RungtaCampusApplication {
    public static void main(String[] args) {
        try {
            RungtaServer.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
